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

import groovy.transform.CompileStatic
import net.vdbaan.issuefinder.model.Finding

@CompileStatic
class Container {
    Set<String> listedports = new HashSet<>()
    Set<String> listedservices = new HashSet<>()
    Finding.Severity highest = Finding.Severity.UNKNOWN
    Set<String> plugins = new HashSet<>()

    String toString() {
        StringBuilder sb = new StringBuilder()
        sb.append(String.format("open ports (%d)", listedports.size()))
        sb.append("\t")
        sb.append(listedports.join(","))
        sb.append("\n")
        sb.append(String.format("found services (%d)", listedservices.size()))
        sb.append("\t")
        sb.append(listedservices.join(","))
        sb.append("\n")
        sb.append(String.format("Highest vulnerability (%d %s)", plugins.size(), highest))
        sb.append("\t")
        sb.append(plugins.join(","))
        sb.append("\n")
        return sb.toString()
    }
}