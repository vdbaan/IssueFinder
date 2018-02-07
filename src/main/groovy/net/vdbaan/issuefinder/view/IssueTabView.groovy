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

package net.vdbaan.issuefinder.view

import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.input.ClipboardContent
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.Location

interface IssueTabView {

    @FXML
    void copySelectedIps(ActionEvent event)

    @FXML
    void copySelectedPortsAndIps(ActionEvent event)

    void bindMasterData(ObservableList<Finding> masterData)

    void setSelectItemPropertyListener(ChangeListener listener)

    void setMainTableItems(ObservableList<Finding> data)

    void setDescription(String description)

    void setRemediation(String remediation)

    void setOutput(String output)

    void setIpList(ObservableList<Location> iPs)

    void sortMainTable()

    void setTableSelectionMode(SelectionMode selectionMode)


    void setCopySelectedIpsHandler(EventHandler<ActionEvent> copySelectedIpsHandler)

    void setCopySelectedPortsAndIpsHandler(EventHandler<ActionEvent> copySelectedPortsAndIpsHandler)

    void setTableCellFactory(Callback<TableColumn<Finding, String>, TableCell<Finding, String>> call)

    void setClipboardContent(ClipboardContent content)

    ObservableList<Finding> getSelectedFindingsList()
}