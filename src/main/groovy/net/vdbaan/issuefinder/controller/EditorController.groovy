package net.vdbaan.issuefinder.controller

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.Stage
import net.vdbaan.issuefinder.model.Finding


class EditorController {
    @FXML TextField editScanner
    @FXML TextField editHostname
    @FXML TextField editIp
    @FXML TextField editPort
    @FXML TextField editService
    @FXML TextField editPlugin
    @FXML ChoiceBox editRisk

    Stage dialogPane
    ObservableList<Finding> masterData
    Finding finding

    def setup() {
        editRisk.items.addAll(Finding.Severity.values())
        editScanner.setText(finding.scanner)
        editHostname.setText(finding.hostName)
        editIp.setText(finding.ip)
        editPort.setText(finding.port)
        editService.setText(finding.service)
        editPlugin.setText(finding.plugin)
        editRisk.setValue(finding.severity)
    }

    boolean okClicked = false

    def doOk() {
        finding.scanner = editScanner.text
        finding.hostName = editHostname.text
        finding.ip = editIp.text
        finding.port = editPort.text
        finding.service = editService.text
        finding.plugin = editPlugin.text
        finding.severity = editRisk.getValue()
        okClicked = true
        dialogPane.hide()
    }

    def doCancel() {
        dialogPane.hide()
    }
}
