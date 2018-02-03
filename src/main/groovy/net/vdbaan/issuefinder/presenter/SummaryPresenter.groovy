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
package net.vdbaan.issuefinder.presenter

import groovy.transform.CompileStatic
import javafx.scene.control.TreeItem
import net.vdbaan.issuefinder.util.Container
import net.vdbaan.issuefinder.util.Wrapper
import net.vdbaan.issuefinder.view.SummaryView

@CompileStatic
class SummaryPresenter {

    private SummaryView summaryView

    SummaryPresenter(final SummaryView view) {
        this.summaryView = view
    }

    void setSummary(final Map<String, Container> summary) {
        final TreeItem<Wrapper> treeRoot = new TreeItem(new Wrapper(key: String.format("Location (%d)", summary.size()), value: ""))
        summary.each { final k, final Container v ->
            final TreeItem<Wrapper> ip = new TreeItem(new Wrapper(key: k, value: ""))
            ip.children << new TreeItem(new Wrapper(key: String.format("open ports (%d)", v.listedports.size()), value: v.listedports.join(", ")))
            ip.children << new TreeItem(new Wrapper(key: String.format("found services (%d)", v.listedservices.size()), value: v.listedservices.join(", ")))
            final TreeItem<Wrapper> vulns = new TreeItem(new Wrapper(key: String.format("Highest vulnerability (%d)", v.plugins.size()), value: v.highest.toString()))
            v.plugins.each { vulns.children << new TreeItem(new Wrapper(key: "", value: it)) }
            ip.children << vulns
            treeRoot.children << ip
        }
        summaryView.setTreeRoot(treeRoot)
    }

}


