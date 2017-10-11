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
class TestSSLParser extends Parser {
    static String scanner = "TestSSL"

    TestSSLParser(content) {
        this.content = content
    }

    static boolean identify(contents) {
        try {
            if (contents instanceof List) {
                if (contents[0] instanceof Map) {
                    Map m = contents[0]
                    if (m.size() == 5 && m.containsKey('finding') && m.containsKey('ip')
                            && m.containsKey('port') && m.containsKey('severity') && m.containsKey('finding'))
                        return true
                }
            }
            return false
        } catch (Exception e) {
            log.log(Level.FINE,'Got an exception',e)
            return false
        }
    }

    @Override
    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        boolean doClients = false, doCiphers = false, doOrder = false
        content.each { issue ->
            if (issue.id.startsWith('order')) doOrder = true
            if (issue.id.startsWith('client_')) doClients = true
            if (issue.id.startsWith('cipher_')) doCiphers = true
        }
        if (doClients && allowed(Finding.Severity.INFO))
            result.add(buildClients())
        if (doCiphers && allowed(Finding.Severity.INFO))
            result.add(buildCiphers())
        if (doOrder && allowed(Finding.Severity.INFO))
            result.add(order())
        content.each { issue ->
            if (!issue.id.startsWith('order') && !issue.id.startsWith('client_') && !issue.id.startsWith('cipher_')) {
                if (allowed(calcRisk(issue.severity)))
                    result << new Finding([scanner : scanner, ip: issue.ip, port: issue.port, portStatus: 'open', protocol: 'tcp', service: "none", plugin: issue.id,
                                           severity: calcRisk(issue.severity), summary: issue.finding])
            }
        }
        return result
    }

    private Finding.Severity calcRisk(String severity) {
        switch (severity.toLowerCase()) {

            case "high": return Finding.Severity.HIGH
            case "medium":
            case "warn": return Finding.Severity.MEDIUM
            case "not ok":
            case "minor":
            case "minor_error":
            case "low": return Finding.Severity.LOW
            case "ok":
            case "info": return Finding.Severity.INFO
            default: return Finding.Severity.UNKNOWN
        }
    }

    private Finding order() {
        String summary = "Cipher orders"
        String ip, port
        content.each { issue ->
            if (issue.id.startsWith('order')) {
                summary += "\n"
                summary += issue.finding
                ip = issue.ip.split('/')[1]
                port = issue.port
            }
        }
        return new Finding([scanner: scanner, ip: ip, port: "generic/SSL", service: "none",
                            plugin : "TestSSL Cipher order info", severity: Finding.Severity.INFO, summary: summary])
    }

    private Finding buildClients() {
        String summary = "Browser simulations"
        String ip, port
        content.each { issue ->
            if (issue.id.startsWith('client_')) {
                summary += "\n"
                summary += issue.finding
                ip = issue.ip
                port = issue.port
            }
        }
        return new Finding([scanner: scanner, ip: ip, port: "generic/SSL", service: "none",
                            plugin : "TestSSL Client info", severity: Finding.Severity.INFO, summary: summary])
    }

    private Finding buildCiphers() {
        String summary = "Supported Ciphers"
        String ip, port
        content.each { issue ->
            if (issue.id.startsWith('cipher_')) {
                summary += "\n"
                summary += issue.finding
                ip = issue.ip
                port = issue.port
            }
        }
        return new Finding([scanner: scanner, ip: ip, port: "generic/SSL", service: "none",
                            plugin : "TestSSL supported Ciphers", severity: Finding.Severity.INFO, summary: summary])
    }
}
