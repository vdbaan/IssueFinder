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

class NessusParser extends Parser {
    static String IDENTIFIER = "NessusClientData_v2"
    static String scanner = "Nessus"

    NessusParser(content) {
        this.content = content
    }

    static boolean identify(contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }


    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        content.Report.ReportHost.each { host ->

            def IPTag = host.HostProperties.tag.find { it.@name == 'host-ip' }
            String hostIp = IPTag
            def FQDNTag = host.HostProperties.tag.find { it.@name == 'host-fqdn' }
            String hostName = FQDNTag ?: hostIp

            for (item in host.ReportItem) {
                String portnr = item.@port
                String protocol = item.@protocol
                String plugin = item.@pluginID
                String pluginName = item.@pluginName
                String service = item.@svc_name
                String risk = item.@severity
                Finding.Severity severity = Finding.Severity.UNKNOWN
                switch (risk.toInteger()) {
                    case 0: severity = Finding.Severity.INFO
                        break
                    case 1: severity = Finding.Severity.LOW
                        break
                    case 2: severity = Finding.Severity.MEDIUM
                        break
                    case 3: severity = Finding.Severity.HIGH
                        break
                    case 4: severity = Finding.Severity.CRITICAL
                        break
                }
                StringBuilder summary = new StringBuilder()
                summary << item.synopsis
                summary << "Plugin output:\n$item.plugin_output\n"
                summary << "Description          : $item.description\n"
                summary << "Solution             : $item.solution\n"
                summary << "RiskFactor           : $item.risk_factor\n"
                summary << "Exploit available    : $item.exploit_available\n"
                summary << "Ease of exploit      : $item.exploitability_ease\n"
                summary << "Patch available since: $item.patch_publication_date\n"
                summary << "CVSS base vector     : $item.cvss_vector\n"
                summary << "CVSS base score      : $item.cvss_base_score\n"
                summary << "CVSS temporal vector : $item.cvss_temporal_vector\n"
                summary << "CVSS temporal score  : $item.cvss_temporal_score\n"
                summary << "See also:\n$item.see_also\n"

                summary << "CVE references  : ${(item.cve.collect { it }).join(", ")}\n"
                summary << "BID references  : ${(item.bid.collect { it }).join(", ")}\n"
                summary << "Other references: ${(item.xref.collect { it }).join(", ")}\n"

                result << new Finding([scanner    : scanner, ip: hostIp, port: portnr, portStatus: 'open', protocol: protocol, hostName: hostName,
                                       service    : service, plugin: plugin + ":" + pluginName,
                                       exploitable: item.exploit_available == 'true', baseCVSS: item.cvss_base_score ?: '0.0',
                                       severity   : severity, summary: summary.toString()])
            }
        }
        return result
    }

    def convertToMap(nodes) {
        nodes.children().collectEntries {
            [it.name(), it.childNodes() ? convertToMap(it) : it.text()]
        }
    }
}


