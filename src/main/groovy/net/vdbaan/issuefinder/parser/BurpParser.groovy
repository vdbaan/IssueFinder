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

class BurpParser extends Parser {
    static IDENTIFIER = "issues"
    static scanner = "Burp"

    BurpParser(content) {
        this.content = content
    }

    static boolean identify(contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }

    List<Finding> parse() {
        String version = content.@burpVersion
        if (version.compareTo("1.7.27") >= 0) {
            return parse1727()
        } else
            return parseOther()
    }

    List<Finding> parseOther() {
        List<Finding> result = new ArrayList<>()
        content.issue.each { issue ->
            // scanner, ip, port, service, plugin, severity, summary
            def url = new URL(issue.host as String)
            def ip = (issue.host.@ip ?: url.getHost()) as String
            def port = url.getPort() as String
            def service = url.protocol.toUpperCase()
            result << new Finding([scanner: scanner, ip: ip, port: port, portStatus: 'open', protocol: 'tcp', service: service,
                                   plugin : issue.name as String, severity: calc(issue.severity, issue.confidence),
                                   summary: buildSummary(issue)])

        }
        return result
    }

    class FindingWrapper {
        Finding finding
        Set<String> locations = new HashSet()

        String toString() {
            StringBuilder sb = new StringBuilder()
            sb.append(finding.fullDescription())
            sb.append(locations.join(', '))
            return sb.toString()
        }
    }

    List<Finding> parse1727() {
        List<Finding> result = new ArrayList<>()
        Map<String, Map> base = new HashMap<>()

        content.issue.each { issue ->
            // scanner, ip, port, service, plugin, severity, summary
            def url = new URL(issue.host as String)
            def ip = (issue.host.@ip ?: url.getHost()) as String
            def port = url.getPort() as String

            def service = url.protocol.toUpperCase()
            def plugin = String.format("(%s) %s", issue.type, issue.name)
            if (port == '-1') port = (service == 'HTTPS') ? '443' : '80'
            String location = issue.location
            def f = new Finding([scanner: scanner, ip: ip, port: port, portStatus: 'open', protocol: 'tcp', service: service,
                                 plugin : plugin, severity: calc(issue.severity, issue.confidence),
                                 summary: buildSummary1727(issue)])

            def key = ip + ':' + port

            Map<String, FindingWrapper> temp
            if (base.containsKey(key)) {
                temp = base.get(key)
            } else {
                temp = new HashMap<>()
            }
            FindingWrapper fw
            if (temp.containsKey(plugin)) {
                fw = temp.get(plugin)
            } else {
                fw = new FindingWrapper(finding: f)
            }
            fw.locations.add(location)
            temp.put(plugin, fw)
            base.put(key, temp)
        }
        base.each { url, map ->
            map.each { plugin, wrap ->
                Finding find = wrap.finding
                find.summary = find.summary.replace('PLACEHOLDER', '\n' + wrap.locations.join('\n'))
                result << find
            }
        }
        return result
    }

    private Finding.Severity calc(severity, confidence) {
        switch (severity) {
            case 'High': return Finding.Severity.HIGH
            case 'Medium': return Finding.Severity.MEDIUM
            case 'Low': return Finding.Severity.LOW
            case 'Information': return Finding.Severity.INFO
            default: return Finding.Severity.UNKNOWN
        }
    }

    private String buildSummary(issue) {
        String summary = "Name   : " + issue.name
        summary += "\nDetail     : " + issue.issueDetail
        summary += "\npath       : " + issue.path
        summary += "\nlocation   : " + issue.location
        summary += "\nSeverity   : " + issue.severity + " (" + issue.confidence + ")"
        summary += "\nBackground : " + issue.issueBackground
        summary += "\nRemediation: " + issue.remediationDetail

        return summary
    }

    private String buildSummary1727(issue) {
        String summary = "Name   : " + issue.name
        summary += "\nDetail     : " + issue.issueDetail
        summary += "\npath       : PLACEHOLDER"
        summary += "\nlocation   : PLACEHOLDER"
        summary += "\nSeverity   : " + issue.severity + " (" + issue.confidence + ")"
        summary += "\nBackground : " + issue.issueBackground
        summary += "\nRemediation: " + issue.remediationDetail

        return summary
    }

    // TODO implement (when I remember what I meant here ;) )
    List<Finding> mergeFindings(List<Finding> result) {

        result.each {

        }
        return result
    }
}
