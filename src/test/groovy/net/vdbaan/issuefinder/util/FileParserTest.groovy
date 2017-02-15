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

package net.vdbaan.issuefinder.util

import net.vdbaan.issuefinder.model.Finding
import org.junit.Test
import static org.junit.Assert.*

class NessusParserTest {

    @Test
    void testEmptyNessus() {
        Parser p = Parser.getParser('<NessusClientData_v2></NessusClientData_v2>')
        assert p instanceof NessusParser
        List<Finding> result = p.parse()
        assertTrue(result.isEmpty())
    }

    @Test
    void testNessusFile() {
        Parser p = Parser.getParser(new File("testdata/Nessus.nessus").text)
        assert p instanceof NessusParser
        List result = p.parse()
        assertFalse(result.isEmpty())
        assert result[0] instanceof Finding
    }
}

class NmapParserTest {

    @Test
    void testEmptyNmap() {
        Parser p = Parser.getParser('<nmaprun></nmaprun>')
        assert p instanceof NMapParser
        List<Finding> result = p.parse()
        assertTrue(result.isEmpty())
    }

    @Test
    void testNmapFile() {
        Parser p = Parser.getParser(new File("testdata/Nmap.xml").text)
        assert p instanceof NMapParser
        List result = p.parse()
        assertFalse(result.isEmpty())
        assert result[0] instanceof Finding
    }
}

class TestSSLParserTest {

    @Test
    void testEmptyJSon() {
        try {
            Parser p = Parser.getParser('{}')
            assert p instanceof TestSSLParser
            List<Finding> result = p.parse()
            assertTrue(result.isEmpty())
        } catch(Exception e) {

        }
    }

    @Test
    void testJSonFile() {
        Parser p = Parser.getParser(new File("testdata/testssl.json").text)
        assert p instanceof TestSSLParser
        List<Finding> result = p.parse()
        assertFalse(result.isEmpty())
        assert result[0] instanceof Finding
    }
}