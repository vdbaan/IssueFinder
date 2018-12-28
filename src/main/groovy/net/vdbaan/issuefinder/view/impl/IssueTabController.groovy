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

package net.vdbaan.issuefinder.view.impl

import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.web.WebView
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.IssueTabPresenter
import net.vdbaan.issuefinder.presenter.Location
import net.vdbaan.issuefinder.util.TableUtils
import net.vdbaan.issuefinder.view.IssueTabView

class IssueTabController implements IssueTabView {
    @FXML
    // fx:id="findingsTable"
    private TableView<?> findingsTable // Value injected by FXMLLoader

    @FXML
    // fx:id="riskColumn"
    private TableColumn riskColumn // Value injected by FXMLLoader

    @FXML
    //fx:id="findingColumn"
    private TableColumn findingColumn // Value injected by FXMLLoader

    @FXML
    // fx:id="descriptionView"
    private WebView descriptionView // Value injected by FXMLLoader

    @FXML
    // fx:id="remediationView"
    private WebView remediationView // Value injected by FXMLLoader

    @FXML
    // fx:id="outputView"
    private WebView outputView // Value injected by FXMLLoader

    @FXML
    // fx:id="ipTable"
    private TableView<?> ipTable // Value injected by FXMLLoader

    private IssueTabPresenter presenter

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

    @FXML
    void createIssue(ActionEvent event) {
        createIssueHandler?.handle(event)
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        presenter = new IssueTabPresenter(this)
        findingsTable.sortOrder.clear()
        findingsTable.sortOrder.addAll(riskColumn)
        findingsTable.sortOrder.addAll(findingColumn)

        TableUtils.installCopyPasteHandler(ipTable)
    }

    private EventHandler<ActionEvent> copySelectedIpsHandler
    private EventHandler<ActionEvent> copySelectedPortsAndIpsHandler
    private EventHandler<ActionEvent> createIssueHandler

    void setCopySelectedIpsHandler(EventHandler<ActionEvent> copySelectedIpsHandler) {
        this.copySelectedIpsHandler = copySelectedIpsHandler
    }

    void setCopySelectedPortsAndIpsHandler(EventHandler<ActionEvent> copySelectedPortsAndIpsHandler) {
        this.copySelectedPortsAndIpsHandler = copySelectedPortsAndIpsHandler
    }

    void setCreateIssueHandler(EventHandler<ActionEvent> createIssueHandler) {
        this.createIssueHandler = createIssueHandler
    }

    void bindMasterData(final ObservableList<Finding> masterData) {
        presenter.bindMasterData(masterData)
    }

    @Override
    void setSelectItemPropertyListener(final ChangeListener listener) {
        findingsTable.selectionModel.selectedItemProperty().addListener(listener)
    }

    @Override
    void setMainTableItems(final ObservableList<Finding> data) {
        findingsTable.items = data
    }

    @Override
    void setDescription(final String description) {
        descriptionView.engine.loadContent(description)

    }

    @Override
    void setRemediation(final String remediation) {
        remediationView.engine.loadContent(remediation)
    }

    @Override
    void setOutput(final String output) {
        outputView.engine.loadContent(output)

    }

    @Override
    void setIpList(final ObservableList<Location> ipList) {
        ipTable.items = ipList
    }

    @Override
    void sortMainTable() {
        findingColumn.sortable = true
        findingsTable.sortOrder.clear()
        findingsTable.sortOrder.addAll(riskColumn)
        findingsTable.sortOrder.addAll(findingColumn)
        findingsTable.sort()
        findingColumn.sortable = false
    }

    @Override
    void setTableSelectionMode(final SelectionMode mode) {
        findingsTable.selectionModel.selectionMode = mode
        ipTable.selectionModel.selectionMode = mode
    }

    @Override
    void setTableCellFactory(final Callback<TableColumn<Finding, String>, TableCell<Finding, String>> call) {
        findingColumn.cellFactory = call
    }

    void changeTab() {
        presenter.changeTab()
    }

    void setClipboardContent(final ClipboardContent content) {
        Clipboard.systemClipboard.content = (content)
    }

    @Override
    ObservableList<Finding> getSelectedFindingsList() {
        return (findingsTable.selectionModel.selectedItems as ObservableList<Finding>)
    }
}
