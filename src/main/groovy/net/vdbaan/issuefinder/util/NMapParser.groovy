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

class NMapParser extends Parser{
    static String IDENTIFIER = "nmaprun"
    static String scanner = "NMap"

    NMapParser(content) {
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
        content.host.each { host ->
            def hostNode = host.address.find { it.@addrtype == 'ipv4' }
            String hostIp = hostNode.@addr
            String protocol = content.scaninfo.@protocol
            result += scanInfo(content, hostIp, protocol)
            result += runStats(content, hostIp, protocol)
            result += summary(content, hostIp, protocol)
            host.ports.port.each { port ->
                String portnr = port.@portid
                protocol = port.@protocol
                String state = port.state.@state
                String service = port.service.@name
                String product = port.service.@product
                String summary = ""
                result << new Finding([scanner:scanner, ip:hostIp, port:portnr + "/" + state + "/" + protocol,
                                       service:service + " (" + product + ")", plugin:"NMap port information",
                                       severity:Finding.Severity.INFO, summary:summary])
            }
        }
        return result
    }

    private Finding scanInfo(xml, String ip, String protocol) {
        String summary = "Protocol       : " + protocol
        summary += "\nNumber of ports: " + xml.scaninfo.@numservices
        summary += "\nPorts scanned  : " + xml.scaninfo.@services
        summary += "\nScan type      : " + xml.scaninfo.@type
        summary += "\nNmap command   : " + xml.@args

        return new Finding([scanner:scanner, ip:ip, port:"generic/" + protocol, service:"none",
                            plugin:"NMap scan info", severity:Finding.Severity.INFO, summary:summary])
    }

    private Finding runStats(xml, String ip, String protocol) {
        String summary = "Number of hosts"
        summary += "\nScanned: " + xml.runstats.hosts.@total
        summary += "\nUp     : " + xml.runstats.hosts.@up
        summary += "\nDown   : " + xml.runstats.hosts.@down

        return new Finding([scanner:scanner, ip:ip, port:"generic/" + protocol, service:"none",
                            plugin:"NMap stats", severity:Finding.Severity.INFO, summary:summary])
    }

    private Finding summary(xml, String ip, String protocol) {
        String summary = xml.runstats.finished.@summary ?:
                "Scan Execution Stats" +
                        "\nCompleted: " + xml.runstats.finished.@timestr +
                        "\nDuration : " + xml.runstats.finished.@elapsed +
                        " seconds"

        return new Finding([scanner:scanner, ip:ip, port:"generic/" + protocol, service:"none",
                            plugin:"NMap summary", severity:Finding.Severity.INFO, summary:summary])
    }
}
