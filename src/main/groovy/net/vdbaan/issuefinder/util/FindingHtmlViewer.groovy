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
<table border=1 style='border-collapse:collapse;font-family:arial;'>
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
<table border=1 style='border-collapse:collapse;'>
<tr><td width='15%' style='border:1px solid black;'><b>Scanner</b></td><td style='border:1px solid black;'>${finding.scanner}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Source</b></td><td style='border:1px solid black;'>${finding.ip}:${finding.port}(${finding.protocol}/${finding.portStatus})</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Hostname</b></td><td style='border:1px solid black;'>${finding.hostName}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Service</b></td><td style='border:1px solid black;'>${finding.service}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Plugin/Version</b></td><td style='border:1px solid black;'>${finding.plugin}</td></tr>
<tr><td width='15%' style='border:1px solid black;'><b>Severity</b></td><td style='border:1px solid black;${getSeverityStyle(finding)}'>${finding.severity}</td></tr>
<tr><td colspan=2 style='border:1px solid black;'>&nbsp;</td></tr>
<tr><td colspan=2 style='border:1px solid black;'><b>Summary:</b><br/>${finding.summary}</td></tr>
</table>
"""
    }

    static String getSeverityStyle(Finding finding) {
        switch (finding.severity) {
            case Finding.Severity.CRITICAL: return 'background:#fec7c6'
            case Finding.Severity.HIGH: return 'background:#ffdebd'
            case Finding.Severity.MEDIUM: return 'background:#ffffa3'
            case Finding.Severity.LOW: return 'background:#d5eaff'
        }
    }
}
