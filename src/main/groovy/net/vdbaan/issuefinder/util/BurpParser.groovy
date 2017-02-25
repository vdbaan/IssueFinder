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

class BurpParser extends Parser{
    static IDENTIFIER = "issues"
    static scanner = "Burp"

    BurpParser(content) {
        this.content = xmlslurper.parseText(content)
    }

    static boolean identify(contents) {
        try {
            def xml = xmlslurper.parseText(contents)
            return IDENTIFIER.equalsIgnoreCase(xml.name()) && (xml.@'burpVersion' != null)
        } catch(Exception e) {
            return false
        }
    }

    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        content.issue.each {issue ->
            // scanner, ip, port, service, plugin, severity, summary
            def url = new URL(issue.host as String)
            def ip = (issue.host.@ip?:url.getHost()) as String
            def port = url.getPort() as String
            def service = url.protocol.toUpperCase()
            result << new Finding([scanner:scanner, ip:ip, port:port, service:service,
                                   plugin:issue.name as String, severity:calc(issue.severity, issue.confidence),
                                   summary:buildSummary(issue)])
        }
        return result
    }

    private Finding.Severity calc(severity,confidence) {
        switch (severity) {
            case 'High':return Finding.Severity.HIGH
            case 'Medium':return Finding.Severity.MEDIUM
            case 'Low':return Finding.Severity.LOW
            case 'Information': return Finding.Severity.INFO
            default: return Finding.Severity.UNKNOWN
        }
    }
    private String buildSummary(issue) {
        String summary = "Name   : " + issue.name
        summary += "\nDetail     : " + issue.issueDetail
        summary += "\npath       : " + issue.path
        summary += "\nlocation   : " + issue.location
        summary += "\nSeverity   : " + issue.severity+ " (" + issue.confidence + ")"
        summary += "\nBackground : " + issue.issueBackground
        summary += "\nRemediation: " + issue.remediationDetail

        return summary
    }
}
