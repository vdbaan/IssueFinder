package net.vdbaan.issuefinder.controller

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.Stage
import net.vdbaan.issuefinder.model.Finding


class EditorController {
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

    Stage dialogPane
    TableView tableView
    ObservableList<Finding> masterData
    Finding finding

    def setup() {
        editRisk.items.addAll(Finding.Severity.values())
        ObservableList<Finding> list = tableView.selectionModel.selectedItems
        if (list.size() == 1) {
            Finding finding = tableView.selectionModel.selectedItem
            editScanner.setText(finding.scanner)
            editHostname.setText(finding.hostName)
            editIp.setText(finding.ip)
            editPort.setText(finding.port)
            editService.setText(finding.service)
            editPlugin.setText(finding.plugin)
            editRisk.setValue(finding.severity)
        }
    }

    def doOk() {
        tableView.selectionModel.selectedIndices.each {
            Finding finding = masterData.get(it)
            finding.scanner = editScanner.text ?: finding.scanner
            finding.hostName = editHostname.text ?: finding.hostName
            finding.ip = editIp.text ?: finding.ip
            finding.port = editPort.text ?: finding.port
            finding.service = editService.text ?: finding.service
            finding.plugin = editPlugin.text ?: finding.plugin
            finding.severity = editRisk.getValue() ?: finding.severity
        }
        dialogPane.close()
    }

    def doCancel() {
        dialogPane.close()
    }
}
