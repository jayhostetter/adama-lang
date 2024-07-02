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
package org.adamalang.common.gossip;

import org.adamalang.common.gossip.codec.GossipProtocol;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.TreeSet;

public class InstanceSetTests extends CommonTest {
  @Test
  public void empty() {
    TreeSet<Instance> instances = new TreeSet<>();
    InstanceSet set = new InstanceSet(instances, 100);
    TreeSet<Instance> clone = set.clone();
    Assert.assertEquals(0, clone.size());
    int[] counters = set.counters();
    Assert.assertEquals(0, counters.length);
    GossipProtocol.Endpoint[] endpoints = set.toEndpoints();
    Assert.assertEquals(0, endpoints.length);
    GossipProtocol.Endpoint[] missing = set.missing(set);
    Assert.assertEquals(0, missing.length);
    set.ingest(new int[0], 40);
    Assert.assertEquals("1B2M2Y8AsgTpgAmY7PhCfg==", set.hash());
  }

  @Test
  public void singleton() {
    TreeSet<Instance> instances = new TreeSet<>();
    Instance a = A();
    instances.add(a);
    InstanceSet set = new InstanceSet(instances, 100);
    TreeSet<Instance> clone = set.clone();
    Assert.assertEquals(1, clone.size());
    Assert.assertEquals(A(), clone.first());
    int[] counters = set.counters();
    Assert.assertEquals(1, counters.length);
    Assert.assertEquals(100, counters[0]);
    GossipProtocol.Endpoint[] endpoints = set.toEndpoints();
    Assert.assertEquals(1, endpoints.length);
    Assert.assertEquals("id-a", endpoints[0].id);
    GossipProtocol.Endpoint[] missing1 = set.missing(set);
    Assert.assertEquals(0, missing1.length);
    GossipProtocol.Endpoint[] missing2 = set.missing(new InstanceSet(new TreeSet<>(), 0));
    Assert.assertEquals(1, missing2.length);
    Assert.assertEquals("id-a", missing2[0].id);
    ArrayList<Integer> newCounters = new ArrayList<>();
    set.ingest(new int[]{1000}, 40);
    Assert.assertEquals("rzhcX9WgM1AwjqKNBz6eJg==", set.hash());
    Assert.assertEquals(1000, a.counter());
  }

  @Test
  public void four_in_order() {
    assertFour(INSTANCES(A(), B(), C(), D()));
  }

  private void assertFour(TreeSet<Instance> instances) {
    InstanceSet set = new InstanceSet(instances, 100);
    TreeSet<Instance> clone = set.clone();
    Assert.assertEquals(4, clone.size());
    int[] counters = set.counters();
    Assert.assertEquals(4, counters.length);
    Assert.assertEquals(100, counters[0]);
    Assert.assertEquals(200, counters[1]);
    Assert.assertEquals(300, counters[2]);
    Assert.assertEquals(400, counters[3]);
    GossipProtocol.Endpoint[] endpoints = set.toEndpoints();
    Assert.assertEquals(4, endpoints.length);
    Assert.assertEquals("id-a", endpoints[0].id);
    Assert.assertEquals("id-b", endpoints[1].id);
    Assert.assertEquals("id-c", endpoints[2].id);
    Assert.assertEquals("id-d", endpoints[3].id);
    GossipProtocol.Endpoint[] missing1 = set.missing(set);
    Assert.assertEquals(0, missing1.length);
    GossipProtocol.Endpoint[] missing2 = set.missing(new InstanceSet(new TreeSet<>(), 0));
    Assert.assertEquals(4, missing2.length);
    Assert.assertEquals("id-a", missing2[0].id);
    Assert.assertEquals("id-b", missing2[1].id);
    Assert.assertEquals("id-c", missing2[2].id);
    Assert.assertEquals("id-d", missing2[3].id);
    TreeSet<Instance> subset1 = new TreeSet<>();
    subset1.add(A());
    subset1.add(C());
    GossipProtocol.Endpoint[] missing3 = set.missing(new InstanceSet(new TreeSet<>(subset1), 0));
    Assert.assertEquals(2, missing3.length);
    Assert.assertEquals("id-b", missing3[0].id);
    Assert.assertEquals("id-d", missing3[1].id);
    set.ingest(counters(1000, 2000, 3000, 4000), 200);
    Assert.assertEquals("ZOTUaGI8E3qWqKVSFNHp3Q==", set.hash());
    Assert.assertEquals(1000, instances.first().counter());
    Assert.assertEquals(4000, instances.last().counter());
  }

  @Test
  public void four_out_of_order() {
    assertFour(INSTANCES(A(), C(), B(), D()));
    assertFour(INSTANCES(C(), A(), D(), B()));
    assertFour(INSTANCES(D(), B(), C(), A()));
    assertFour(INSTANCES(D(), C(), B(), A()));
  }

  @Test
  public void four_duplicates() {
    assertFour(INSTANCES(A(), A(), B(), B(), D(), C()));
    assertFour(INSTANCES(A(), C(), D(), B(), D(), C()));
    assertFour(INSTANCES(A(), C(), D(), B(), D(), A()));
  }
}
