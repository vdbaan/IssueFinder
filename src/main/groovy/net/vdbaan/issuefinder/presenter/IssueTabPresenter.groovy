/*
 *  Copyright (C) 2018  S. van der Baan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder.presenter

import groovy.transform.CompileStatic
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import javafx.scene.control.TableRow
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.IssueTabView

@CompileStatic
class IssueTabPresenter {

    IssueTabView view
    private ObservableList<Finding> masterData

    IssueTabPresenter(final IssueTabView view) {
        this.view = view
        view.tableSelectionMode = SelectionMode.MULTIPLE
    }

    void bindMasterData(final ObservableList<Finding> mData) {
        this.masterData = mData
        view.selectItemPropertyListener = this.&showContent

        view.tableCellFactory = { final val ->
            return new TableCell<Finding, String>() {
                List<String> cellStyles = ['cell', 'indexed-cell', 'table-cell', 'table-column']
                List<String> rowStyles = ['cell', 'indexed-cell', 'table-row-cell']

                @Override
                protected void updateItem(final String item, final boolean empty) {
                    super.updateItem(item, empty)
                    final Finding f = masterData.find { final f -> f.plugin == item }
                    styleClass.clear()
                    styleClass.addAll(cellStyles)
                    final TableRow<Finding> row = tableRow
                    row.styleClass.clear()
                    row.styleClass.addAll(rowStyles)
                    if (item != null) {
                        text = item.toString()
                        if (f != null) {
                            final Finding.Severity sev = f.severity
                            if (Config.instance.getProperty(Config.COLOURED_ROWS) as boolean)
                                row.styleClass.add(sev.toString() + 'ROW')
                            else
                                styleClass.add(sev.toString())
                        }
                    }
                }
            }
        }

    }


    void dataListener(final ListChangeListener.Change<? extends Finding> c) {
        final HashMap<String, Finding> uniques = new HashMap<>()
        masterData.each { final finding ->
            (uniques[finding.plugin] = finding)
        }
        final List findings = new ArrayList<Finding>()
        findings.addAll(uniques.values())
        view.mainTableItems = FXCollections.observableList(findings)
        view.ipList = FXCollections.observableList(new ArrayList<Location>())
        view.description = ''
        view.remediation = ''
        view.output = ''
        view.sortMainTable()
    }

    void showContent(
            final ObservableValue<? extends Finding> observable, final Finding oldValue, final Finding newValue) {
        if (observable.value != null) {
            final String plugin = observable.value.plugin
            view.description = observable.value.description.replace('\n', '<br/>')
            view.remediation = observable.value.solution.replace('\n', '<br/>')
            final Map<String, Location> locations = new HashMap()
            final List<String> output = new ArrayList<>()
            masterData.findAll { final finding -> finding.plugin == plugin }.each { final result ->
                locations[String.format("%s%s%s", result.ip, result.port, result.location)] = new Location(ip: result.ip, port: result.port, location: result.location)
                output << String.format('<pre>%s (%s)\n%s</pre>', result.ip, result.location, result.pluginOutput)
            }
            view.output = output.join('<hr/>')
            view.ipList = FXCollections.observableList(locations.values() as List)
        }
    }

    void changeTab() {
        masterData.addListener(this.&dataListener as ListChangeListener)
        dataListener(null)
    }
}

class Location {
    String ip, port, location
}
