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

        File testFile = new File("testdata/Nessus.nessus")
        if(testFile.exists()) {
            Parser p = Parser.getParser(testFile.text)
            assert p instanceof NessusParser
            List result = p.parse()
            assertFalse(result.isEmpty())
            assert result[0] instanceof Finding
        }
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
        File testFile = new File("testdata/Nmap.xml")
        if(testFile.exists()) {
            Parser p = Parser.getParser(testFile.text)
            assert p instanceof NMapParser
            List result = p.parse()
            assertFalse(result.isEmpty())
            assert result[0] instanceof Finding
        }
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
        File testFile = new File("testdata/testssl-4-4848.json")
        if(testFile.exists()) {
            Parser p = Parser.getParser(testFile.text)
            assert p instanceof TestSSLParser
            List<Finding> result = p.parse()
            assertFalse(result.isEmpty())
            assert result[0] instanceof Finding
        }
    }
}

class NiktoParserTest {

    @Test
    void testEmptyNikto() {
        Parser p = Parser.getParser('<niktoscan></niktoscan>')
        assert p instanceof NiktoParser
        List<Finding> result = p.parse()

        // Nikto Parser, by default adds scaninfo to the result
        assertTrue(result.size() == 1)
    }

    @Test
    void testNiktoFile() {
        File testFile = new File("testdata/Nikto.xml")
        if(testFile.exists()) {
            Parser p = Parser.getParser(testFile.text)
            assert p instanceof NiktoParser
            List result = p.parse()
            assertFalse(result.isEmpty())
            assert result[0] instanceof Finding
        }
    }
}


class ArachniParserTest {
    @Test
    void testEmptyArachni() {
        Parser p = Parser.getParser('<report><sitemap><entry url="http://127.0.0.1/" code="200"/></sitemap><issues/></report>')
        assert p instanceof ArachniParser
        List<Finding> result = p.parse()
        assertTrue(result.isEmpty())
    }

    @Test
    void testArachniFile() {
        File testFile = new File("testdata/2017-02-21 10_47_30+0000.xml")
        if(testFile.exists()) {
            Parser p = Parser.getParser(testfile.text)
            assert p instanceof ArachniParser
            List result = p.parse()
            assertFalse(result.isEmpty())
            assert result[0] instanceof Finding
        }
    }
}

class BurpParserTest {
    @Test
    void testEmptyBurp() {
        Parser p = Parser.getParser('<issues burpVersion="1.7.17" exportTime="Tue Feb 21 14:27:01 GMT 2017" />')
        assert p instanceof BurpParser
        List<Finding> result = p.parse()
        assertTrue(result.isEmpty())
    }
    @Test
    void testBurpFile() {
        File testFile = new File("testdata/burp.xml")
        if(testFile.exists()) {
            Parser p = Parser.getParser(testFile.text)
            assert p instanceof BurpParser
            List result = p.parse()
            assertFalse(result.isEmpty())
            assert result[0] instanceof Finding
        }
    }
}