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
package net.vdbaan.issuefinder.util

import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.parser.NessusParser

class FindingHtmlViewer {
    static String asHtml(Finding finding) {
        if(finding.scanner.equalsIgnoreCase(NessusParser.scanner)) {
            return nessusHTMLDescription(finding)
        } else
            return otherHTMLDescription(finding)
    }

    static String nessusHTMLDescription(Finding finding) {
        return """
<table border=1 width='100%' style='border-collapse:collapse;font-family:arial;'>
<tr><td width='15%' style='border:1px solid black;'><b>Scanner</b></td><td style='border:1px solid black;'>${finding.scanner}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Source</b></td><td style='border:1px solid black;'>${finding.ip}:${finding.port}(${finding.protocol}/${finding.portStatus})</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Hostname</b></td><td style='border:1px solid black;'>${finding.hostName}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Service</b></td><td style='border:1px solid black;'>${finding.service}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Plugin/Version</b></td><td style='border:1px solid black;'>${finding.plugin}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Severity</b></td><td style='border:1px solid black;${getSeverityStyle(finding)}'>${finding.severity}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>CVSS Base Score</b></td><td style='border:1px solid black;'>${finding.baseCVSS}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>CVSS Base Vector</b></td><td style='border:1px solid black;'>${finding.cvssVector}</td></tr>
<tr><td colspan=2 style='border:1px solid black;'>&nbsp;</td></tr>
<tr><td colspan=2 style='border:1px solid black;'><b>Summary:</b><br/>${finding.summary.replace('\n','<br/>')}</td></tr>
<tr><td colspan=2 style='border:1px solid black;'><b>Description:</b><br/>${finding.description}</td></tr>
<tr><td colspan=2 style='border:1px solid black;'><b>Reference:</b><br/>${finding.reference.replace('\n','<br/>')}</td></tr>
<tr><td colspan=2 style='border:1px solid black;'><b>Plugin Output:</b><br/><pre>${finding.pluginOutput}</pre></td></tr>
<tr><td colspan=2 style='border:1px solid black;'><b>Solution:</b><br/>${finding.solution}</td></tr>
</table>
"""
    }

    static String otherHTMLDescription(Finding finding) {
        return """
<table border=1 width='100%' style='border-collapse:collapse;font-family:arial;'>
<tr><td width='15%' style='border:1px solid black;'><b>Scanner</b></td><td style='border:1px solid black;'>${finding.scanner}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Source</b></td><td style='border:1px solid black;'>${finding.ip}:${finding.port}(${finding.protocol}/${finding.portStatus})</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Hostname</b></td><td style='border:1px solid black;'>${finding.hostName}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Service</b></td><td style='border:1px solid black;'>${finding.service}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Plugin/Version</b></td><td style='border:1px solid black;'>${finding.plugin}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Severity</b></td><td style='border:1px solid black;${getSeverityStyle(finding)}'>${finding.severity}</td></tr>
<tr><td colspan=2 style='border:1px solid black;'>&nbsp;</td></tr>
<tr><td colspan=2 style='border:1px solid black;'><b>Summary:</b><br/>${finding.summary.replace('\n','<br/>')}</td></tr>
</table>
"""
    }

    static String getSeverityStyle(Finding finding) {
        switch (finding.severity) {
            case Finding.Severity.CRITICAL: return 'background:#fec7c6'
            case Finding.Severity.HIGH: return 'background:#ffdebd'
            case Finding.Severity.MEDIUM: return 'background:#ffffa3'
            case Finding.Severity.LOW: return 'background:#d5eaff'
            default: return ''
        }
    }
}
