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

package net.vdbaan.issuefinder.presenter.mock

import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.stage.Window
import javafx.util.Callback
import net.vdbaan.issuefinder.presenter.MainPresenter
import net.vdbaan.issuefinder.util.RetentionFileChooser
import net.vdbaan.issuefinder.view.MainView
import net.vdbaan.issuefinder.view.ProgressDialogView

import javax.swing.text.html.ListView

class MainViewMock implements MainView {

    MainPresenter presenter

    List<File> progressFileList
    MainViewMock() {
        presenter = new MainPresenter(this)
        presenter.progressDialog = new Stage() {
            @Override
            void showAndWait() {

            }
        }
        presenter.progressController = new ProgressDialogView() {

            @Override
            void setFileList(List<File> files) {
                progressFileList = files
            }

            @Override
            void setMasterView(MainView mainView) {

            }

            @Override
            void setCallback(Closure callback) {

            }
        }
    }


    @Override
    RetentionFileChooser getRetentionFileChooser() {
        return null
    }

    @Override
    void newAction(ActionEvent event) {
        presenter.newAction(null)

    }

    @Override
    void loadReport(ActionEvent event) {
        presenter.loadReport(null)
    }

    @Override
    void saveDatabase(ActionEvent event) {
        presenter.saveDatabase(null)
    }

    @Override
    void loadDatabase(ActionEvent event) {
        presenter.loadDatabase(null)
    }

    @Override
    void showPreferences(ActionEvent event) {
        presenter.showPreferences(null)
    }

    @Override
    void exitApplication(ActionEvent event) {
        presenter.exitApplication(null)
    }

    @Override
    void showHelp(ActionEvent event) {
        presenter.showHelp(null)
    }

    @Override
    void showStatistics(ActionEvent event) {
        presenter.showStatistics(null)
    }

    @Override
    void showAbout(ActionEvent event) {
        presenter.showAbout(null)
    }

    @Override
    void filterClicked(MouseEvent event) {
        presenter.filterClicked(null)
    }

    @Override
    void doFilter(ActionEvent event) {
        presenter.doFilter(null)
    }

    @Override
    void clearFilter(ActionEvent event) {
        presenter.clearFilter(null)
    }

    @Override
    void copyUniqueIPs(ActionEvent event) {
        presenter.copyUniqueIPs(null)
    }

    @Override
    void copyUniquePortAndIPs(ActionEvent event) {
        presenter.copyUniquePortAndIPs(null)
    }

    @Override
    def tabChanged(Event event) {
        return null
    }

    @Override
    void onAction(ActionEvent event) {

    }

    @Override
    void handleReturn(KeyEvent event) {

    }

    @Override
    Window getWindow() {
        return null
    }

    @Override
    void setNewActionHandler(EventHandler<ActionEvent> newActionHandler) {

    }

    @Override
    void setLoadReportHandler(EventHandler<ActionEvent> loadReportHandler) {

    }

    @Override
    void setSaveDatabaseHandler(EventHandler<ActionEvent> saveDatabaseHandler) {

    }

    @Override
    void setLoadDatabaseHandler(EventHandler<ActionEvent> loadDatabaseHandler) {

    }

    @Override
    void setShowPreferencesHandler(EventHandler<ActionEvent> showPreferencesHandler) {

    }

    @Override
    void setExitApplicationHandler(EventHandler<ActionEvent> exitApplicationHandler) {

    }

    @Override
    void setShowHelpHandler(EventHandler<ActionEvent> showHelpHandler) {

    }

    @Override
    void setShowStatisticsHandler(EventHandler<ActionEvent> showStatisticsHandler) {

    }

    @Override
    void setShowAboutHandler(EventHandler<ActionEvent> showAboutHandler) {

    }

    @Override
    void setFilterClickedHandler(EventHandler<MouseEvent> filterClickedHandler) {

    }

    @Override
    void setDoFilterHandler(EventHandler<ActionEvent> doFilterHandler) {

    }

    @Override
    void setClearFilterHandler(EventHandler<ActionEvent> clearFilterHandler) {

    }

    @Override
    void setCopyUniqueIPsHandler(EventHandler<ActionEvent> copyUniqueIPsHandler) {

    }

    @Override
    void setCopyUniquePortAndIPsHandler(EventHandler<ActionEvent> copyUniquePortAndIPsHandler) {

    }

    @Override
    void setTabChangedHandler(EventHandler<Event> tabChangedHandler) {

    }

    @Override
    ObservableList<String> getFilterTextStyleClass() {
        return null
    }

    @Override
    Object getFilterTextValue() {
        return null
    }

    @Override
    void setFilterText(Object text) {

    }

    @Override
    void setFilterTextItems(List<String> items) {

    }

    @Override
    List<String> getFilterTextItems() {
        return null
    }

    @Override
    void setStatusLabel(String label) {

    }

    @Override
    void setRowInfoLabel(String label) {

    }

    @Override
    void setIpInfoLabel(String label) {

    }

    @Override
    void selectSelectIssueTab() {

    }

    @Override
    void disableFindingsTab() {

    }

    @Override
    void enableFindingsTab() {

    }

    @Override
    def addFilterTextListener(ChangeListener listener) {
        return null
    }

    @Override
    void setOnActionHandler(EventHandler<ActionEvent> onChangedHandler) {

    }

    @Override
    void setHandleReturnHandler(EventHandler<KeyEvent> handleReturnHandler) {

    }

    @Override
    void refresh() {

    }

    @Override
    void setComboCellFactory(Callback<ListView, ListCell> cellFactory) {

    }

    @Override
    ComboBox getMainFilter() {
        return new ComboBox()
    }
}
