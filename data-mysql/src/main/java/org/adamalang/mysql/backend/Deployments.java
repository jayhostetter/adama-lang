/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.mysql.backend;

import org.adamalang.ErrorCodes;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.mysql.DataBase;

import java.sql.*;
import java.util.ArrayList;

public class Deployments {
  public static void undeploy(DataBase dataBase, String space, String target) throws Exception {
    try (Connection connection = dataBase.pool.getConnection()) {
      { // delete prior versions
        String sql =
            new StringBuilder()
                .append("DELETE FROM `")
                .append(dataBase.databaseName)
                .append("`.`deployed` WHERE `space`=? AND `target`=?")
                .toString();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
          statement.setString(1, space);
          statement.setString(2, target);
          statement.execute();
        }
      }
    }
  }

  public static class Deployment {
    public final String space;
    public final String hash;
    public final String plan;

    public Deployment(String space, String hash, String plan) {
      this.space = space;
      this.hash = hash;
      this.plan = plan;
    }
  }

  public static ArrayList<Deployment> list(DataBase dataBase, String target) throws Exception {
    try (Connection connection = dataBase.pool.getConnection()) {
      String sql =
          new StringBuilder()
              .append("SELECT `space`, `hash`, `plan` FROM `")
              .append(dataBase.databaseName)
              .append("`.`deployed` WHERE `target`=? ORDER BY `space` ASC")
              .toString();

      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, target);
        try (ResultSet rs = statement.executeQuery()) {
          ArrayList<Deployment> results = new ArrayList<>();
          while (rs.next()) {
            results.add(new Deployment(rs.getString(1), rs.getString(2), rs.getString(3)));
          }
          return results;
        }
      }
    }
  }

  public static void deploy(DataBase dataBase, String space, String target, String hash, String plan) throws Exception {
    try (Connection connection = dataBase.pool.getConnection()) {
      { // delete prior versions
        String sql =
            new StringBuilder()
                .append("DELETE FROM `")
                .append(dataBase.databaseName)
                .append("`.`deployed` WHERE `space`=? AND `target`=?")
                .toString();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
          statement.setString(1, space);
          statement.setString(2, target);
          statement.execute();
        }
      }
      {
        String sql =
            new StringBuilder()
                .append("INSERT INTO `")
                .append(dataBase.databaseName)
                .append("`.`deployed` (`space`, `target`, `hash`, `plan`) VALUES (?,?,?,?)")
                .toString();
        try (PreparedStatement statement =
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
          statement.setString(1, space);
          statement.setString(2, target);
          statement.setString(3, hash);
          statement.setString(4, plan);
          statement.execute();
        }
      }
    }
  }
}
