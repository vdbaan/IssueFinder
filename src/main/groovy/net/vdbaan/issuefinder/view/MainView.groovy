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

import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.stage.Window
import javafx.util.Callback
import net.vdbaan.issuefinder.util.RetentionFileChooser

import javax.swing.text.html.ListView

interface MainView {

    RetentionFileChooser getRetentionFileChooser()

    @FXML
    void newAction(ActionEvent event)

    @FXML
    void loadReport(ActionEvent event)

    @FXML
    void saveDatabase(ActionEvent event)

    @FXML
    void loadDatabase(ActionEvent event)

    @FXML
    void showPreferences(ActionEvent event)

    @FXML
    void exitApplication(ActionEvent event)

    @FXML
    void showHelp(ActionEvent event)

    @FXML
    void showStatistics(ActionEvent event)

    @FXML
    void showAbout(ActionEvent event)

    @FXML
    void filterClicked(MouseEvent event)

    @FXML
    void doFilter(ActionEvent event)

    @FXML
    void clearFilter(ActionEvent event)

    @FXML
    void copyUniqueIPs(ActionEvent event)

    @FXML
    void copyUniquePortAndIPs(ActionEvent event)

    @FXML
    tabChanged(Event event)

    @FXML
    void onAction(ActionEvent event)

    @FXML
    void handleReturn(KeyEvent event)

    Window getWindow()

    void setNewActionHandler(EventHandler<ActionEvent> newActionHandler)

    void setLoadReportHandler(EventHandler<ActionEvent> loadReportHandler)

    void setSaveDatabaseHandler(EventHandler<ActionEvent> saveDatabaseHandler)

    void setLoadDatabaseHandler(EventHandler<ActionEvent> loadDatabaseHandler)

    void setShowPreferencesHandler(EventHandler<ActionEvent> showPreferencesHandler)

    void setExitApplicationHandler(EventHandler<ActionEvent> exitApplicationHandler)

    void setShowHelpHandler(EventHandler<ActionEvent> showHelpHandler)

    void setShowStatisticsHandler(EventHandler<ActionEvent> showStatisticsHandler)

    void setShowAboutHandler(EventHandler<ActionEvent> showAboutHandler)

    void setFilterClickedHandler(EventHandler<MouseEvent> filterClickedHandler)

    void setDoFilterHandler(EventHandler<ActionEvent> doFilterHandler)

    void setClearFilterHandler(EventHandler<ActionEvent> clearFilterHandler)

    void setCopyUniqueIPsHandler(EventHandler<ActionEvent> copyUniqueIPsHandler)

    void setCopyUniquePortAndIPsHandler(EventHandler<ActionEvent> copyUniquePortAndIPsHandler)

    void setTabChangedHandler(EventHandler<Event> tabChangedHandler)

    ObservableList<String> getFilterTextStyleClass()

    Object getFilterTextValue()

    void setFilterText(Object text)

    void setFilterTextItems(List<String> items)

    List<String> getFilterTextItems()

    void setStatusLabel(String label)

    void setRowInfoLabel(String label)

    void setIpInfoLabel(String label)

    void selectSelectIssueTab()

    void disableFindingsTab()

    void enableFindingsTab()

    def addFilterTextListener(ChangeListener listener)

    void setOnActionHandler(EventHandler<ActionEvent> onChangedHandler)

    void setHandleReturnHandler(EventHandler<KeyEvent> handleReturnHandler)

    void refresh()

    void setComboCellFactory(Callback<ListView, ListCell> cellFactory)

    ComboBox getMainFilter()
}