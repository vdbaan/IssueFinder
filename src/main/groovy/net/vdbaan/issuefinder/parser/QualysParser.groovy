/*
 *  Copyright (C) 2019  S. van der Baan
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

package net.vdbaan.issuefinder.parser

import net.vdbaan.issuefinder.model.Finding

/**
 * @author S. van der Baan (@vdbaan)
 */
class QualysParser extends Parser {
    static String IDENTIFIER = "SCAN"
    static String scanner = "Qualys"

    QualysParser(final content) {
        this.content = content
    }

    static boolean identify(final contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }

    @Override
    List<Finding> parse() {
        final List<Finding> result = new ArrayList<>()
        content.IP.each { final host ->
            final def hostIp = host.@value
            final def hostname = host.@name
            // INFOS, SERVICES, VULNS, PRACTICES
            host.INFOS.CAT.each { final cat ->
                final def catname = cat.@value
                final def port = cat.@port ?: '0'
                final def protocol = cat.@protocol ?: ''
                cat.INFO.each { final vuln ->
                    result << buildFinding(catname, hostIp, hostname, port, protocol, vuln)
                }
            }
            host.SERVICES.CAT.each { final cat ->
                final def catname = cat.@value
                final def port = cat.@port ?: '0'
                final def protocol = cat.@protocol ?: ''
                cat.SERVICE.each { final vuln ->
                    result << buildFinding(catname, hostIp, hostname, port, protocol, vuln)
                }
            }
            host.VULNS.CAT.each { final cat ->
                final def catname = cat.@value
                final def port = cat.@port ?: '0'
                final def protocol = cat.@protocol ?: ''
                cat.VULN.each { final vuln ->
                    result << buildFinding(catname, hostIp, hostname, port, protocol, vuln)
                }
            }
            host.PRACTICES.CAT.each { final cat ->
                final def catname = cat.@value
                final def port = cat.@port ?: '0'
                final def protocol = cat.@protocol ?: ''
                cat.PRACTICE.each { final vuln ->
                    result << buildFinding(catname, hostIp, hostname, port, protocol, vuln)
                }
            }
        }
        return result
    }

    private Finding buildFinding(catname, ip, hostName, port, protocol, node) {
        final def pluginName = catname.toString() + ' (' + node.@number.toString() + '): ' + node.TITLE.toString()
        String risk = node.@severity
        Finding.Severity severity = Finding.Severity.UNKNOWN
        switch (risk.toInteger()) {
            case 1: severity = Finding.Severity.INFO
                break
            case 2: severity = Finding.Severity.LOW
                break
            case 3: severity = Finding.Severity.MEDIUM
                break
            case 4: severity = Finding.Severity.HIGH
                break
            case 5: severity = Finding.Severity.CRITICAL
                break
        }
        def exploitable = node.CORRELATION != ""

        def reference = buildReference(node)
        // summary == rest
        def summary = buildSummary(node)
        return new Finding([scanner   : scanner, ip: ip, hostName: hostName, port: port, protocol: protocol, service:catname,
                            portStatus: 'open', plugin: pluginName, description: node.DIAGNOSIS.toString(),
                            solution  : node.SOLUTION.toString(), severity: severity, exploitable: exploitable,
                            reference : reference, pluginOutput:node.RESULT, summary:summary
        ])
    }

    private String buildReference(node) {
        StringBuilder references = new StringBuilder()

        references << "CVE references  : ${(node.CVE_ID_LIST.CVE_ID.collect { it.ID }).join(", ")}\n"
        references << "BID references  : ${(node.BUGTRAQ_ID_LIST.BUGTRAQ_ID.collect { it.ID }).join(", ")}\n"

        return references.toString()
    }

    private String buildSummary(node) {
        StringBuilder summary = new StringBuilder()
        summary << """
CONSEQUENCE: ${node.CONSEQUENCE}
LAST_UPDATE: ${node.LAST_UPDATE}
PCI_FLAG   : ${node.PCI_FLAG}

"""
        node.CORRELATION.EXPLOITABILITY.EXPLT_SRC.each {final src ->
            summary << src.SRC_NAME
            summary.append("\n")
            src.EXPLT_LIST.EXPLT.each { expl ->
                summary << """[${expl.REF}] ${expl.DESC} (${expl.LINK})
"""
            }
            summary.append("\n")
        }
        return summary.toString()
    }
}
