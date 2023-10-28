/*
* Adama Platform and Language
* Copyright (C) 2021 - 2023 by Adama Platform Initiative, LLC
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
package org.adamalang.runtime.graph;

import java.util.HashMap;
import java.util.TreeSet;

public class SubGraph {
  private final HashMap<Integer, TreeSet<Integer>> forward;
  private final HashMap<Integer, TreeSet<Integer>> reverse;

  public SubGraph() {
    this.forward = new HashMap<>();
    this.reverse = new HashMap<>();
  }

  private static void remove1(HashMap<Integer, TreeSet<Integer>> edges, int from, int to) {
    TreeSet<Integer> right = edges.get(from);
    if (right != null) {
      right.remove(to);
      if (right.size() == 0) {
        edges.remove(from);
      }
    }
  }

  private static void put1(HashMap<Integer, TreeSet<Integer>> edges, int from, int to) {
    TreeSet<Integer> dest = edges.get(from);
    if (dest == null) {
      dest = new TreeSet<>();
      edges.put(from, dest);
    }
    dest.add(to);
  }

  public void remove(int from, int to) {
    remove1(forward, from, to);
    remove1(reverse, to, from);
  }

  public void put(int from, int to) {
    put1(forward, from, to);
    put1(reverse, to, from);
  }
}