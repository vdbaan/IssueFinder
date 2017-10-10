package net.vdbaan.issuefinder.filter

import net.vdbaan.issuefinder.model.Finding
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertEquals

class PredicateParserTest {

    @Test
    void testParseComplex() {
        String txt = "(IP == \"127.0.0.1\") && ((!EXPLOITABLE) || ((PORT LIKE \"443\") && !(SERVICE LIKE http)) || (RISK BETWEEN (LOW,CRITICAL)))"
        FindingPredicateParser fpp = new FindingPredicateParser()
        FindingPredicate f = fpp.parse(txt)
        assertEquals('Wrong parsing', "(IP == '127.0.0.1') && (((!EXPLOITABLE) || ((PORT LIKE '443') && (!(SERVICE LIKE 'http')))) || (RISK BETWEEN '[LOW, CRITICAL]'))", f.toString())
        txt = "(IP == \"127.0.0.1\") && ((!EXPLOITABLE) || ((PORT LIKE \"443\") && !(SERVICE LIKE http)) || (RISK IN [LOW,CRITICAL,HIGH]))"
        fpp = new FindingPredicateParser()
        f = fpp.parse(txt)
        assertEquals('Wrong parsing', "(IP == '127.0.0.1') && (((!EXPLOITABLE) || ((PORT LIKE '443') && (!(SERVICE LIKE 'http')))) || (RISK IN '[LOW, CRITICAL, HIGH]'))", f.toString())

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
        boolean result = p.test(f)
        assertEquals('Should be false', false, p.test(f))
        p.right = '80'
        assertEquals('Should be false', true, p.test(f))
    }

    @Rule public ExpectedException thrown= ExpectedException.none();
    @Test
    void testError() {
        thrown.expect(FindingPredicateParserRuntimeException.class)
        String txt = "(IP == \"127.0.0.1"
        FindingPredicateParser fpp = new FindingPredicateParser()
        FindingPredicate f = fpp.parse(txt)
    }
}
