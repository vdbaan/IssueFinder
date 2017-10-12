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

/**
 * Created by steven on 18/12/15.
 */
class SSLyzeParser extends Parser {
    static IDENTIFIER = "document"
    static scanner = "SSLyze"
    static service = 'ssl'

    SSLyzeParser(content) {
        this.content = content
    }

    static boolean identify(contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }

    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()

        // for a weird reason, content.result.target.each barfs out with an Exception
        for (def i = 0; i < content.results.target.size(); i++) {
            if (allowed(Finding.Severity.HIGH))
                result << reportSSLv2(content.results.target[i])
            if (allowed(Finding.Severity.HIGH))
                result << reportSSLv3(content.results.target[i])
            if (allowed(Finding.Severity.MEDIUM))
                result << reportTLSv1(content.results.target[i])
            if (allowed(Finding.Severity.INFO))
                result << reportTLSv1_1(content.results.target[i])
            if (allowed(Finding.Severity.INFO))
                result << reportTLSv1_2(content.results.target[i])
            if (allowed(Finding.Severity.INFO))
                reportOther(result, content.results.target[i])
        }

        return result
    }

    private Finding reportCert(target) {
        def ip = target.@ip
        def port = target.@port
        def plugin = target.certinfo_basic.@title
        def severity = Finding.Severity.INFO
        def summary = "Received Certificate:"
        summary += "\nissuer OU" + target.certinfo_basic.receivedCertificateChain.certificate.organizationalUnitName
//        summary
        return null
    }

    private Finding reportSSLv2(target) {
        def ip = target.@ip
        def port = target.@port
        return buildSuite(ip, port, target.sslv2, Finding.Severity.HIGH)
    }

    private Finding reportSSLv3(target) {
        def ip = target.@ip
        def port = target.@port
        return buildSuite(ip, port, target.sslv3, Finding.Severity.HIGH)
    }

    private Finding reportTLSv1(target) {
        def ip = target.@ip
        def port = target.@port
        return buildSuite(ip, port, target.tlsv1, Finding.Severity.MEDIUM)
    }

    private Finding reportTLSv1_1(target) {
        def ip = target.@ip
        def port = target.@port
        return buildSuite(ip, port, target.tlsv1_1, Finding.Severity.INFO)
    }

    private Finding reportTLSv1_2(target) {
        def ip = target.@ip
        def port = target.@port
        return buildSuite(ip, port, target.tlsv1_2, Finding.Severity.INFO)
    }

    private void reportOther(results, target) {
        // scanner, ip, port, service, plugin, severity, summary
        def ip = target.@ip as String
        def port = target.@port as String
        results << new Finding([scanner: scanner, ip: ip, port: port + '/open/tcp', service: 'ssl', plugin: target.heartbleed.@title as String,
                                risk   : target.heartbleed.openSslHeartbleed.@isVulnerable == 'True' ? Finding.Severity.HIGH : Finding.Severity.INFO,
                                summary: ''])
        results << new Finding([scanner: scanner, ip: ip, port: port + '/open/tcp', service: 'ssl', plugin: target.fallback.@title as String,
                                risk   : target.fallback.tlsFallbackScsv.@isVulnerable == 'True' ? Finding.Severity.HIGH : Finding.Severity.INFO,
                                summary: ''])
        results << new Finding([scanner: scanner, ip: ip, port: port + '/open/tcp', service: 'ssl', plugin: target.openssl_ccs.@title as String,
                                risk   : target.openssl_ccs.openSslCcsInjection.@isVulnerable == 'True' ? Finding.Severity.HIGH : Finding.Severity.INFO,
                                summary: ''])
        results << new Finding([scanner: scanner, ip: ip, port: port + '/open/tcp', service: 'ssl', plugin: target.reneg.@title as String,
                                risk   : target.reneg.sessionRenegotiation.@isSecure == 'False' ? Finding.Severity.HIGH : Finding.Severity.INFO,
                                summary: ''])
//        <compression title="Deflate Compression">
//        <compressionMethod isSupported="False" type="DEFLATE"/>
//        </compression>
//      <resum title="Session Resumption">
//        <sessionResumptionWithSessionIDs errors="0" failedAttempts="0" isSupported="True" successfulAttempts="5" totalAttempts="5"/>
//        <sessionResumptionWithTLSTickets isSupported="False" reason="TLS ticket not assigned"/>
//        </resum>
    }

    private Finding buildSuite(ip, port, suite, risk) {

        def plugin = suite.@title
        def severity = suite.@isProtocolSupported == 'True' ? risk : Finding.Severity.INFO
        def summary = "CipherSuites"
        def prefered = '' + suite.preferredCipherSuite.cipherSuite.cipher.@name
        summary += "\n- Preferred: " + (prefered ?: 'None')
        summary += "\n- Accepted: "
        def accepted = ''
        suite.acceptedCipherSuites.cipherSuite.each { cipher ->
            accepted += "\n" + cipher.@name
        }
        summary += accepted ?: 'None'
        summary += "\n\n- Rejected: "
        def rejected = ''
        suite.rejectedCipherSuites.cipherSuite.each { cipher ->
            rejected += "\n" + cipher.@name
        }
        summary += rejected ?: 'None'

        return new Finding([scanner: scanner, ip: ip as String, port: port as String, service: service,
                            plugin : plugin as String, risk: severity, summary: summary])
    }
}
