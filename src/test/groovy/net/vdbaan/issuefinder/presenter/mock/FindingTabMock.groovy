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

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.SelectionMode
import javafx.scene.input.ClipboardContent
import javafx.stage.Window
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.FindingTabPresenter
import net.vdbaan.issuefinder.view.FindingTabView

class FindingTabMock implements FindingTabView {
    FindingTabPresenter presenter

    FindingTabMock() {
        presenter = new FindingTabPresenter(this)
    }

    @Override
    void copySelectedIps(ActionEvent event) {
        presenter.copySelectedIps(null)

    }

    @Override
    void copySelectedPortsAndIps(ActionEvent event) {
        presenter.copySelectedPortsAndIps(null)
    }

    @Override
    void filterOnIP(ActionEvent event) {
        presenter.filterOnIP(null)
    }

    @Override
    void filterOnPlugin(ActionEvent event) {
        presenter.filterOnPlugin(null)
    }

    @Override
    void filterOnPort(ActionEvent event) {
        presenter.filterOnPort(null)
    }

    @Override
    void filterOnProtocol(ActionEvent event) {
        presenter.filterOnProtocol(null)
    }

    @Override
    void filterOnPortStatus(ActionEvent event) {
        presenter.filterOnPortStatus(null)
    }

    @Override
    void filterOnRisk(ActionEvent event) {
        presenter.filterOnRisk(null)
    }

    @Override
    void filterOnScanner(ActionEvent event) {
        presenter.filterOnScanner(null)
    }

    @Override
    void filterOnService(ActionEvent event) {
        presenter.filterOnService(null)
    }

    @Override
    void editSelection(ActionEvent event) {
        presenter.editSelection(null)
    }

    @Override
    void setCopySelectedIpsHandler(EventHandler<ActionEvent> copySelectedIpsHandler) {

    }

    @Override
    void setCopySelectedPortsAndIpsHandler(EventHandler<ActionEvent> copySelectedPortsAndIpsHandler) {

    }

    @Override
    void setFilterOnIPHandler(EventHandler<ActionEvent> filterOnIPHandler) {

    }

    @Override
    void setFilterOnPluginHandler(EventHandler<ActionEvent> filterOnPluginHandler) {

    }

    @Override
    void setFilterOnPortHandler(EventHandler<ActionEvent> filterOnPortHandler) {

    }

    @Override
    void setFilterOnProtocolHandler(EventHandler<ActionEvent> filterOnProtocolHandler) {

    }

    @Override
    void setFilterOnPortStatusHandler(EventHandler<ActionEvent> filterOnPortStatusHandler) {

    }

    @Override
    void setFilterOnRiskHandler(EventHandler<ActionEvent> filterOnRiskHandler) {

    }

    @Override
    void setFilterOnScannerHandler(EventHandler<ActionEvent> filterOnScannerHandler) {

    }

    @Override
    void setFilterOnServiceHandler(EventHandler<ActionEvent> filterOnServiceHandler) {

    }

    @Override
    void setEditSelectionHandler(EventHandler<ActionEvent> editSelectionHandler) {

    }

    @Override
    ReadOnlyObjectProperty<Comparator> getTableComparatorProperty() {
        return null
    }

    @Override
    void setMainTableItems(ObservableList items) {

    }

    @Override
    void setSelectItemPropertyListener(ChangeListener listener) {

    }

    @Override
    void setTableSelectionMode(SelectionMode mode) {

    }

    @Override
    void textArealoadContent(String content) {

    }

    @Override
    void copyUniqueIPs(ObservableList<Finding> data) {

    }

    @Override
    void copyUniquePortAndIPs(ObservableList<Finding> data) {

    }

    @Override
    void setMainFilterText(String text) {

    }

    @Override
    Finding getSelectedFinding() {
        return null
    }

    @Override
    ObservableList<Finding> getSelectedFindingsList() {
        return null
    }

    @Override
    void setClipboardContent(ClipboardContent content) {

    }

    @Override
    Window getWindow() {
        return null
    }

    @Override
    void refresh() {

    }

    @Override
    void setRiskColumnCellFactory(Callback callback) {

    }
}
