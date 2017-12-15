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

import groovy.util.slurpersupport.NodeChild
import groovy.util.slurpersupport.NodeChildren
import net.vdbaan.issuefinder.model.Finding

class NexposeParser extends Parser {
    static String IDENTIFIER = "NexposeReport"
    static String scanner = "Nexpose"

    static void main(String... args) {
        File f = new File("testdata/nexpose/report-v2.xml")
        Parser p = Parser.getParser(f)
        assert p instanceof NexposeParser
        long start = System.currentTimeMillis()
        List<Finding> l = p.parse()
        long stop = System.currentTimeMillis()
        println String.format("It took %d ms", (stop - start))
        l.each { println it.fullDescription() }
    }

    NexposeParser(content) {
        this.content = content
    }

    static boolean identify(contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }


    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        long start = System.currentTimeMillis()
        Map<String, NexposeVuln> vulns = getAllNexposeVulns()
        long stop = System.currentTimeMillis()
        println String.format("getAllNexposeVulns took %d ms", (stop - start))
//        vulns.each{k,v ->
//            println String.format("[%s] => %s",k,v.title)
//        }
        content.nodes.node.each { host ->
            def hostIp = host.@address
            def hostName = host.names.name[0]
            host.endpoints.endpoint.each { port ->
                def protocol = port.@protocol
                def portnr = port.@port
                port.services.service.each { service ->
                    def serviceName = service.@name
                    service.tests.test.each { issue ->
                        def pluginId = '' + issue.@id
                        NexposeVuln v = vulns.get(pluginId.toString())
                        if (v != null) {
                            def pluginName = v.title
                            def cvssVector = v.cvssVector
                            String cvssval = v.cvss ?: '0.0'
                            if (cvssval == '') cvssval = '0.0'
                            BigDecimal cvss = new BigDecimal(cvssval)
                            Finding.Severity severity = calcSeverity(cvss)
                            if (allowed(severity))
                                result << new Finding([scanner     : scanner, ip: hostIp, port: portnr, portStatus: 'open', protocol: protocol, hostName: hostName,
                                                       service     : serviceName,
                                                       plugin      : pluginId + ":" + pluginName,
                                                       exploitable : issue == 'true', baseCVSS: cvss, cvssVector: cvssVector,
                                                       severity    : severity, summary: buildSummary(v), description: v.description, reference: v.refs,
                                                       pluginOutput: reformat(contentAsText(issue)), solution: v.solution])
                        }
                    }
                }

            }
        }
        return result
    }

    private static String reformat(text) {
        return text.replaceAll('Paragraph', 'p').replaceAll('UnorderedList', 'ul').replaceAll('ListItem', 'li').replaceAll('ContainerBlockElement', 'div')
    }

    private static String contentAsText(NodeChildren node) {
        StringBuffer sb = new StringBuffer()
        node.each { sb.append(contentAsText(it)) }
        return sb.toString()
    }

    private static String contentAsText(NodeChild node) {
        StringBuffer sb = new StringBuffer()
        sb.append(String.format("<%s>", node.name()))

        if (node.children().size() > 0) {
            node.children().each {
                sb.append(contentAsText(it))
            }
        } else {
            sb.append(node.text().replaceAll('\n', '<br/>'))
        }
        sb.append(String.format("</%s>", node.name()))
        return sb.toString()
    }

    private String buildSummary(NexposeVuln item) {
        /*
    String , severity, , , , , pciSeverity, published, added, modified
    List<String> , tags, exploits, malware
         */
        return """
PCI Severity : ${item.pciSeverity}
Published    : ${item.published}
Added        : ${item.added}
Modified     : ${item.modified}
Tags         : <ul>${item.tags.collect { String.format("<li>%s</li>", it) }.join('')}</ul>
Exploits     : <ul>${item.exploits.collect { String.format("<li>%s</li>", it) }.join('')}</ul>
Malware      : <ul>${item.malware.collect { String.format("<li>%s</li>", it) }.join('')}</ul>
"""
    }

    private Finding.Severity calcSeverity(BigDecimal cvss) {
        switch (cvss) {
            case { it <= 3.9 }: return Finding.Severity.LOW
            case { it <= 6.9 }: return Finding.Severity.MEDIUM
            case { it <= 8.9 }: return Finding.Severity.HIGH
            case { it <= 10.0 }: return Finding.Severity.CRITICAL
            default: return Finding.Severity.UNKNOWN
        }
    }


    private Map<String, NexposeVuln> getAllNexposeVulns() {
        Map<String, NexposeVuln> result = new HashMap()
//        <vulnerability id="mysql-bug-29801-remote-federated-engine-crash"
//        title="MySQL Bug #29801: Remote Federated Engine Crash" severity="6" pciSeverity="4"
//        cvssScore="6.0" cvssVector="(AV:N/AC:M/Au:S/C:P/I:P/A:P)" published="20070714T000000000"
//        added="20100629T000000000" modified="20150130T000000000" riskScore="670.66736">
        content.VulnerabilityDefinitions.vulnerability.each { vuln ->
            def id = vuln.@id
            def title = vuln.@title
            def severity = vuln.@severity
            def cvss = vuln.@cvssScore
            def cvssVector = vuln.@cvssVector
            def pciSeverity = vuln.@pciSeverity
            def published = vuln.@published
            def added = vuln.@added
            def modified = vuln.@modified
            List<String> tags = vuln.tags?.tag?.collect { it }
            List<String> malware = vuln.malware?.name?.collect { it }
            List<String> refs = vuln.references?.reference?.collect { String.format("[%s] %s", it.@source, it) }
            // <exploit id="4978" title="dhclient 4.1 - Bash Environment Variable Command Injection (PoC) (Shellshock)"
//            type="exploitdb" link="http://www.exploit-db.com/exploits/36933" skillLevel="Expert"/>
            List exploits = vuln?.exploits?.exploit?.collect {
                String.format("Exploit [%s:%s] %s (%s) - %s", it.@type, it.@id, it.@title, it.@skillLevel, it.@link)
            }

            def description = reformat(contentAsText(vuln.description.ContainerBlockElement))
            def solution = reformat(contentAsText(vuln.solution))

            result.put(id.toString(), new NexposeVuln([title      : title, severity: severity, cvss: cvss, cvssVector: cvssVector,
                                                       description: description, solution: solution,
                                                       refs       : refs, tags: tags, exploits: exploits, published: published,
                                                       pciSeverity: pciSeverity, malware: malware, added: added, modified: modified]))
        }
        return result
    }
}

class NexposeVuln {
    String title, severity, cvss, cvssVector, description, solution, pciSeverity, published, added, modified
    List<String> refs, tags, exploits, malware
}

