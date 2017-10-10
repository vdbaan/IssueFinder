/*
 *  Copyright (C) 2017  S. van der Baan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder.parser

import org.junit.Test

class ParserSelectTest {

    @Test
    void testSelectNessusFile() {
        assert Parser.getParser((new File("testdata/Nessus.nessus")).text) instanceof NessusParser
    }

    @Test
    void testSelectNMapFile() {
        assert Parser.getParser((new File("testdata/Nmap.xml")).text) instanceof NMapParser
    }

    @Test
    void testSelectNetsparkerFile() {
        assert Parser.getParser((new File("testdata/Netsparker.xml")).text) instanceof NetsparkerParser
    }

    @Test
    void testSelectNiktoFile() {
        assert Parser.getParser((new File("testdata/Nikto.xml")).text) instanceof NiktoParser
    }

    @Test
    void testSelectIllegalFile() {
        Parser p = Parser.getParser(getClass().getResource("/style.css"))
        assert p == null
    }
}
