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

import net.vdbaan.issuefinder.model.Finding

class NMapParser extends Parser {
    static String IDENTIFIER = "nmaprun"
    static String scanner = "NMap"

    NMapParser(final content) {
        this.content = content
    }

    static boolean identify(final contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }

    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        content.host.each { final host ->
            final def hostNode = host.address.find { it.@addrtype == 'ipv4' }
            final String hostIp = hostNode.@addr
            String hostName
            host.hostnames.hostname.each { final hostname ->
                if (hostname.@type == 'user') {
                    hostName = hostname.@name
                } else if (hostname.@type == 'PTR') {
                    hostName = hostname.@name
                } else {
                    hostName = hostIp
                }

            }
            hostName = hostName ?: hostIp
            String protocol = content.scaninfo.@protocol
            if (allowed(Finding.Severity.INFO))
                result += scanInfo(content, hostIp, hostName, protocol)
            if (allowed(Finding.Severity.INFO))
                result += runStats(content, hostIp, hostName, protocol)
            if (allowed(Finding.Severity.INFO))
                result += summary(content, hostIp, hostName, protocol)
            if (host.ports?.extraports?.@state == 'closed') {
                final String summary = "Amount of closed ports: " + host.ports.extraports.@count

                result << new Finding(([scanner: scanner, ip: hostIp, port: 0, portStatus: 'closed', protocol: protocol, service: "none", hostName: hostName,
                                        plugin : "NMap closed ports", severity: Finding.Severity.MEDIUM, summary: summary]))
            }
            host.ports.port.each { final port ->
                final String portnr = port.@portid
                protocol = port.@protocol
                final String state = port.state.@state
                final String service = port.service.@name
                String product = port.service.@product
                if (product != "") product = ' (' + product + ')'
                String summary = ""

                port.script.each { final script ->
                    summary += 'script ('
                    summary += script.@id
                    summary += '):\n'
                    summary += script.@output
                    summary += '\n'
                    summary += script
                    summary += '\n\n'
                }
                if (allowed(Finding.Severity.INFO))
                    result << new Finding([scanner : scanner, ip: hostIp, port: portnr, portStatus: state, protocol: protocol, hostName: hostName,
                                           service : service + product, plugin: "NMap port (" + portnr + ") information",
                                           severity: (state == 'closed') ? Finding.Severity.LOW : Finding.Severity.INFO, summary: summary])
            }
        }
        return result
    }

    private Finding scanInfo(final xml, final String ip, final String hostName, final String protocol) {
        String summary = "Protocol       : " + protocol
        summary += "\nNumber of ports: " + xml.scaninfo.@numservices
        summary += "\nPorts scanned  : " + xml.scaninfo.@services
        summary += "\nScan type      : " + xml.scaninfo.@type
        summary += "\nNmap command   : " + xml.@args

        return new Finding([scanner: scanner, ip: ip, port: 0, portStatus: 'NA', protocol: protocol, service: "none", hostName: hostName,
                            plugin : "NMap scan info", severity: Finding.Severity.INFO, summary: summary])
    }

    private Finding runStats(final xml, final String ip, final String hostName, final String protocol) {
        String summary = "Number of hosts"
        summary += "\nScanned: " + xml.runstats.hosts.@total
        summary += "\nUp     : " + xml.runstats.hosts.@up
        summary += "\nDown   : " + xml.runstats.hosts.@down

        return new Finding([scanner: scanner, ip: ip, port: 0, portStatus: 'NA', protocol: protocol, service: "none", hostName: hostName,
                            plugin : "NMap stats", severity: Finding.Severity.INFO, summary: summary])
    }

    private Finding summary(final xml, final String ip, final String hostName, final String protocol) {
        final String summary = xml.runstats.finished.@summary ?:
                "Scan Execution Stats" +
                        "\nCompleted: " + xml.runstats.finished.@timestr +
                        "\nDuration : " + xml.runstats.finished.@elapsed +
                        " seconds"

        return new Finding([scanner: scanner, ip: ip, port: 0, portStatus: 'NA', protocol: protocol, service: "none", hostName: hostName,
                            plugin : "NMap summary", severity: Finding.Severity.INFO, summary: summary])
    }
}
