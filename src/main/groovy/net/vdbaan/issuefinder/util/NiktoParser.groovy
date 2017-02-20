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
        this.content = xmlslurper.parseText(content)
    }

    static boolean identify(contents) {
        try {
            def xml = xmlslurper.parseText(contents)
            return IDENTIFIER.equalsIgnoreCase(xml.name())
        } catch(Exception e) {
            return false
        }
    }

    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        String ip = content.scandetails.@targetip
        String port = content.scandetails.@targetport
        result += scanInfo(content, ip, port)
        content.scandetails.item.each { item ->
            String desc = item.description.toString()
            String plugin = item.@id
            plugin += ":" + desc
            def summary = "METHOD: " + item.@method
            summary += "\nOSVDB-" + item.@osvdbid
            summary += "\nURI: " + item.uri.toString()
            result << new Finding(scanner, ip, port, "web", plugin, Finding.Severity.INFO, summary)
        }
        return result
    }

    private Finding scanInfo(xml, String ip, String port) {
        String summary = "Nikto options: " + xml.@options
        summary += "\nHostname     : " + xml.scandetails.@targethostname
        summary += "\nBanner       : " + xml.scandetails.@targetbanner
        summary += "\nStart time   : " + xml.scandetails.@starttime
        summary += "\nEnd time     : " + xml.statistics.@endtime
        summary += "\nItems tested : " + xml.statistics.@itemstested
        summary += "\nItems found  : " + xml.statistics.@itemsfound
        //Finding (scanner,ip, port,service,plugin,severity,summary)

        new Finding(scanner, ip, "generic/" + port, "none", "scaninfo", Finding.Severity.INFO, summary)
    }

    private String cdata(String text) {
        int txtlen = text.length()
        println text
        return text.subSequence(9, txtlen - 2)
    }
}
