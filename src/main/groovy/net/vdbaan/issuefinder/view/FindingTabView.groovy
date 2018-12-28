/*
 *  Copyright (C) 2017  S. van der Baan
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

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.SelectionMode
import javafx.scene.input.ClipboardContent
import javafx.stage.Window
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.RetentionFileChooser

interface FindingTabView {
    @FXML
    void copySelectedIps(ActionEvent event)

    @FXML
    void copySelectedPortsAndIps(ActionEvent event)

    @FXML
    void filterOnIP(ActionEvent event)

    @FXML
    void filterOnPlugin(ActionEvent event)

    @FXML
    void filterOnPort(ActionEvent event)

    @FXML
    void filterOnProtocol(ActionEvent event)

    @FXML
    void filterOnPortStatus(ActionEvent event)

    @FXML
    void filterOnRisk(ActionEvent event)

    @FXML
    void filterOnScanner(ActionEvent event)

    @FXML
    void filterOnService(ActionEvent event)

    @FXML
    void editSelection(ActionEvent event)

    @FXML
    void exportToCSV(ActionEvent event)

    @FXML
    void exportToIvil(ActionEvent event)

    void setCopySelectedIpsHandler(EventHandler<ActionEvent> copySelectedIpsHandler)

    void setCopySelectedPortsAndIpsHandler(EventHandler<ActionEvent> copySelectedPortsAndIpsHandler)

    void setFilterOnIPHandler(EventHandler<ActionEvent> filterOnIPHandler)

    void setFilterOnPluginHandler(EventHandler<ActionEvent> filterOnPluginHandler)

    void setFilterOnPortHandler(EventHandler<ActionEvent> filterOnPortHandler)

    void setFilterOnProtocolHandler(EventHandler<ActionEvent> filterOnProtocolHandler)

    void setFilterOnPortStatusHandler(EventHandler<ActionEvent> filterOnPortStatusHandler)

    void setFilterOnRiskHandler(EventHandler<ActionEvent> filterOnRiskHandler)

    void setFilterOnScannerHandler(EventHandler<ActionEvent> filterOnScannerHandler)

    void setFilterOnServiceHandler(EventHandler<ActionEvent> filterOnServiceHandler)

    void setEditSelectionHandler(EventHandler<ActionEvent> editSelectionHandler)

    void setExportToCSVHandler(EventHandler<ActionEvent> handler)

    void setExportToIvilHandler(EventHandler<ActionEvent> handler)

    ReadOnlyObjectProperty<Comparator> getTableComparatorProperty()

    void setMainTableItems(ObservableList items)

    void setSelectItemPropertyListener(ChangeListener listener)

    void setTableSelectionMode(SelectionMode mode)

    void textArealoadContent(String content)

    void copyUniqueIPs(ObservableList<Finding> data)

    void copyUniquePortAndIPs(ObservableList<Finding> data)

    void setMainFilterText(String text)

    Finding getSelectedFinding()

    ObservableList<Finding> getSelectedFindingsList()

    void setClipboardContent(ClipboardContent content)

    Window getWindow()

    RetentionFileChooser getRetentionFileChooser()

    void refresh()

    void setRiskColumnCellFactory(Callback callback)
}