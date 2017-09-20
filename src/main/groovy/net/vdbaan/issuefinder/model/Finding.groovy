package net.vdbaan.issuefinder.model

import groovy.transform.Canonical
import groovyx.javafx.beans.FXBindable


/**
 * Created by Steven on 31/08/2017.
 */


class Finding {
    enum Severity {
        CRITICAL, HIGH, MEDIUM, LOW, INFO, UNKNOWN
    }

    @FXBindable String scanner= ''
    @FXBindable String ip= ''
    @FXBindable String hostName= ''
    @FXBindable String port= ''
    @FXBindable String service= ''
    @FXBindable String plugin = ''
    @FXBindable Severity severity = Severity.UNKNOWN
    @FXBindable String summary = ''
    @FXBindable String baseCVSS = '0.0'
    @FXBindable Boolean exploitable = false

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
