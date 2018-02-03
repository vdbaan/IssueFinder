package net.vdbaan.issuefinder.parser

import net.vdbaan.issuefinder.model.Finding

class OpenVASParser extends Parser {

    static String IDENTIFIER = "report"
    static String scanner = "OpenVAS"

    OpenVASParser(final content) {
        this.content = content
    }

    static boolean identify(final contents) {
        boolean hasReport = false
        contents.'**'.each { if ('report'.equals(it.name())) hasReport = true }
        return IDENTIFIER.equalsIgnoreCase(contents.name()) && hasReport
    }

    List<Finding> parse() {
        final List<Finding> result = new ArrayList<>()
        content?.report?.results?.result?.each { final issue ->
            final String ip = issue.host
            final String port = issue.port
            final String hostname = ip
            final String service = ''
            final String plugin = issue.nvt.@oid.toString() + ':' + issue.nvt.name.toString()
            String cvssval = issue.severity ?: '0.0'
            if (cvssval == '') cvssval = '0.0'
            final BigDecimal cvss = new BigDecimal(cvssval)
            final Finding.Severity severity = calcSeverity(cvss)
            if (allowed(severity))
                result << new Finding([scanner : scanner, ip: ip, port: port, portStatus: 'open', protocol: 'tcp', hostName: hostname,
                                       service : service, plugin: plugin, baseCVSS: cvss,
                                       severity: severity, summary: buildSummary(issue)])
        }
        return result
    }

    Finding.Severity calcSeverity(final BigDecimal cvss) {
        switch (cvss) {
            case { it <= 3.9 }: return Finding.Severity.LOW
            case { it <= 6.9 }: return Finding.Severity.MEDIUM
            case { it <= 8.9 }: return Finding.Severity.HIGH
            case { it <= 10.0 }: return Finding.Severity.CRITICAL
            default: return Finding.Severity.UNKNOWN
        }
    }

    String buildSummary(final def issue) {
        return """
family: ${issue.family}
cve: ${issue.cve}
bid: ${issue.bid}
tags: ${issue.tags}
"""
    }
}
