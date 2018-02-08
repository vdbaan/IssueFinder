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

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.db.DbHandlerImpl
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.EditorDialogView

class EditorDialogController implements EditorDialogView {

    @FXML
    // fx:id="editScanner"
    private TextField editScanner; // Value injected by FXMLLoader

    @FXML
    // fx:id="editIp"
    private TextField editIp; // Value injected by FXMLLoader

    @FXML
    // fx:id="editHostname"
    private TextField editHostname; // Value injected by FXMLLoader

    @FXML
    // fx:id="editProtocol"
    private TextField editProtocol; // Value injected by FXMLLoader

    @FXML
    // fx:id="editService"
    private TextField editService; // Value injected by FXMLLoader

    @FXML
    // fx:id="editPlugin"
    private TextField editPlugin; // Value injected by FXMLLoader

    @FXML
    // fx:id="editStatus"
    private TextField editStatus; // Value injected by FXMLLoader

    @FXML
    // fx:id="editPort"
    private TextField editPort; // Value injected by FXMLLoader

    @FXML
    // fx:id="editCvss"
    private TextField editCvss; // Value injected by FXMLLoader

    @FXML
    // fx:id="editRisk"
    private ChoiceBox<?> editRisk; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        final List<Finding.Severity> values = new ArrayList()
        values.addAll(Finding.Severity.values())
        editRisk.items = FXCollections.observableList(values)
    }

    @Override
    void saveFindings() {
        final DbHandler db = new DbHandlerImpl()
        findings.each { final finding ->
            finding.scanner = editScanner.text ?: finding.scanner
            finding.hostName = editHostname.text ?: finding.hostName
            finding.ip = editIp.text ?: finding.ip
            finding.port = editPort.text ?: finding.port
            finding.protocol = editProtocol.text ?: finding.protocol
            finding.portStatus = editStatus.text ?: finding.portStatus
            finding.service = editService.text ?: finding.service
            finding.plugin = editPlugin.text ?: finding.plugin
            finding.severity = editRisk.value ?: finding.severity
            db.updateFinding(finding)
        }
    }


    ObservableList<Finding> findings

    @Override
    void setIssues(final ObservableList<Finding> findings) {
        this.findings = findings
    }

    @Override
    void populate() {
        if (findings.size() == 1) {
            final Finding f = findings[0]
            editScanner.text = f.scanner
            editIp.text = f.ip
            editHostname.text = f.hostName
            editPort.text = f.portStatus
            editStatus.text = f.portStatus
            editProtocol.text = f.protocol
            editPlugin.text = f.plugin
            editCvss.text = f.baseCVSS.toString()
            editRisk.value = f.severity
        } else {
            editScanner.text = ''
            editIp.text = ''
            editHostname.text = ''
            editPort.text = ''
            editStatus.text = ''
            editProtocol.text = ''
            editPlugin.text = ''
            editCvss.text = ''
            editRisk.value = Finding.Severity.UNKNOWN
        }
    }
}
