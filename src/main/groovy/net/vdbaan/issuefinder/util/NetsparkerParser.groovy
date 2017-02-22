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


class NetsparkerParser extends Parser {
    static String IDENTIFIER = "netsparker"
    static String scanner = "Netsparker"

    NetsparkerParser(content) {
        this.content = xmlslurper.parseText(content)
    }

    static boolean identify(contents) {
        try {
            def xml = xmlslurper.parseText(contents)
            return IDENTIFIER.equalsIgnoreCase(xml.name())
        } catch(Exception e) {
            return false
        }
    }


    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        content.target.each { scan ->
            String summary = "Target URL: " + scan.url + '\n' + 'Scan time : ' + scan.scantime + '\n'
            result << new Finding(scanner, 'none', 'generic', 'none', 'scaninfo', Finding.Severity.INFO, summary)
        }

        println  content.target.url
        def url = new URL(content.target.url.toString())
        int port = url.port
        String service = url.protocol.toUpperCase()
        if (port == -1) {
            port = service == 'HTTPS'?443:80
        }
        content.vulnerability.each { vuln ->
            String plugin = vuln.type
            Finding.Severity severity = calcSeverity(''+vuln.severity)
            String summary = 'URL: ' + vuln.url + '\n'
            summary += 'Severity:' + vuln.severity + ' (' + vuln.certainty + '%)'
            summary += 'Extra Information (' + vuln.extrainformation.info.@name + '):'
            summary += vuln.extrainformation.info + '\n'
            summary += 'Classification:\n' + classification(vuln.classification)
            result << new Finding(scanner, url.host, '' + port, service, plugin, severity, summary)

        }
        return result
    }

    private Finding.Severity calcSeverity(String risk) {
        Finding.Severity result
        try {
            result = Finding.Severity.valueOf(Finding.Severity, risk.toUpperCase())
        }catch(IllegalArgumentException e) {
            result = Finding.Severity.INFO
        }
        return result
    }

    private String classification(xml) {
        def sb = new StringBuilder()
        sb << "OWASP:" + xml.OWASP
        sb << " ,WASC:" + xml.WASC
        sb << " ,CWE:" + xml.CWE
        sb << " ,CAPEC:" + xml.CAPEC
        sb << " ,PCI:" + xml.PCI
        sb << " ,PCI2:" + xml.PCI2
        sb << " ,PCI3:" + xml.PCI3
        sb << "\n"
        return sb.toString()
    }
}
