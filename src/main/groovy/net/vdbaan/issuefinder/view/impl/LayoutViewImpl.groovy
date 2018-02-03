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
package net.vdbaan.issuefinder.view.impl

import groovy.transform.CompileStatic
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.MouseEvent
import javafx.scene.web.WebView
import javafx.stage.Window
import net.vdbaan.issuefinder.MainApp
import net.vdbaan.issuefinder.db.DbListener
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.LayoutPresenter
import net.vdbaan.issuefinder.util.RetentionFileChooser
import net.vdbaan.issuefinder.util.TableUtils
import net.vdbaan.issuefinder.view.LayoutView

@CompileStatic
class LayoutViewImpl extends AbstractLayoutView implements LayoutView, DbListener {

    @FXML
    // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources

    @FXML
    // URL location of the FXML file that was given to the FXMLLoader
    private URL location

    @FXML
    // fx:id="filterText"
    private ComboBox<String> filterText // Value injected by FXMLLoader

    @FXML
    // fx:id="filterButton"
    private Button filterButton // Value injected by FXMLLoader

    @FXML
    // fx:id="clearButton"
    private Button clearButton // Value injected by FXMLLoader

    @FXML
    // fx:id="mainTable"
    private TableView<?> mainTable // Value injected by FXMLLoader

    @FXML
    // fx:id="textArea"
    private WebView textArea // Value injected by FXMLLoader

    @FXML
    // fx:id="statusLabel"
    private Label statusLabel // Value injected by FXMLLoader

    @FXML
    // fx:id="rowInfoLabel"
    private Label rowInfoLabel // Value injected by FXMLLoader

    @FXML
    // fx:id="ipInfoLabel"
    private Label ipInfoLabel // Value injected by FXMLLoader

    @FXML
    // fx:id="webScroll
    private ScrollPane webScroll

    @FXML
    void aboutHelp(final ActionEvent event) {
        aboutHelpAction?.handle(event)
    }

    @FXML
    void aboutAction(final ActionEvent event) {
        aboutAction?.handle(event)
    }

    @FXML
    void buildSummary(final ActionEvent event) {
        summaryAction?.handle(event)
    }

    @FXML
    void clearFilter(final ActionEvent event) {
        clearAction?.handle(event)
    }

    @FXML
    void closeAction(final ActionEvent event) {
        closeAction?.handle(event)
    }

    @FXML
    void loadAction(final ActionEvent event) {
        loadAction?.handle(event)
    }

    @FXML
    void saveAction(final ActionEvent event) {
        saveAction?.handle(event)
    }

    @FXML
    void copyUniqueIps(final ActionEvent event) {
        copyIpAction?.handle(event)
    }

    @FXML
    void copyUniquePortsAndIps(final ActionEvent event) {
        copyIpPortAction?.handle(event)
    }

    @FXML
    void exportAction(final ActionEvent event) {
        exportAction?.handle(event)
    }

    @FXML
    void filterOnIp(final ActionEvent event) {
        filterIpAction?.handle(event)
    }

    @FXML
    void filterOnPlugin(final ActionEvent event) {
        filterPluginAction?.handle(event)
    }

    @FXML
    void filterOnPort(final ActionEvent event) {
        filterPortAction?.handle(event)
    }

    @FXML
    void filterOnService(final ActionEvent event) {
        filterServiceAction?.handle(event)
    }

    @FXML
    void filterOnScanner(final ActionEvent event) {
        filterOnScannerAction?.handle(event)
    }

    @FXML
    void filterOnPortStatus(final ActionEvent event) {
        filterOnPortStatusAction?.handle(event)
    }

    @FXML
    void filterOnProtocol(final ActionEvent event) {
        filterOnProtocolAction?.handle(event)
    }

    @FXML
    void filterOnRisk(final ActionEvent event) {
        filterOnRiskAction?.handle(event)
    }

    @FXML
    void filterTable(final ActionEvent event) {
        filterTableAction?.handle(event)
    }

    @FXML
    void modifyEntry(final ActionEvent event) {
        modifyAction?.handle(event)
    }

    @FXML
    void newAction(final ActionEvent event) {
        newAction?.handle(event)
    }

    @FXML
    void openAction(final ActionEvent event) {
        openAction?.handle(event)
    }

    @FXML
    void openSettings(final ActionEvent event) {
        settingsAction?.handle(event)
    }

    @FXML
    void filterTextMouseClicked(final MouseEvent event) {
        filterTextMouseAction?.handle(event)
    }

    @Override
    void copySelectedIps(final ActionEvent event) {
        copySelectedIpsAction?.handle(event)
    }

    @Override
    void copySelectedIpsPorts(final ActionEvent event) {
        copySelectedIpsPortsAction?.handle(event)
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        presenter = new LayoutPresenter(this)
//        mainTable.getSelectionModel().setCellSelectionEnabled(true)
        TableUtils.installCopyPasteHandler(mainTable)
    }

    @Override
    void setStatusLabel(final String text) {
        this.statusLabel.text = text
    }

    void setRowInfoLabel(final String text) {
        this.rowInfoLabel.text = text
    }

    void setIpInfoLabel(final String text) {
        this.ipInfoLabel.text = text
    }


    void setFilterTextItems(final List<String> items) {
        filterText.items?.clear()
        filterText.items.addAll(items)
    }

    List<String> getFilterTextItems() {
        return filterText.getItems()
    }

    void setFilterText(final String text) {
        filterText.setValue(text)
    }

    void addFiterTextListener(final ChangeListener listener) {
        filterText.valueProperty().addListener(listener)
    }

    void setMainTableItems(final ObservableList items) {
        mainTable.setItems(items)
    }

    void setSelectItemPropertyListener(final ChangeListener listener) {
        mainTable.getSelectionModel().selectedItemProperty().addListener(listener)
    }

    void setTableSelectionMode(final SelectionMode mode) {
        mainTable.getSelectionModel().setSelectionMode(mode)
    }

    ObservableList<String> getFilterTextStyleClass() {
        return filterText.getEditor().getStyleClass()
    }

    void addFilterTextMouseClicked(final EventHandler handler) {
        filterText.getEditor().setOnMouseClicked(handler)
    }

    ObjectProperty<EventHandler> getTableKeyEvent() {
        null
//        return mainTable.getOnKeyPressed()
    }


    void textArealoadContent(final String content) {
        textArea.getEngine().loadContent(content)
    }

    Finding getSelectedFinding() {
        return (Finding) mainTable.selectionModel.getSelectedItem()
    }

    Window getWindow() {
        root.getScene().getWindow()
    }

    List<Finding> getSelectedFindings() {
        return (List<Finding>) mainTable.selectionModel.getSelectedItems()
    }

    String getFilterTextValue() {
        return filterText.getValue()
    }


    ReadOnlyObjectProperty<Comparator> getTableComparatorProperty() {
        return mainTable.comparatorProperty()
    }


    void addFilterTextItem(final String item) {
        filterText.items.add(item)
    }
}

abstract class AbstractLayoutView extends BaseViewImpl implements LayoutView {
    LayoutPresenter presenter

    private EventHandler<ActionEvent> aboutAction
    private EventHandler<ActionEvent> aboutHelpAction
    private EventHandler<ActionEvent> summaryAction
    private EventHandler<ActionEvent> clearAction
    private EventHandler<ActionEvent> closeAction
    private EventHandler<ActionEvent> copyIpAction
    private EventHandler<ActionEvent> copyIpPortAction
    private EventHandler<ActionEvent> exportAction
    private EventHandler<ActionEvent> filterIpAction
    private EventHandler<ActionEvent> filterPortAction
    private EventHandler<ActionEvent> filterPluginAction
    private EventHandler<ActionEvent> filterServiceAction
    private EventHandler<ActionEvent> filterOnScannerAction
    private EventHandler<ActionEvent> filterOnPortStatusAction
    private EventHandler<ActionEvent> filterOnProtocolAction
    private EventHandler<ActionEvent> filterOnRiskAction
    private EventHandler<ActionEvent> settingsAction
    private EventHandler<ActionEvent> openAction
    private EventHandler<ActionEvent> newAction
    private EventHandler<ActionEvent> loadAction
    private EventHandler<ActionEvent> saveAction
    private EventHandler<ActionEvent> modifyAction
    private EventHandler<ActionEvent> filterTableAction
    private EventHandler<ActionEvent> copySelectedIpsAction
    private EventHandler<ActionEvent> copySelectedIpsPortsAction
    private EventHandler<MouseEvent> filterTextMouseAction

    RetentionFileChooser retentionFileChooser = new RetentionFileChooser()

    EventHandler<ActionEvent> getAboutAction() {
        return aboutAction
    }

    void setAboutHelpAction(final EventHandler<ActionEvent> aboutHelpAction) {
        this.aboutHelpAction = aboutHelpAction
    }

    EventHandler<ActionEvent> getAboutHelpAction() {
        return aboutHelpAction
    }

    void setAboutAction(final EventHandler<ActionEvent> aboutAction) {
        this.aboutAction = aboutAction
    }

    EventHandler<ActionEvent> getSummaryAction() {
        return summaryAction
    }

    void setSummaryAction(final EventHandler<ActionEvent> summaryAction) {
        this.summaryAction = summaryAction
    }

    EventHandler<ActionEvent> getClearAction() {
        return clearAction
    }

    void setClearAction(final EventHandler<ActionEvent> clearAction) {
        this.clearAction = clearAction
    }

    EventHandler<ActionEvent> getCloseAction() {
        return closeAction
    }

    void setCloseAction(final EventHandler<ActionEvent> closeAction) {
        this.closeAction = closeAction
    }

    EventHandler<ActionEvent> getCopyIpAction() {
        return copyIpAction
    }

    void setCopyIpAction(final EventHandler<ActionEvent> copyIpAction) {
        this.copyIpAction = copyIpAction
    }

    EventHandler<ActionEvent> getCopyIpPortAction() {
        return copyIpPortAction
    }

    void setCopyIpPortAction(final EventHandler<ActionEvent> copyIpPortAction) {
        this.copyIpPortAction = copyIpPortAction
    }

    EventHandler<ActionEvent> getExportAction() {
        return exportAction
    }

    void setExportAction(final EventHandler<ActionEvent> exportAction) {
        this.exportAction = exportAction
    }

    EventHandler<ActionEvent> getFilterIpAction() {
        return filterIpAction
    }

    void setFilterIpAction(final EventHandler<ActionEvent> filterIpAction) {
        this.filterIpAction = filterIpAction
    }

    EventHandler<ActionEvent> getFilterPortAction() {
        return filterPortAction
    }

    void setFilterPortAction(final EventHandler<ActionEvent> filterPortAction) {
        this.filterPortAction = filterPortAction
    }

    EventHandler<ActionEvent> getFilterPluginAction() {
        return filterPluginAction
    }

    void setFilterPluginAction(final EventHandler<ActionEvent> filterPluginAction) {
        this.filterPluginAction = filterPluginAction
    }

    EventHandler<ActionEvent> getFilterServiceAction() {
        return filterServiceAction
    }

    void setFilterServiceAction(final EventHandler<ActionEvent> filterServiceAction) {
        this.filterServiceAction = filterServiceAction
    }

    EventHandler<ActionEvent> getSettingsAction() {
        return settingsAction
    }

    void setSettingsAction(final EventHandler<ActionEvent> settingsAction) {
        this.settingsAction = settingsAction
    }

    EventHandler<ActionEvent> getOpenAction() {
        return openAction
    }

    void setOpenAction(final EventHandler<ActionEvent> openAction) {
        this.openAction = openAction
    }

    EventHandler<ActionEvent> getNewAction() {
        return newAction
    }

    void setNewAction(final EventHandler<ActionEvent> newAction) {
        this.newAction = newAction
    }

    void setLoadAction(final EventHandler<ActionEvent> loadAction) {
        this.loadAction = loadAction
    }

    EventHandler<ActionEvent> getLoadAction() {
        return loadAction
    }

    void setSaveAction(final EventHandler<ActionEvent> saveAction) {
        this.saveAction = saveAction
    }

    EventHandler<ActionEvent> getSaveAction() {
        return saveAction
    }

    EventHandler<ActionEvent> getModifyAction() {
        return modifyAction
    }

    void setModifyAction(final EventHandler<ActionEvent> modifyAction) {
        this.modifyAction = modifyAction
    }

    EventHandler<ActionEvent> getFilterTableAction() {
        return filterTableAction
    }

    void setFilterTableAction(final EventHandler<ActionEvent> filterTable) {
        this.filterTableAction = filterTable
    }

    EventHandler<MouseEvent> getFilterTextMouseAction() {
        return filterTextMouseAction
    }

    void setFilterTextMouseAction(final EventHandler<MouseEvent> filterTextMouseAction) {
        this.filterTextMouseAction = filterTextMouseAction
    }

    EventHandler<ActionEvent> getCopySelectedIpsAction() {
        return copySelectedIpsAction
    }

    void setCopySelectedIpsAction(final EventHandler<ActionEvent> copySelectedIpsAction) {
        this.copySelectedIpsAction = copySelectedIpsAction
    }

    EventHandler<ActionEvent> getCopySelectedIpsPortsAction() {
        return copySelectedIpsPortsAction
    }

    void setCopySelectedIpsPortsAction(final EventHandler<ActionEvent> copySelectedIpsPortsAction) {
        this.copySelectedIpsPortsAction = copySelectedIpsPortsAction
    }

    LayoutPresenter getPresenter() {
        return presenter
    }

    void setPresenter(final LayoutPresenter presenter) {
        this.presenter = presenter
    }

    EventHandler<ActionEvent> getFilterOnScannerAction() {
        return filterOnScannerAction
    }

    void setFilterOnScannerAction(final EventHandler<ActionEvent> filterOnScannerAction) {
        this.filterOnScannerAction = filterOnScannerAction
    }

    EventHandler<ActionEvent> getFilterOnPortStatusAction() {
        return filterOnPortStatusAction
    }

    void setFilterOnPortStatusAction(final EventHandler<ActionEvent> filterOnPortStatusAction) {
        this.filterOnPortStatusAction = filterOnPortStatusAction
    }

    EventHandler<ActionEvent> getFilterOnProtocolAction() {
        return filterOnProtocolAction
    }

    void setFilterOnProtocolAction(final EventHandler<ActionEvent> filterOnProtocolAction) {
        this.filterOnProtocolAction = filterOnProtocolAction
    }

    EventHandler<ActionEvent> getFilterOnRiskAction() {
        return filterOnRiskAction
    }

    void setFilterOnRiskAction(final EventHandler<ActionEvent> filterOnRiskAction) {
        this.filterOnRiskAction = filterOnRiskAction
    }

    @Override
    void dbUpdated() {
        presenter.dbUpdated()
    }

    void setMainApp(final MainApp mainApp) {
        presenter?.setMainApp(mainApp)
    }

    RetentionFileChooser getRetentionFileChooser() {
        return retentionFileChooser
    }

    void setClipboardContent(final ClipboardContent content) {
        Clipboard.getSystemClipboard().setContent(content)
    }
}