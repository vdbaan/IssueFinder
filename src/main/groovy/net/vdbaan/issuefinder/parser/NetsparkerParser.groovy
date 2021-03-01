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

import groovy.util.logging.Log
import net.vdbaan.issuefinder.model.Finding

import java.util.logging.Level

@Log
class NetsparkerParser extends Parser {
    static String IDENTIFIER = "netsparker"
    static String scanner = "Netsparker"

    NetsparkerParser(final content) {
        this.content = content
    }

    static boolean identify(final contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }


    List<Finding> parse() {
        final List<Finding> result = new ArrayList<>()
        content.target.each { final scan ->
            final String summary = "Target URL: " + scan.url + '\n' + 'Scan time : ' + scan.scantime + '\n'
            result << new Finding([scanner : scanner, ip: 'none', port: 'generic', service: 'none', plugin: 'scaninfo',
                                   severity: Finding.Severity.INFO, summary: summary])
        }

        content.vulnerability.each { final vuln ->
            final def url = new URL(vuln.url.toString())
            int port = url.port
            final String service = url.protocol.toUpperCase()
            if (port == -1) {
                port = service == 'HTTPS' ? 443 : 80
            }
            final String plugin = vuln.type
            final Finding.Severity severity = calcSeverity('' + vuln.severity)
            String summary = 'URL: ' + vuln.url + '\n'
            summary += 'Severity:' + vuln.severity + ' (' + vuln.certainty + '%)\n'
            summary += 'Classification:' + classification(vuln.classification) + '\n'
            summary += 'Extra Information (' + vuln.extrainformation.info.@name + '):\n'
            summary += vuln.extrainformation.info + '\n'

            result << new Finding([scanner : scanner, ip: url.host, port: '' + port, service: service, plugin: plugin,
                                   severity: severity, summary: summary])

        }
        return result
    }

    private Finding.Severity calcSeverity(final String risk) {
        Finding.Severity result
        try {
            result = Finding.Severity.valueOf(Finding.Severity, risk.toUpperCase())
        } catch (final IllegalArgumentException e) {
            log.log(Level.FINE, 'Got an exception', e)
            result = Finding.Severity.INFO
        }
        return result
    }

    private String classification(final xml) {
        final def sb = new StringBuilder()
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
