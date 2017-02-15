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


 package net.vdbaan.issuefinder.model

class Finding {
    enum Severity {
        CRITICAL, HIGH, MEDIUM, LOW, INFO, UNKNOWN
    }

    String scanner
    String ip
    String port
    String service
    String plugin
    Severity severity
    String summary

    @Override
    String toString() {
        return String.format("scanner:%s, source:%s:%s (%s), plugin:%s, risk:%s",
                scanner, ip, port, service, plugin, severity)
    }

    Finding(String scanner, String hostName, String port, String service, String plugin, Severity risk, String summary) {
        this.scanner = scanner
        this.ip = hostName
        this.port = port
        this.service = service
        this.plugin = plugin
        this.severity = risk
        this.summary = summary
    }

    String fullDescription() {
        return String.format("scanner: %s\nsource: %s:%s\nservice: %s\nplugin: %s\nrisk: %s\n"+
                "=============================================\n"+"summary:\n%s\n",
                scanner, ip, port, service, plugin, severity, summary)
    }
}
