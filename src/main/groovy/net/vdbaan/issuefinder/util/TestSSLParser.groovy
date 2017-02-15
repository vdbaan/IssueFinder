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

class TestSSLParser extends Parser {
    static String scanner = "TestSSL"

    TestSSLParser(content) {
        this.content = jsonSlurper.parseText(content)
    }

    static boolean identify(contents) {
        try {
            def json = jsonSlurper.parseText(contents)
            // test if there is an entry with the following keys:
            // id,ip,port,severity,finding
            if(json instanceof List) {
                if (json[0] instanceof Map) {
                    Map m = json[0]
                    if (m.size() == 5 && m.containsKey('finding') && m.containsKey('ip')
                            && m.containsKey('port') && m.containsKey('severity') && m.containsKey('finding'))
                        return true
                }
            }
            return false
        } catch(Exception e) {
            return false
        }
    }

    @Override
    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        result.add(buildClients())
        result.add(buildCiphers())
        result.add(order())
        content.each { issue ->
            if (!issue.id.startsWith('order') && !issue.id.startsWith('client_') && !issue.id.startsWith('cipher_')) {
                println issue
                def f = new Finding(scanner, issue.ip, issue.port, "none", issue.id, calcRisk(issue.severity), issue.finding)
                println f
                result += f
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

        }
    }

    private Finding order() {
        String summary = "Cipher orders"
        String ip,port
        content.each {issue ->
            if(issue.id.startsWith('order')) {
                summary += "\n"
                summary += issue.finding
                ip = issue.ip
                port = issue.port
            }
        }
        return new Finding(scanner, ip, "generic/SSL"  , "none", "TestSSL Cipher order info", Finding.Severity.INFO, summary)
    }

    private Finding buildClients() {
        String summary = "Browser simulations"
        String ip,port
        content.each {issue ->
            if(issue.id.startsWith('client_')) {
                summary += "\n"
                summary += issue.finding
                ip = issue.ip
                port = issue.port
            }
        }
        return new Finding(scanner, ip, "generic/SSL"  , "none", "TestSSL Client info", Finding.Severity.INFO, summary)
    }

    private Finding buildCiphers() {
        String summary = "Supported Ciphers"
        String ip,port
        content.each {issue ->
            if(issue.id.startsWith('cipher_')) {
                summary += "\n"
                summary += issue.finding
                ip = issue.ip
                port = issue.port
            }
        }
        return new Finding(scanner, ip, "generic/SSL"  , "none", "TestSSL Cipher info", Finding.Severity.INFO, summary)
    }
}
