/*
* Adama Platform and Language
* Copyright (C) 2021 - 2024 by Adama Platform Engineering, LLC
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published
* by the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Affero General Public License for more details.
* 
* You should have received a copy of the GNU Affero General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.adamalang.common.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.adamalang.ErrorCodes;
import org.adamalang.common.*;
import org.adamalang.common.gossip.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/** defines the threading base for the common networking library */
public class NetBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(NetBase.class);
  private static final ExceptionLogger EXLOGGER = ExceptionLogger.FOR(LOGGER);
  public final NetMetrics metrics;
  public final NioEventLoopGroup bossGroup;
  public final NioEventLoopGroup workerGroup;
  public final MachineIdentity identity;
  private final AtomicBoolean alive;
  private final CountDownLatch killLatch;
  private final SslContext sslContext;
  private final Engine engine;

  public NetBase(NetMetrics metrics, MachineIdentity identity, int bossThreads, int workerThreads) throws Exception {
    this.metrics = metrics;
    this.identity = identity;
    this.sslContext = SslContextBuilder.forClient().keyManager(identity.getCert(), identity.getKey()).trustManager(identity.getTrust()).build();
    this.bossGroup = new NioEventLoopGroup(bossThreads);
    this.workerGroup = new NioEventLoopGroup(workerThreads);
    this.alive = new AtomicBoolean(true);
    this.killLatch = new CountDownLatch(1);
    this.engine = new Engine(identity.ip, metrics.gossip, TimeSource.REAL_TIME);
  }

  public Engine startGossiping() {
    engine.kickoff(alive);
    return engine;
  }

  public boolean alive() {
    return alive.get();
  }

  public void connect(String target, Lifecycle lifecycle) {
    if (!alive.get()) {
      lifecycle.failed(new ErrorCodeException(ErrorCodes.NET_SHUTTING_DOWN));
    }
    try {
      String[] parts = target.split(Pattern.quote(":"));
      String peerHost = parts[0];
      int peerPort = Integer.parseInt(parts[1]);
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(workerGroup);
      bootstrap.remoteAddress(peerHost, peerPort);
      bootstrap.channel(NioSocketChannel.class);
      bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
      bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 250);
      bootstrap.option(ChannelOption.TCP_NODELAY, true);
      bootstrap.handler(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
          metrics.net_create_client_handler.run();
          ch.pipeline().addLast(sslContext.newHandler(ch.alloc(), peerHost, peerPort));
          ch.pipeline().addLast(new LengthFieldPrepender(4));
          ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(67108864, 0, 4, 0, 4));
          ch.pipeline().addLast(new ChannelClient(peerHost, peerPort, lifecycle, engine));
        }
      });
      bootstrap.connect().addListener(new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
          if (!channelFuture.isSuccess()) {
            if (!StartUp.hasRecentlyStartedUp()) {
              LOGGER.error("failed-connect({}) : {}", target, channelFuture.cause().getMessage());
            }
            lifecycle.failed(new ErrorCodeException(ErrorCodes.NET_CONNECT_FAILED_TO_CONNECT));
          }
        }
      });
    } catch (Exception ex) {
      lifecycle.failed(ErrorCodeException.detectOrWrap(ErrorCodes.NET_CONNECT_FAILED_UNKNOWN, ex, EXLOGGER));
    }
  }

  public ServerHandle serve(int port, Handler handler) throws Exception {
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workerGroup);
    bootstrap.channel(NioServerSocketChannel.class);
    bootstrap.localAddress(port);
    SslContext sslContext = makeServerSslContext();
    SocketChannelSet set = new SocketChannelSet();
    bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
      protected void initChannel(SocketChannel ch) throws Exception {
        metrics.net_create_server_handler.run();
        ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
        ch.pipeline().addLast(new LengthFieldPrepender(4));
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(67108864, 0, 4, 0, 4));
        ch.pipeline().addLast(new ChannelServer(ch, set, handler, engine));
      }
    });
    ChannelFuture future = bootstrap.bind();
    LOGGER.info("started");
    CountDownLatch waitForEndLatch = new CountDownLatch(1);
    return new ServerHandle() {
      @Override
      public void waitForEnd() {
        LOGGER.info("waiting");
        try {
          future.channel().closeFuture().sync();
        } catch (Exception ex) {
          LOGGER.info("failure", ex);
          ex.printStackTrace();
        }
        LOGGER.info("finished");
        waitForEndLatch.countDown();
      }

      @Override
      public void kill() {
        try {
          future.channel().close().sync();
          if (future.channel().parent() != null) {
            future.channel().parent().close().sync();
          }
          set.kill();
          waitForEndLatch.await(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
          throw new RuntimeException(ie);
        }
      }
    };
  }

  public SslContext makeServerSslContext() throws Exception {
    return SslContextBuilder.forServer(identity.getCert(), identity.getKey()).trustManager(identity.getTrust()).clientAuth(ClientAuth.REQUIRE).build();
  }

  public void waitForShutdown() throws InterruptedException {
    killLatch.await();
  }

  public void shutdown() {
    alive.set(false);
    killLatch.countDown();
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }
}
