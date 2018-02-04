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

import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.parser.NessusParser
import org.junit.Test

import static org.junit.Assert.assertEquals

class FindingHtmlViewerTest {

    @Test
    void testNessusFinding() {
        Finding f = new Finding(scanner: NessusParser.scanner)
        String html = FindingHtmlViewer.asHtml(f)
        assertEquals("\n" +
                "<table border=1 width='100%' style='border-collapse:collapse;font-family:arial;'>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Scanner</b></td><td style='border:1px solid black;'>Nessus</td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Source</b></td><td style='border:1px solid black;'>:(/)</td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Hostname</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Service</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Plugin/Version</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Severity</b></td><td style='border:1px solid black;'>UNKNOWN</td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>CVSS Base Score</b></td><td style='border:1px solid black;'>0.0</td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>CVSS Base Vector</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'>&nbsp;</td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'><b>Summary:</b><br/></td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'><b>Description:</b><br/></td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'><b>Reference:</b><br/></td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'><b>Plugin Output:</b><br/><pre></pre></td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'><b>Solution:</b><br/></td></tr>\n" +
                "</table>\n", html)
    }

    @Test
    void testNoneNessusFinding() {
        Finding f = new Finding()
        String html = FindingHtmlViewer.asHtml(f)
        assertEquals("\n" +
                "<table border=1 width='100%' style='border-collapse:collapse;font-family:arial;'>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Scanner</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Source</b></td><td style='border:1px solid black;'>:(/)</td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Hostname</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Service</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Plugin/Version</b></td><td style='border:1px solid black;'></td></tr>\n" +
                "<tr><td width='15%' style='border:1px solid black;'><b>Severity</b></td><td style='border:1px solid black;'>UNKNOWN</td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'>&nbsp;</td></tr>\n" +
                "<tr><td colspan=2 style='border:1px solid black;'><b>Summary:</b><br/></td></tr>\n" +
                "</table>\n", html)
    }
}
