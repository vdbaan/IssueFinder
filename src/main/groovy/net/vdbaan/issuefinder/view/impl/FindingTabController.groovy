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

package net.vdbaan.issuefinder.view.impl

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.web.WebView
import javafx.stage.Window
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.FindingTabPresenter
import net.vdbaan.issuefinder.view.FindingTabView
import net.vdbaan.issuefinder.view.MainView

class FindingTabController implements FindingTabView {

    @FXML
    // fx:id="issueTable"
    private TableView<?> issueTable // Value injected by FXMLLoader

    @FXML
    // fx:id="textArea"
    private WebView textArea // Value injected by FXMLLoader


    @FXML
    // fx:id="riskColumn"
    private TableColumn riskColumn

    @Override
    @FXML
    void copySelectedIps(final ActionEvent event) {
        copySelectedIpsHandler?.handle(event)
    }

    @Override
    @FXML
    void copySelectedPortsAndIps(final ActionEvent event) {
        copySelectedPortsAndIpsHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnIP(final ActionEvent event) {
        filterOnIPHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnPlugin(final ActionEvent event) {
        filterOnPluginHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnPort(final ActionEvent event) {
        filterOnPortHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnProtocol(final ActionEvent event) {
        filterOnProtocolHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnPortStatus(final ActionEvent event) {
        filterOnPortStatusHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnRisk(final ActionEvent event) {
        filterOnRiskHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnScanner(final ActionEvent event) {
        filterOnScannerHandler?.handle(event)
    }

    @Override
    @FXML
    void filterOnService(final ActionEvent event) {
        filterOnServiceHandler?.handle(event)
    }

    @Override
    @FXML
    void editSelection(final ActionEvent event) {
        editSelectionHandler?.handle(event)
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        presenter = new FindingTabPresenter(this)
    }

    private MainView mainView
    private FindingTabPresenter presenter

    private EventHandler<ActionEvent> copySelectedIpsHandler
    private EventHandler<ActionEvent> copySelectedPortsAndIpsHandler
    private EventHandler<ActionEvent> filterOnIPHandler
    private EventHandler<ActionEvent> filterOnPluginHandler
    private EventHandler<ActionEvent> filterOnPortHandler
    private EventHandler<ActionEvent> filterOnProtocolHandler
    private EventHandler<ActionEvent> filterOnPortStatusHandler
    private EventHandler<ActionEvent> filterOnRiskHandler
    private EventHandler<ActionEvent> filterOnScannerHandler
    private EventHandler<ActionEvent> filterOnServiceHandler
    private EventHandler<ActionEvent> editSelectionHandler

    void setCopySelectedIpsHandler(final EventHandler<ActionEvent> copySelectedIpsHandler) {
        this.copySelectedIpsHandler = copySelectedIpsHandler
    }

    void setCopySelectedPortsAndIpsHandler(final EventHandler<ActionEvent> copySelectedPortsAndIpsHandler) {
        this.copySelectedPortsAndIpsHandler = copySelectedPortsAndIpsHandler
    }

    void setFilterOnIPHandler(final EventHandler<ActionEvent> filterOnIPHandler) {
        this.filterOnIPHandler = filterOnIPHandler
    }

    void setFilterOnPluginHandler(final EventHandler<ActionEvent> filterOnPluginHandler) {
        this.filterOnPluginHandler = filterOnPluginHandler
    }

    void setFilterOnPortHandler(final EventHandler<ActionEvent> filterOnPortHandler) {
        this.filterOnPortHandler = filterOnPortHandler
    }

    void setFilterOnProtocolHandler(final EventHandler<ActionEvent> filterOnProtocolHandler) {
        this.filterOnProtocolHandler = filterOnProtocolHandler
    }

    void setFilterOnPortStatusHandler(final EventHandler<ActionEvent> filterOnPortStatusHandler) {
        this.filterOnPortStatusHandler = filterOnPortStatusHandler
    }

    void setFilterOnRiskHandler(final EventHandler<ActionEvent> filterOnRiskHandler) {
        this.filterOnRiskHandler = filterOnRiskHandler
    }

    void setFilterOnScannerHandler(final EventHandler<ActionEvent> filterOnScannerHandler) {
        this.filterOnScannerHandler = filterOnScannerHandler
    }

    void setFilterOnServiceHandler(final EventHandler<ActionEvent> filterOnServiceHandler) {
        this.filterOnServiceHandler = filterOnServiceHandler
    }

    void setEditSelectionHandler(final EventHandler<ActionEvent> editSelectionHandler) {
        this.editSelectionHandler = editSelectionHandler
    }

    void bindMasterData(final ObservableList<Finding> masterData) {
        presenter.bindMasterData(masterData)
    }

    @Override
    void setMainTableItems(final ObservableList items) {
        issueTable.items = items
    }

    @Override
    void setSelectItemPropertyListener(final ChangeListener listener) {
        issueTable.selectionModel.selectedItemProperty().addListener(listener)
    }

    @Override
    void setTableSelectionMode(final SelectionMode mode) {
        issueTable.selectionModel.selectionMode = mode

    }

    @Override
    void textArealoadContent(final String content) {
        textArea.engine.loadContent(content)
    }

    ReadOnlyObjectProperty<Comparator> getTableComparatorProperty() {
        return issueTable.comparatorProperty()
    }

    @Override
    void copyUniqueIPs(final ObservableList<Finding> data) {
        presenter.copyUniqueIPs(data)
    }

    @Override
    void copyUniquePortAndIPs(final ObservableList<Finding> data) {
        presenter.copyUniquePortAndIPs(data)
    }


    void setMasterView(final MainView mainView) {
        this.mainView = mainView
    }

    @Override
    void setMainFilterText(final String text) {
        mainView.filterText = text
    }

    @Override
    Finding getSelectedFinding() {
        return (Finding) issueTable.selectionModel.selectedItem
    }

    ObservableList<Finding> getSelectedFindingsList() {
        return (issueTable.selectionModel.selectedItems as ObservableList<Finding>)
    }

    void setClipboardContent(final ClipboardContent content) {
        Clipboard.systemClipboard.content = (content)
    }

    @Override
    Window getWindow() {
        return mainView.window
    }

    @Override
    void refresh() {
        mainView.refresh()
    }

    void setRiskColumnCellFactory(Callback callback) {
        riskColumn.setCellFactory(callback)
    }
}

