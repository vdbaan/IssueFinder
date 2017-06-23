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

class NiktoParser extends Parser{
    static String IDENTIFIER = "niktoscan"
    static String scanner = "Nikto"

    NiktoParser(content) {
        this.content = content.niktoscan
    }

    static boolean identify(contents) {
        return IDENTIFIER.equalsIgnoreCase(contents.name())
    }

    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        String ip = content.scandetails.@targetip
        String hostName = content.scandetails.@targethostname
        String port = content.scandetails.@targetport
        result += scanInfo(content, ip, hostName, port)

        content.scandetails.item.each { item ->
            String desc = item.description.toString()
            String plugin = item.@id
            plugin += ": " + desc
            def summary = "METHOD: " + item.@method
            summary += "\nOSVDB-" + item.@osvdbid
            summary += "\nURI: " + item.uri

            result << new Finding([scanner:scanner, ip:ip, port:port+'/open/tcp', service:"web", plugin:plugin,hostName: hostName,
                                   severity:Finding.Severity.INFO, summary:summary])
        }
        return result
    }

    private Finding scanInfo(xml, String ip, String hostName, String port) {
        String summary = "Nikto options: " + xml.@options
        summary += "\nBanner       : " + xml.scandetails.@targetbanner
        summary += "\nVersion      : " + xml.scandetails.@version
        summary += "\nStart time   : " + xml.scandetails.@starttime
        summary += "\nEnd time     : " + xml.scandetails.statistics.@endtime
        summary += "\nItems tested : " + xml.scandetails.statistics.@itemstested
        summary += "\nItems found  : " + xml.scandetails.statistics.@itemsfound
        //Finding (scanner,ip, port,service,plugin,severity,summary)

        new Finding([scanner:scanner, ip:ip, port:"generic/" + port, service:"none", plugin:"scaninfo",hostName: hostName,
                     severity:Finding.Severity.INFO, summary:summary])
    }

    private String cdata(String text) {
        int txtlen = text.length()
        println text
        return text.subSequence(9, txtlen - 2)
    }
}
