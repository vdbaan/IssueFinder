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

package net.vdbaan.issuefinder.presenter


import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.collections.transformation.SortedList
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.input.ClipboardContent
import javafx.stage.Modality
import javafx.util.Callback
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.EditorDialogView
import net.vdbaan.issuefinder.view.FindingTabView

class FindingTabPresenter {
    FindingTabView view
    SortedList<Finding> sortedData

    FindingTabPresenter(final FindingTabView issuesTabView) {
        this.view = issuesTabView
        view.copySelectedIpsHandler = this.&copySelectedIps
        view.copySelectedPortsAndIpsHandler = this.&copySelectedPortsAndIps
        view.filterOnIPHandler = this.&filterOnIP
        view.filterOnPluginHandler = this.&filterOnPlugin
        view.filterOnPortHandler = this.&filterOnPort
        view.filterOnProtocolHandler = this.&filterOnProtocol
        view.filterOnPortStatusHandler = this.&filterOnPortStatus
        view.filterOnRiskHandler = this.&filterOnRisk
        view.filterOnScannerHandler = this.&filterOnScanner
        view.filterOnServiceHandler = this.&filterOnService
        view.editSelectionHandler = this.&editSelection
        view.selectItemPropertyListener = this.&showContent
        view.tableSelectionMode = SelectionMode.MULTIPLE
        view.setRiskColumnCellFactory(this.&riskCellFactory as Callback<TableColumn<Finding, Finding.Severity>, TableCell<Finding, Finding.Severity>>)
        view.exportToCSVHandler = this.&exportToCSV
        view.exportToIvilHandler = this.&exportToIvil
    }

    void bindMasterData(final ObservableList<Finding> masterData) {
        sortedData = new SortedList<>(masterData)
        sortedData.comparatorProperty().bind(view.tableComparatorProperty as ObservableValue<? extends Comparator<? super Finding>>)
        view.mainTableItems = sortedData
    }

    void copySelectedIps(final ActionEvent event) {
        copyUniqueIPs(view.selectedFindingsList)
    }

    void copySelectedPortsAndIps(final ActionEvent e) {
        copyUniquePortAndIPs(view.selectedFindingsList)
    }

    void filterOnIP(final ActionEvent e) {
        view.mainFilterText = String.format("IP == '%s'", view.selectedFinding.ip)
    }

    void filterOnPlugin(final ActionEvent e) {
        view.mainFilterText = String.format("PLUGIN == '%s'", view.selectedFinding.plugin)
    }

    void filterOnPort(final ActionEvent e) {
        view.mainFilterText = String.format("PORT == '%s'", view.selectedFinding.port)
    }

    void filterOnProtocol(final ActionEvent e) {
        view.mainFilterText = String.format("PROTOCOL == '%s'", view.selectedFinding.protocol)
    }

    void filterOnPortStatus(final ActionEvent e) {
        view.mainFilterText = String.format("PORTSTATUS == '%s'", view.selectedFinding.portStatus)
    }

    void filterOnRisk(final ActionEvent e) {
        view.mainFilterText = String.format("RISK == '%s'", view.selectedFinding.risk)
    }

    void filterOnScanner(final ActionEvent e) {
        view.mainFilterText = String.format("SCANNER == '%s'", view.selectedFinding.scanner)
    }

    void filterOnService(final ActionEvent e) {
        view.mainFilterText = String.format("SERVICE == '%s'", view.selectedFinding.service)
    }

    void exportToCSV(final ActionEvent e) {
        final File location = view.retentionFileChooser.showSaveDialog(view.window, 'Save to CSV')
        if (location != null) {
            location.withWriter { out ->
                out.println '"Scanner", "Ip Address", "Host Name", "Port", "Port Status", "Protocol", "Location", "Service", "Plugin", "Severity", "Base CVSS Score"'
                view.selectedFindingsList.each { final finding ->
                    out.println finding.toCSV()
                }
            }
        }
    }

    void exportToIvil(final ActionEvent e) {
        final File location = view.retentionFileChooser.showSaveDialog(view.window, 'Save as XML')
        if (location != null) {
            view.selectedFindingsList.each { finding ->
                location.write(finding.toIvil())
            }
        }
    }

    private Dialog editorDialog
    private EditorDialogView editorController


    void editSelection(final ActionEvent e) {
        if (editorDialog == null) {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource('/editorDialog.fxml'))
            editorDialog = new Dialog()
            editorDialog.initOwner(view.window)
            editorDialog.title = 'Edit Issue(s)'
            editorDialog.initModality(Modality.APPLICATION_MODAL)
            editorDialog.dialogPane = (loader.load() as DialogPane)
            editorDialog.getDialogPane().getButtonTypes().add(new ButtonType('Save', ButtonBar.ButtonData.APPLY))
            editorController = loader.controller
            editorDialog.resultConverter = { final buttonType ->
                if (buttonType == ButtonType.APPLY) {
                    editorController.saveFindings()
                    view.refresh()
                }
            }
        }
        editorController.issues = view.selectedFindingsList
        editorController.populate()
        editorDialog.showAndWait()
    }

    void showContent(
            final ObservableValue<? extends Finding> observable, final Finding oldValue, final Finding newValue) {
        view.textArealoadContent(newValue?.htmlDescription() ?: '')
    }

    void copyUniqueIPs(final ObservableList<Finding> data) {
        final Set<String> ips = new TreeSet<>()
        data.each { ips << it.ip }
        ips.remove('none') // FIXME due to NetSparkerParser
        final def sorted = ips.sort { final a, final b ->
            final def ip1 = a.split("\\.")
            final def ip2 = b.split("\\.")
            final def uip1 = String.format("%3s.%3s.%3s.%3s", ip1[0], ip1[1], ip1[2], ip1[3])
            final def uip2 = String.format("%3s.%3s.%3s.%3s", ip2[0], ip2[1], ip2[2], ip2[3])
            uip1 <=> uip2
        }
        final ClipboardContent clipboardContent = new ClipboardContent()
        clipboardContent.putString(sorted.join("\n"))
        view.clipboardContent = clipboardContent
    }

    void copyUniquePortAndIPs(final ObservableList<Finding> data) {
        final Map<String, String> ips = new TreeMap<>()
        final String formatString = Config.instance.getProperty(Config.IP_PORT_FORMAT_STRING)
        data.each { final f ->
            final String port = f.port.split('/')[0]
            if (port.number && port != '0') {
                if (!f.ip.equalsIgnoreCase('none')) { // FIXME due to NetSparkerParser

                    ips[f.ip + ":" + port] = f.formatString(formatString as String)
                }
            }
        }
        final Map<String, String> sorted = ips.sort({ final Map.Entry a, final Map.Entry b ->
            final def ip1 = a.key.toString().split(":")[0].split("\\.")
            final def port1 = a.key.toString().split(":")[1]
            final def ip2 = b.key.toString().split(":")[0].split("\\.")
            final def port2 = b.key.toString().split(":")[1]
            final
            def uip1 = String.format("%03d.%03d.%03d.%03d.%05d", ip1[0] as int, ip1[1] as int, ip1[2] as int, ip1[3] as int, port1 as int)
            final
            def uip2 = String.format("%03d.%03d.%03d.%03d.%05d", ip2[0] as int, ip2[1] as int, ip2[2] as int, ip2[3] as int, port2 as int)
            uip1 <=> uip2
        })
        final ClipboardContent clipboardContent = new ClipboardContent()
        clipboardContent.putString(sorted.values().join("\n"))
        view.clipboardContent = clipboardContent
    }

    final List<String> cellStyles = ['cell', 'indexed-cell', 'table-cell', 'table-column']
    final List<String> rowStyles = ['cell', 'indexed-cell', 'table-row-cell']

    TableCell riskCellFactory(Object val) {
        return new TableCell<Finding, Finding.Severity>() {

            @Override
            protected void updateItem(final Finding.Severity item, final boolean empty) {
                super.updateItem(item, empty)
                getStyleClass().clear()
                getStyleClass().addAll(cellStyles)
                final TableRow<Finding> row = getTableRow()
                row.getStyleClass().clear()
                row.getStyleClass().addAll(rowStyles)
                if (item != null) {
                    setText(item.toString())
                    if (Config.getInstance().getProperty(Config.COLOURED_ROWS) as boolean) {
                        row.getStyleClass().add(item.toString() + 'ROW')
                    } else {
                        getStyleClass().add(item.toString())
                    }
                }
            }
        }
    }
}
