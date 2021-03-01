/*
 *  Copyright (C) 2017  S. van der Baan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder.view.impl

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.SettingsDialogView


class SettingsDialogController implements SettingsDialogView {

    @FXML
    // fx:id="filterList"
    private ListView<?> filterList // Value injected by FXMLLoader

    @FXML
    // fx:id="isCritical"
    private CheckBox isCritical // Value injected by FXMLLoader

    @FXML
    // fx:id="isHigh"
    private CheckBox isHigh // Value injected by FXMLLoader

    @FXML
    // fx:id="isMedium"
    private CheckBox isMedium // Value injected by FXMLLoader

    @FXML
    // fx:id="isLow"
    private CheckBox isLow // Value injected by FXMLLoader

    @FXML
    // fx:id="isInfo"
    private CheckBox isInfo // Value injected by FXMLLoader

    @FXML
    // fx:id="batchSize"
    private TextField batchSize // Value injected by FXMLLoader

    @FXML
    // fx:id="maxRows"
    private TextField maxRows // Value injected by FXMLLoader

    @FXML
    // fx:id="filterText"
    private TextField filterText // Value injected by FXMLLoader

    @FXML
    // fx:id="copyString"
    private TextField copyString // Value injected by FXMLLoader

    @FXML
    // fx:id="coloredRows"
    private CheckBox coloredRows // Value injected by FXMLLoader

    @FXML
    void addFilter(final ActionEvent event) {
        if (filterText.text != '') {
            filterList.items.add(filterText.text)
        }
    }

    @FXML
    void deleteFilter(final ActionEvent event) {
        filterList.items.remove(filterList.selectionModel.selectedItem)
    }

    private boolean inEditMode
    @FXML
    void editFilter(final ActionEvent event) {
        filterText.text = (filterList.selectionModel.selectedItem as String)
        inEditMode = true
    }

    private void focusListener(ObservableValue value, Boolean oldValue, Boolean newValue) {
        if(inEditMode && newValue == false) { // in edit mode and element looses focus
            String orig = filterList.selectionModel.selectedItem as String
            int index = filterList.selectionModel.selectedIndices.get(0)
            filterList.items.set(index,filterText.text)
            inEditMode = false
            filterText.text = ''
        }
    }

    @FXML
    void initialize() {
        maxRows.text = (Config.instance.getProperty(Config.MAX_ROWS) as String)
        batchSize.text = (Config.instance.getProperty(Config.BATCH_SIZE) as String)
        copyString.text = (Config.instance.getProperty(Config.IP_PORT_FORMAT_STRING) as String)
        final Map<Finding.Severity, Boolean> loadFilter = (Map) Config.instance.getProperty(Config.PRELOAD_FILTER)
        isCritical.selected = loadFilter[Finding.Severity.CRITICAL]
        isHigh.selected = loadFilter[Finding.Severity.HIGH]
        isMedium.selected = loadFilter[Finding.Severity.MEDIUM]
        isLow.selected = loadFilter[Finding.Severity.LOW]
        isInfo.selected = loadFilter[Finding.Severity.INFO]
        filterList.items.addAll(Config.instance.getProperty(Config.FILTERS))
        filterText.focusedProperty().addListener(this.&focusListener as ChangeListener)
    }

    void storePreferences() {
        final Map<Finding.Severity, Boolean> loadfilter = new HashMap()
        loadfilter[Finding.Severity.CRITICAL] = isCritical.selected
        loadfilter[Finding.Severity.HIGH] = isHigh.selected
        loadfilter[Finding.Severity.MEDIUM] = isMedium.selected
        loadfilter[Finding.Severity.LOW] = isLow.selected
        loadfilter[Finding.Severity.INFO] = isInfo.selected
        Config.instance.setProperty(Config.PRELOAD_FILTER, loadfilter)

        Config.instance.setProperty(Config.COLOURED_ROWS, coloredRows.selected)
        Config.instance.setProperty(Config.MAX_ROWS, maxRows.text as Integer)
        Config.instance.setProperty(Config.BATCH_SIZE, batchSize.text as Integer)
        Config.instance.setProperty(Config.IP_PORT_FORMAT_STRING, copyString.text)
        Config.instance.setProperty(Config.FILTERS, filterList.items)
    }
}
