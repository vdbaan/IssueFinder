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

class NiktoParser extends Parser {
    static String IDENTIFIER = "niktoscan"
    static String scanner = "Nikto"

    NiktoParser(final content) {
        this.content = content
    }

    static boolean identify(final contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }

    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        final String ip = content.scandetails.@targetip
        final String hostName = content.scandetails.@targethostname
        final String port = content.scandetails.@targetport
        if (allowed(Finding.Severity.INFO)) {
            result += scanInfo(content, ip, hostName, port)
        }

        content.scandetails.item.each { final item ->
            final String desc = item.description.toString()
            String plugin = item.@id
            plugin += ": " + desc
            def summary = "METHOD: " + item.@method
            summary += "\nOSVDB-" + item.@osvdbid
            summary += "\nURI: " + item.uri

            if (allowed(Finding.Severity.INFO)) {
                result << new Finding([scanner : scanner, ip: ip, port: port, portStatus: 'open', protocol: 'tcp', service: "web", plugin: plugin, hostName: hostName,
                                       severity: Finding.Severity.INFO, summary: summary])
            }
        }
        return result
    }

    private Finding scanInfo(final xml, final String ip, final String hostName, final String port) {
        String summary = "Nikto options: " + xml.@options
        summary += "\nBanner       : " + xml.scandetails.@targetbanner
        summary += "\nVersion      : " + xml.scandetails.@version
        summary += "\nStart time   : " + xml.scandetails.@starttime
        summary += "\nEnd time     : " + xml.scandetails.statistics.@endtime
        summary += "\nItems tested : " + xml.scandetails.statistics.@itemstested
        summary += "\nItems found  : " + xml.scandetails.statistics.@itemsfound
        //Finding (scanner,ip, port,service,plugin,severity,summary)

        new Finding([scanner : scanner, ip: ip, port: port, portStatus: 'open', protocol: 'tcp', service: "none", plugin: "scaninfo", hostName: hostName,
                     severity: Finding.Severity.INFO, summary: summary])
    }

}
