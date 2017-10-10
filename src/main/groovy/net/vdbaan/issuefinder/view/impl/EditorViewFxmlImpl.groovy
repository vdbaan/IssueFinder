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
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.EditorPresenter
import net.vdbaan.issuefinder.view.EditorView

@CompileStatic
class EditorViewFxmlImpl extends AbstractEditorView implements EditorView {
    @FXML
    TextField editScanner
    @FXML
    TextField editHostname
    @FXML
    TextField editIp
    @FXML
    TextField editPort
    @FXML
    TextField editService
    @FXML
    TextField editPlugin
    @FXML
    ChoiceBox editRisk


    @FXML
    void initialize() {
        presenter = new EditorPresenter(this)
    }

    @FXML
    void ok(ActionEvent event) {
        okAction?.handle(event)
    }

    @FXML
    void cancel(ActionEvent event) {
        cancelAction?.handle(event)
    }

    String getEditScanner() {
        return editScanner.text
    }

    void setEditScanner(String editScanner) {
        this.editScanner.text = editScanner
    }

    String getEditHostname() {
        return editHostname.text
    }

    void setEditHostname(String editHostname) {
        this.editHostname.text = editHostname
    }

    String getEditIp() {
        return editIp.text
    }

    void setEditIp(String editIp) {
        this.editIp.text = editIp
    }

    String getEditPort() {
        return editPort.text
    }

    void setEditPort(String editPort) {
        this.editPort.text = editPort
    }

    String getEditService() {
        return editService.text
    }

    void setEditService(String editService) {
        this.editService.text = editService
    }

    String getEditPlugin() {
        return editPlugin.text
    }

    void setEditPlugin(String editPlugin) {
        this.editPlugin.text = editPlugin
    }

    Finding.Severity getEditRisk() {
        return editRisk.getValue()
    }

    void setEditRisk(Finding.Severity editRisk) {
        this.editRisk.value = Finding.Severity
    }

    void setEditRiskOptions(List<Finding.Severity> options) {
        this.editRisk.items.add(options)
    }


    void setEditData(List<Finding> data) {
        presenter.setEditData(data)
    }
}

abstract class AbstractEditorView extends BaseViewImpl implements EditorView {
    EditorPresenter presenter

    private EventHandler<ActionEvent> okAction
    private EventHandler<ActionEvent> cancelAction

    EventHandler<ActionEvent> getOkAction() {
        return okAction
    }

    void setOkAction(EventHandler<ActionEvent> okAction) {
        this.okAction = okAction
    }

    EventHandler<ActionEvent> getCancelAction() {
        return cancelAction
    }

    void setCancelAction(EventHandler<ActionEvent> cancelAction) {
        this.cancelAction = cancelAction
    }
}