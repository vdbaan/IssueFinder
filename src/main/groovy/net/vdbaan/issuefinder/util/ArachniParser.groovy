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

class ArachniParser  extends Parser {

    static String IDENTIFIER = "report"
    static String scanner = "Arachni"

    ArachniParser(content) {
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
        String url =  content.sitemap.entry.@url
        URL host = new URL(url)
        def ip = host.getHost()
        def port = host.getPort()
        String service = host.protocol.toUpperCase()
        if (port == -1) {
            port = service == 'HTTPS'?443:80
        }
        content.issues.issue.each { issue ->
            // scanner, ip, port, service, plugin, severity, summary
            result << new Finding([scanner:scanner, ip:ip, port:port,
                                   service:service, plugin:issue.check.name + " v"+issue.check.version,
                                   severity:calc(issue.severity), summary:buildSummary(issue)])
        }
        return result
    }

    private Finding.Severity calc(severity) {
//        CRITICAL, HIGH, MEDIUM, LOW, INFO, UNKNOWN
        switch(severity) {
            case 'high': return Finding.Severity.HIGH
            case 'medium': return Finding.Severity.MEDIUM
            case 'low': return Finding.Severity.LOW
            case 'informational':return Finding.Severity.INFO
            default: return Finding.Severity.UNKNOWN
        }
    }

    private String buildSummary(issue) {
        String summary = "Name: " + issue.name
        summary += "\nDescription:\n "+ issue.description
        summary += "\nRemedy:\n" + issue.remedy_guidance
        if(issue.cwe)
            summary += "\ncwe: "+issue.cwe
        summary += "\nReferences:"
        issue?.references.reference.each{ ref ->
            summary += "- "+ ref.url
        }
        return summary
    }
}
