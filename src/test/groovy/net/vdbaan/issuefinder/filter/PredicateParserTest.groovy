/**
 * Copyright (C) 2017 S. van der Baan

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.vdbaan.issuefinder.filter

import net.vdbaan.issuefinder.model.Finding
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertEquals

class PredicateParserTest {

    @Test
    void testParseFormat() {
        String txt = "service like http"
        FindingPredicateParser fpp = new FindingPredicateParser()
        FindingPredicate f = fpp.parse(txt)
        assertEquals('Wrong parsing', "SERVICE LIKE '%http%'", f.toString())
        txt = "service !like http"
        f = fpp.parse(txt)
        assertEquals('Wrong parsing', "SERVICE NOT LIKE '%http%'", f.toString())
        txt = "exploitable"
        f = fpp.parse(txt)
        assertEquals('Wrong parsing', "EXPLOITABLE IS TRUE", f.toString())
        txt = "!exploitable"
        f = fpp.parse(txt)
        assertEquals('Wrong parsing', "EXPLOITABLE IS FALSE", f.toString())
    }

    @Test
    void testParseComplex() {
        String txt = "(IP == \"127.0.0.1\") && ((!EXPLOITABLE) || ((PORT LIKE \"443\") && (service !LIKE http)) || (RISK BETWEEN (low,  critical)))"
        FindingPredicateParser fpp = new FindingPredicateParser()
        FindingPredicate f = fpp.parse(txt)
        assertEquals('Wrong parsing', "(IP == '127.0.0.1') && (((EXPLOITABLE IS FALSE) || ((PORT LIKE '%443%') && (SERVICE NOT LIKE '%http%'))) || (RISK BETWEEN 'LOW' AND 'CRITICAL'))", f.toString())
        txt = "(IP == \"127.0.0.1\") && ((!EXPLOITABLE) || ((PORT LIKE \"443\") && (SERVICE !LIKE http)) || (RISK IN (LOW,CRITICAL,HIGH)))"
        fpp = new FindingPredicateParser()
        f = fpp.parse(txt)
        assertEquals('Wrong parsing', "(IP == '127.0.0.1') && (((EXPLOITABLE IS FALSE) || ((PORT LIKE '%443%') && (SERVICE NOT LIKE '%http%'))) || (RISK IN ('LOW', 'CRITICAL', 'HIGH')))", f.toString())

    }

    @Test
    void testParseSimple() {
        FindingPredicate p = new FindingPredicate(ColumnName.IP, FindingPredicate.LogicalOperation.GE, '100')
        String text = "IP >= '100'"
        FindingPredicateParser fpp = new FindingPredicateParser()
        assertEquals('Wrong interpretation', p.toString(), fpp.parse(text).toString())
    }

    @Test
    void testPredicate() {
        Finding f = new Finding(ip: 80)
        FindingPredicate p = new FindingPredicate(ColumnName.IP, FindingPredicate.LogicalOperation.GE, '100')
//        boolean result = p.test(f)
        assertEquals('Should be false', false, p.test(f))
        p.right = '80'
        assertEquals('Should be true', true, p.test(f))
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Test
    void testError() {
        thrown.expect(FindingPredicateParserRuntimeException.class)
        String txt = "(IP == \"127.0.0.1"
        FindingPredicateParser fpp = new FindingPredicateParser()
        FindingPredicate f = fpp.parse(txt)
        assert f == null
    }
}
