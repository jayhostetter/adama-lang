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
package org.adamalang.common.rate;

/** a grant that operations can happen */
public class TokenGrant {
  /** this is how many tokens you have been granted */
  public final int tokens;

  /** Don't call back until this time frame. If you try, then either reject or delay */
  public final int millseconds;

  public TokenGrant(int tokens, int millseconds) {
    this.tokens = tokens;
    this.millseconds = millseconds;
  }
}
