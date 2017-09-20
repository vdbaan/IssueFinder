package net.vdbaan.issuefinder.model



/**
 * Created by Steven on 31/08/2017.
 */


class Finding {
    enum Severity {
        CRITICAL, HIGH, MEDIUM, LOW, INFO, UNKNOWN
    }

    String scanner= ''
    String ip= ''
    String hostName= ''
    String port= ''
    String service= ''
    String plugin = ''
    Severity severity = Severity.UNKNOWN
    String summary = ''
    String baseCVSS = '0.0'
    Boolean exploitable = false

    @Override
    String toString() {
        return String.format("scanner:%s, source:%s:%s (%s), plugin:%s, risk:%s cvss:%s, exploitable:%b",
                scanner, ip, port, service, plugin, severity,baseCVSS,exploitable)
    }

    String fullDescription() {
        return String.format("scanner: %s\nsource: %s:%s\nservice: %s\nplugin: %s\nrisk: %s\n"+
                "=============================================\n"+"summary:\n%s\n",
                scanner, ip, port, service, plugin, severity, summary)
    }
    String htmlDescription() {
        return String.format("scanner: %s<br/>source: %s:%s<br/>service: %s<br/>plugin: %s<br/>risk: %s<br/>"+
                "<hr/>"+"summary:<br/>%s",
                scanner, ip, port, service, plugin, severity, summary.replace('\n','<br/>'))
    }
}
