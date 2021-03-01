package net.vdbaan.issuefinder.parser

import groovy.util.logging.Log
import net.vdbaan.issuefinder.model.Finding

@Log
class ZAPParser extends Parser {
    static String IDENTIFIER = "OWASPZAPReport"
    static String scanner = "ZAP"

    ZAPParser(final content) {
        this.content = content
    }

    static boolean identify(final contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }

    @Override
    List<Finding> parse() {
        final List<Finding> result = new ArrayList<>()

        content.site.each { final site ->
            final String host = site.@host
            final String port = site.@port
            final String ip = host
            final URL url = new URL(site.@name.toString())
            final String service = url.protocol.toUpperCase()
            site.alerts.alertitem.each { final issue ->
                final String plugin = issue.pluginid + ':' + issue.name
                final Finding.Severity severity = calc(issue.riskcode)
                if (allowed(severity)) {
                    result << new Finding([scanner : scanner, ip: ip, port: port, portStatus: 'open', protocol: 'tcp', hostName: host,
                                           service : service, plugin: plugin,
                                           severity: severity, summary: buildSummary(issue)])
                }

            }
        }
        return result
    }

    private Finding.Severity calc(final severity) {
//        CRITICAL, HIGH, MEDIUM, LOW, INFO, UNKNOWN
        switch (severity) {
            case '3': return Finding.Severity.HIGH
            case '2': return Finding.Severity.MEDIUM
            case '1': return Finding.Severity.LOW
            case '0': return Finding.Severity.INFO
            default: return Finding.Severity.UNKNOWN
        }
    }

    private static String buildSummary(final issue) {
        String summary = "Name: " + issue.name
        final String description = '' + issue.description
        final String solution = '' + issue.solution
        final String otherInfo = '' + issue.otherinfo
        final String reference = '' + issue.reference
        summary += "\nDescription:\n " + description.replace('\n', ' ')
        summary += "\nSolution:\n" + solution.replace('\n', ' ')
        summary += "\nOther info:\n" + otherInfo.replace('\n', ' ')
        summary += "\nreference:\n" + reference.replace('\n', ' ')
        summary += "\ncwe  id: " + issue.cweid
        summary += "\nwasc id: " + issue.wascid
        summary += "\nInstances:\n"
        issue.instances.instance.each {
            summary += '- ' + it.url + '\n'
        }

        return summary
    }
}
