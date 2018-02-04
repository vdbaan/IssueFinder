/*
 *  Copyright (C) 2018  S. van der Baan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder.util

import org.junit.Test

import static org.junit.Assert.assertEquals

class ContainerTest {
    @Test
    void baseTest() {
        Container c = new Container()
        String text = c.toString()
        assertEquals("open ports (0)\t\n" +
                "found services (0)\t\n" +
                "Highest vulnerability (0 UNKNOWN)\t\n", text)
    }
}
