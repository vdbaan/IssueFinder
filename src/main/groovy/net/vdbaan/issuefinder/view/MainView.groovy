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
package net.vdbaan.issuefinder.view

import groovy.transform.CompileStatic
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.SelectionMode
import javafx.scene.input.ClipboardContent
import javafx.stage.Window
import net.vdbaan.issuefinder.MainApp
import net.vdbaan.issuefinder.db.DbListener
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.RetentionFileChooser

@CompileStatic
interface MainView extends BaseView, DbListener {
    void setMainApp(MainApp mainApp)

    void aboutHelp(ActionEvent event)

    void aboutAction(ActionEvent event)

    void buildSummary(ActionEvent event)

    void clearFilter(ActionEvent event)

    void closeAction(ActionEvent event)

    void copyUniqueIps(ActionEvent event)

    void copyUniquePortsAndIps(ActionEvent event)

    void exportAction(ActionEvent event)

    void filterOnIp(ActionEvent event)

    void filterOnPlugin(ActionEvent event)

    void filterOnPort(ActionEvent event)

    void filterOnService(ActionEvent event)

    void filterOnScanner(ActionEvent event)

    void filterOnPortStatus(ActionEvent event)

    void filterOnProtocol(ActionEvent event)

    void filterOnRisk(ActionEvent event)

    void filterTable(ActionEvent event)

    void modifyEntry(ActionEvent event)

    void copySelectedIps(ActionEvent event)

    void copySelectedIpsPorts(ActionEvent event)

    void newAction(ActionEvent event)

    void openAction(ActionEvent event)

    void openSettings(ActionEvent event)

    EventHandler<ActionEvent> getAboutAction()


    void setAboutHelpAction(EventHandler<ActionEvent> aboutHelpAction)
    EventHandler<ActionEvent> getAboutHelpAction()

    void setAboutAction(EventHandler<ActionEvent> aboutAction)

    EventHandler<ActionEvent> getSummaryAction()

    void setSummaryAction(EventHandler<ActionEvent> summaryAction)

    EventHandler<ActionEvent> getClearAction()

    void setClearAction(EventHandler<ActionEvent> clearAction)

    EventHandler<ActionEvent> getCloseAction()

    void setCloseAction(EventHandler<ActionEvent> closeAction)

    EventHandler<ActionEvent> getCopyIpAction()

    void setCopyIpAction(EventHandler<ActionEvent> copyIpAction)

    EventHandler<ActionEvent> getCopyIpPortAction()

    void setCopyIpPortAction(EventHandler<ActionEvent> copyIpPortAction)

    EventHandler<ActionEvent> getExportAction()

    void setExportAction(EventHandler<ActionEvent> exportAction)

    EventHandler<ActionEvent> getFilterIpAction()

    void setFilterIpAction(EventHandler<ActionEvent> filterIpAction)

    EventHandler<ActionEvent> getFilterPortAction()

    void setFilterPortAction(EventHandler<ActionEvent> filterPortAction)

    EventHandler<ActionEvent> getFilterPluginAction()

    void setFilterPluginAction(EventHandler<ActionEvent> filterPluginAction)

    EventHandler<ActionEvent> getFilterServiceAction()

    void setFilterServiceAction(EventHandler<ActionEvent> filterServiceAction)

    EventHandler<ActionEvent> getFilterOnScannerAction()

    void setFilterOnScannerAction(EventHandler<ActionEvent> filterOnScannerAction)

    EventHandler<ActionEvent> getFilterOnPortStatusAction()

    void setFilterOnPortStatusAction(EventHandler<ActionEvent> filterOnPortStatusAction)

    EventHandler<ActionEvent> getFilterOnProtocolAction()

    void setFilterOnProtocolAction(EventHandler<ActionEvent> filterOnProtocolAction)

    EventHandler<ActionEvent> getFilterOnRiskAction()

    void setFilterOnRiskAction(EventHandler<ActionEvent> filterOnRiskAction)

    EventHandler<ActionEvent> getSettingsAction()

    void setSettingsAction(EventHandler<ActionEvent> settingsAction)

    EventHandler<ActionEvent> getOpenAction()

    void setOpenAction(EventHandler<ActionEvent> openAction)

    void setLoadAction(EventHandler<ActionEvent> loadAction)
    void setSaveAction(EventHandler<ActionEvent> saveAction)

    EventHandler<ActionEvent> getNewAction()

    void setNewAction(EventHandler<ActionEvent> newAction)

    EventHandler<ActionEvent> getModifyAction()

    EventHandler<ActionEvent> getFilterTableAction()

    void setFilterTableAction(EventHandler<ActionEvent> filterTable)

    void setModifyAction(EventHandler<ActionEvent> modifyAction)

    void setCopySelectedIpsPortsAction(EventHandler<ActionEvent> copySelectedIpsPortsAction)

    void setCopySelectedIpsAction(EventHandler<ActionEvent> copySelectedIpsAction)

    void setRowInfoLabel(String text)

    void setIpInfoLabel(String text)

    void setStatusLabel(String text)

    void setFilterTextItems(List<String> items)

    List<String> getFilterTextItems()

    void addFiterTextListener(ChangeListener listener)

    void setFilterText(String text)

    void setMainTableItems(ObservableList items)

    void setSelectItemPropertyListener(ChangeListener listener)

    void setTableSelectionMode(SelectionMode mode)

    ObservableList<String> getFilterTextStyleClass()

    void addFilterTextMouseClicked(EventHandler handler)

    ObjectProperty<EventHandler> getTableKeyEvent()

    void textArealoadContent(String content)

    Finding getSelectedFinding()

    Window getWindow()

    List<Finding> getSelectedFindings()

    String getFilterTextValue()

    ReadOnlyObjectProperty<Comparator> getTableComparatorProperty()

    RetentionFileChooser getRetentionFileChooser()

    void setClipboardContent(ClipboardContent content)
}