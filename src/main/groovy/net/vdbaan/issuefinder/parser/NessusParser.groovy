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

@Log
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

                String cvssval = item.cvss_base_score ?: '0.0'
                if (cvssval == '') cvssval = '0.0'
                BigDecimal cvss = new BigDecimal(cvssval)
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


                String description = item.description
                String solution = item.solution
                String pluginOutput = item.plugin_output
                String cvssVector = item.cvss_vector

                StringBuilder references = new StringBuilder()
                references << "CVE references  : ${(item.cve.collect { it }).join(", ")}\n"
                references << "BID references  : ${(item.bid.collect { it }).join(", ")}\n"
                references << "Other references: ${(item.xref.collect { it }).join(", ")}\n"

                if (allowed(severity))
                    result << new Finding([scanner     : scanner, ip: hostIp, port: portnr, portStatus: 'open', protocol: protocol, hostName: hostName,
                                           service     : service, plugin: plugin + ":" + pluginName,
                                           exploitable : item.exploit_available == 'true', baseCVSS: cvss, cvssVector: cvssVector,
                                           severity    : severity, summary: buildSummary(item), description: description, reference: references,
                                           pluginOutput: pluginOutput, solution: solution])
            }
        }
        log.info(String.format("Returning %d issues", result.size()))
        return result
    }

    String buildSummary(item) {
        return """
${item.synopsis}
RiskFactor           : ${item.risk_factor}
Exploit available    : ${item.exploit_available}
Ease of exploit      : ${item.exploitability_ease}
Patch availabls since: ${item.patch_publication_date}
CVSS temporal vector : ${item.cvss_temporal_vector}
CVSS temporal score  : ${item.cvss_temporal_score}
"""
    }
}


