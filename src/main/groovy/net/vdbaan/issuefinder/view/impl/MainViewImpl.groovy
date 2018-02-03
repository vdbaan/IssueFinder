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

import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.stage.Window
import javafx.util.Callback
import net.vdbaan.issuefinder.presenter.MainPresenter
import net.vdbaan.issuefinder.util.RetentionFileChooser
import net.vdbaan.issuefinder.view.MainView

import javax.swing.text.html.ListView

class MainViewImpl implements MainView {
    @FXML
    //fx:id = mainFilter
    private ComboBox mainFilter

    @FXML
    //fx:id = tabs
    private TabPane tabs

    @FXML
    //fx:id = mainStatus
    private Label mainStatus

    @FXML
    // fx:id = mainIssueInfo
    private Label mainIssueInfo

    @FXML
    // fx:id = mainIpInfo
    private Label mainIpInfo

    @FXML
    private SplitPane findingTab
    @FXML
    private FindingTabController findingTabController
    @FXML
    private IssueTabController issueTabController

    @FXML
    void newAction(final ActionEvent event) {
        newActionHandler?.handle(event)
    }

    @FXML
    void loadReport(final ActionEvent event) {
        loadReportHandler?.handle(event)
    }

    @FXML
    void saveDatabase(final ActionEvent event) {
        saveDatabaseHandler?.handle(event)
    }

    @FXML
    void loadDatabase(final ActionEvent event) {
        loadDatabaseHandler?.handle(event)
    }

    @FXML
    void showPreferences(final ActionEvent event) {
        showPreferencesHandler?.handle(event)
    }

    @FXML
    void exitApplication(final ActionEvent event) {
        exitApplicationHandler?.handle(event)
    }

    @FXML
    void showHelp(final ActionEvent event) {
        showHelpHandler?.handle(event)
    }

    @FXML
    void showStatistics(final ActionEvent event) {
        showStatisticsHandler?.handle(event)
    }

    @FXML
    void showAbout(final ActionEvent event) {
        showAboutHandler?.handle(event)
    }

    @FXML
    void filterClicked(final MouseEvent event) {
        filterClickedHandler?.handle(event)
    }


    @FXML
    void doFilter(final ActionEvent event) {
        doFilterHandler?.handle(event)
    }

    @FXML
    void clearFilter(final ActionEvent event) {
        clearFilterHandler?.handle(event)
    }

    @FXML
    void copyUniqueIPs(final ActionEvent event) {
        copyUniqueIPsHandler?.handle(event)
    }

    @FXML
    void copyUniquePortAndIPs(final ActionEvent event) {
        copyUniquePortAndIPsHandler?.handle(event)
    }

    @FXML
    tabChanged(final Event event) {
        tabChangedHandler?.handle(event)
    }

    @FXML
    void onAction(final ActionEvent event) {
        onActionHandler?.handle(event)
    }


    @FXML
    void handleReturn(final KeyEvent event) {
        handleReturnHandler?.handle(event)
    }

    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        presenter = new MainPresenter(this)
        presenter.issuesTabController = findingTabController
        presenter.findingsTabController = issueTabController
    }


    RetentionFileChooser retentionFileChooser = new RetentionFileChooser()

    private MainPresenter presenter

    Window getWindow() {
        return mainFilter.scene.window
    }

    void setNewActionHandler(final EventHandler<ActionEvent> newActionHandler) {
        this.newActionHandler = newActionHandler
    }

    void setLoadReportHandler(final EventHandler<ActionEvent> loadReportHandler) {
        this.loadReportHandler = loadReportHandler
    }

    void setSaveDatabaseHandler(final EventHandler<ActionEvent> saveDatabaseHandler) {
        this.saveDatabaseHandler = saveDatabaseHandler
    }

    void setLoadDatabaseHandler(final EventHandler<ActionEvent> loadDatabaseHandler) {
        this.loadDatabaseHandler = loadDatabaseHandler
    }

    void setShowPreferencesHandler(final EventHandler<ActionEvent> showPreferencesHandler) {
        this.showPreferencesHandler = showPreferencesHandler
    }

    void setExitApplicationHandler(final EventHandler<ActionEvent> exitApplicationHandler) {
        this.exitApplicationHandler = exitApplicationHandler
    }

    void setShowHelpHandler(final EventHandler<ActionEvent> showHelpHandler) {
        this.showHelpHandler = showHelpHandler
    }

    void setShowStatisticsHandler(final EventHandler<ActionEvent> showStatisticsHandler) {
        this.showStatisticsHandler = showStatisticsHandler
    }

    void setShowAboutHandler(final EventHandler<ActionEvent> showAboutHandler) {
        this.showAboutHandler = showAboutHandler
    }

    void setFilterClickedHandler(final EventHandler<MouseEvent> filterClickedHandler) {
        this.filterClickedHandler = filterClickedHandler
    }

    void setDoFilterHandler(final EventHandler<ActionEvent> doFilterHandler) {
        this.doFilterHandler = doFilterHandler
    }

    void setClearFilterHandler(final EventHandler<ActionEvent> clearFilterHandler) {
        this.clearFilterHandler = clearFilterHandler
    }

    void setCopyUniqueIPsHandler(final EventHandler<ActionEvent> copyUniqueIPsHandler) {
        this.copyUniqueIPsHandler = copyUniqueIPsHandler
    }

    void setCopyUniquePortAndIPsHandler(final EventHandler<ActionEvent> copyUniquePortAndIPsHandler) {
        this.copyUniquePortAndIPsHandler = copyUniquePortAndIPsHandler
    }

    void setTabChangedHandler(final EventHandler<Event> tabChangedHandler) {
        this.tabChangedHandler = tabChangedHandler
    }

    void setOnActionHandler(EventHandler<ActionEvent> onActionHandler) {
        this.onActionHandler = onActionHandler
    }


    void setHandleReturnHandler(EventHandler<KeyEvent> handleReturnHandler) {
        this.handleReturnHandler = handleReturnHandler
    }

    @Override
    ObservableList<String> getFilterTextStyleClass() {
        return mainFilter.editor.styleClass
    }

    @Override
    Object getFilterTextValue() {
        return mainFilter.value
    }

    @Override
    void setFilterText(final Object text) {
        mainFilter.value = (text)
    }

    @Override
    void setFilterTextItems(final List<String> items) {
        mainFilter.items?.clear()
        mainFilter.items.addAll(items)
    }

    @Override
    List<String> getFilterTextItems() {
        return mainFilter.items
    }

    @Override
    void setStatusLabel(final String label) {
        mainStatus.text = label

    }

    @Override
    void setRowInfoLabel(final String label) {
        mainIssueInfo.text = label
    }

    @Override
    void setIpInfoLabel(final String label) {
        mainIpInfo.text = label
    }


    RetentionFileChooser getRetentionFileChooser() {
        return retentionFileChooser
    }
    private EventHandler<ActionEvent> newActionHandler
    private EventHandler<ActionEvent> loadReportHandler
    private EventHandler<ActionEvent> saveDatabaseHandler
    private EventHandler<ActionEvent> loadDatabaseHandler
    private EventHandler<ActionEvent> showPreferencesHandler
    private EventHandler<ActionEvent> exitApplicationHandler
    private EventHandler<ActionEvent> showHelpHandler
    private EventHandler<ActionEvent> showStatisticsHandler
    private EventHandler<ActionEvent> showAboutHandler
    private EventHandler<MouseEvent> filterClickedHandler
    private EventHandler<ActionEvent> doFilterHandler
    private EventHandler<ActionEvent> clearFilterHandler
    private EventHandler<ActionEvent> copyUniqueIPsHandler
    private EventHandler<ActionEvent> copyUniquePortAndIPsHandler
    private EventHandler<ActionEvent> onActionHandler
    private EventHandler<KeyEvent> handleReturnHandler
    private EventHandler<Event> tabChangedHandler

    @Override
    void selectSelectIssueTab() {
//        tabs.
        tabs.selectionModel.select(0)
    }

    @Override
    void disableFindingsTab() {
        tabs.tabs[1].disable = true
    }

    @Override
    void enableFindingsTab() {
        tabs.tabs[1].disable = false
    }

    @Override
    def addFilterTextListener(final ChangeListener listener) {
        mainFilter.valueProperty().addListener(listener)
    }

    @Override
    void refresh() {
        presenter.doFilter()
    }

    @Override
    void setComboCellFactory(Callback<ListView, ListCell> cellFactory) {
        mainFilter.setCellFactory(cellFactory)
    }

    void setComboBoxSelectHandler(EventHandler handler) {
        mainFilter.onInputMethodTextChanged = handler
    }

    ComboBox getMainFilter() {
        return mainFilter
    }
}
