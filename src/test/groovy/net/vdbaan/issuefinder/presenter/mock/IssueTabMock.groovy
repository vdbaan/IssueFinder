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
import javafx.event.EventHandler
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.input.ClipboardContent
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.IssueTabPresenter
import net.vdbaan.issuefinder.presenter.Location
import net.vdbaan.issuefinder.view.IssueTabView

class IssueTabMock implements IssueTabView {

    IssueTabPresenter presenter

    IssueTabMock() {
        presenter = new IssueTabPresenter(this)
    }

    @Override
    void copySelectedIps(ActionEvent event) {

    }

    @Override
    void copySelectedPortsAndIps(ActionEvent event) {

    }

    @Override
    void bindMasterData(ObservableList<Finding> masterData) {

    }

    @Override
    void setSelectItemPropertyListener(ChangeListener listener) {

    }

    @Override
    void setMainTableItems(ObservableList<Finding> data) {

    }

    @Override
    void setDescription(String description) {

    }

    @Override
    void setRemediation(String remediation) {

    }

    @Override
    void setOutput(String output) {

    }

    @Override
    void setIpList(ObservableList<Location> iPs) {

    }

    @Override
    void sortMainTable() {

    }

    @Override
    void setTableSelectionMode(SelectionMode selectionMode) {

    }

    @Override
    void setCopySelectedIpsHandler(EventHandler<ActionEvent> copySelectedIpsHandler) {

    }

    @Override
    void setCopySelectedPortsAndIpsHandler(EventHandler<ActionEvent> copySelectedPortsAndIpsHandler) {

    }

    @Override
    void setTableCellFactory(Callback<TableColumn<Finding, String>, TableCell<Finding, String>> call) {

    }

    @Override
    void setClipboardContent(ClipboardContent content) {

    }
}
