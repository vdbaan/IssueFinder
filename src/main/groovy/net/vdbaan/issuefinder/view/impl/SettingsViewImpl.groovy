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
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import net.vdbaan.issuefinder.presenter.SettingsPresenter
import net.vdbaan.issuefinder.view.SettingsView

@CompileStatic
class SettingsViewImpl extends AbstractSettingsView implements SettingsView {
    @FXML
    ListView filterList

    @FXML
    CheckBox isCritical
    @FXML
    CheckBox isHigh
    @FXML
    CheckBox isMedium
    @FXML
    CheckBox isLow
    @FXML
    CheckBox isInfo

    @FXML
    TextField filterText
    @FXML
    TextField batchSize
    @FXML
    TextField maxRows
    @FXML
    TextField copyString

    @FXML
    CheckBox coloredRows


    @FXML
    void initialize() {
        presenter = new SettingsPresenter(this)
    }

    @FXML
    void addFilter(final ActionEvent event) {
        addFilterAction?.handle(event)
    }

    @FXML
    void cancelAll(final ActionEvent event) {
        cancelAllAction?.handle(event)
    }

    @FXML
    void deleteFilter(final ActionEvent event) {
        deleteFilterAction?.handle(event)
    }

    @FXML
    void editFilter(final ActionEvent event) {
        editFilterAction?.handle(event)
    }

    @FXML
    void saveAll(final ActionEvent event) {
        saveAllAction?.handle(event)
    }

    void removeListItems() {
        filterList.items.removeAll(filterList.items)
    }

    List getAllItems() {
        return filterList.items
    }

    void addToListItems(final List items) {
        filterList.items.addAll(items)
    }

    void addToListItems(final String item) {
        filterList.items << item
    }

    void removeItem(final String item) {
        filterList.items.remove(item)
    }

    String getSelectedListItem() {
        return filterList.getSelectionModel().getSelectedItem()
    }

    boolean getIsCritical() {
        return isCritical.selected
    }

    void setIsCritical(final boolean isCritical) {
        this.isCritical.selected = isCritical
    }

    boolean getIsHigh() {
        return isHigh.selected
    }

    void setIsHigh(final boolean isHigh) {
        this.isHigh.selected = isHigh
    }

    boolean getIsMedium() {
        return isMedium.selected
    }

    void setIsMedium(final boolean isMedium) {
        this.isMedium.selected = isMedium
    }

    boolean getIsLow() {
        return isLow.selected
    }

    void setIsLow(final boolean isLow) {
        this.isLow.selected = isLow
    }

    boolean getIsInfo() {
        return isInfo.selected
    }

    void setIsInfo(final boolean isInfo) {
        this.isInfo.selected = isInfo
    }

    String getFilterText() {
        return filterText.text
    }

    void setFilterText(final String filterText) {
        this.filterText.text = filterText
    }

    String getBatchSize() {
        return batchSize.text
    }

    void setBatchSize(final String batchSize) {
        this.batchSize.text = batchSize
    }

    String getMaxRows() {
        return maxRows.text
    }

    void setMaxRows(final String maxRows) {
        this.maxRows.text = maxRows
    }

    String getCopyString() {
        return copyString.text
    }

    void setCopyString(final String copyString) {
        this.copyString.text = copyString
    }

    @Override
    boolean isColouredRow() {
        return coloredRows.selected
    }

    @Override
    void setColouredRow(final boolean coloured) {
        coloredRows.selected = coloured
    }
}

abstract class AbstractSettingsView extends BaseViewImpl implements SettingsView {
    SettingsPresenter presenter

    private EventHandler<ActionEvent> addFilterAction
    private EventHandler<ActionEvent> cancelAllAction
    private EventHandler<ActionEvent> deleteFilterAction
    private EventHandler<ActionEvent> editFilterAction
    private EventHandler<ActionEvent> saveAllAction

    EventHandler<ActionEvent> getAddFilterAction() {
        return addFilterAction
    }

    void setAddFilterAction(final EventHandler<ActionEvent> addFilterAction) {
        this.addFilterAction = addFilterAction
    }

    EventHandler<ActionEvent> getCancelAllAction() {
        return cancelAllAction
    }

    void setCancelAllAction(final EventHandler<ActionEvent> cancelAllAction) {
        this.cancelAllAction = cancelAllAction
    }

    EventHandler<ActionEvent> getDeleteFilterAction() {
        return deleteFilterAction
    }

    void setDeleteFilterAction(final EventHandler<ActionEvent> deleteFilterAction) {
        this.deleteFilterAction = deleteFilterAction
    }

    EventHandler<ActionEvent> getEditFilterAction() {
        return editFilterAction
    }

    void setEditFilterAction(final EventHandler<ActionEvent> editFilterAction) {
        this.editFilterAction = editFilterAction
    }

    EventHandler<ActionEvent> getSaveAllAction() {
        return saveAllAction
    }

    void setSaveAllAction(final EventHandler<ActionEvent> saveAllAction) {
        this.saveAllAction = saveAllAction
    }
}