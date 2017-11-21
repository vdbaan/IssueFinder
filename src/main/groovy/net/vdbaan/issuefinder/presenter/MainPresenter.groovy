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
package net.vdbaan.issuefinder.presenter

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.transformation.SortedList
import javafx.event.ActionEvent
import javafx.scene.control.SelectionMode
import javafx.scene.input.ClipboardContent
import javafx.scene.input.MouseEvent
import net.vdbaan.issuefinder.MainApp
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.filter.FindingPredicate
import net.vdbaan.issuefinder.filter.FindingPredicateParser
import net.vdbaan.issuefinder.filter.FindingPredicateParserRuntimeException
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.Container
import net.vdbaan.issuefinder.view.MainView

import java.util.logging.Level

@Log
@CompileStatic
class MainPresenter {
    private MainView masterView
    DbHandler db
    MainApp mainApp

    ObservableList<Finding> masterData = FXCollections.observableArrayList()
    SortedList<Finding> sortedData

    FindingPredicateParser findingPredicateParser = new FindingPredicateParser()

    MainPresenter(MainView view) {
        this.masterView = view

        db = masterView.getDbHandler()

        masterView.setStatusLabel 'Application started'
        masterView.setRowInfoLabel '0 Findings'
        masterView.setIpInfoLabel '0 unique IPs'

        masterView.setFilterTextItems((List<String>) Config.getInstance().getProperty(Config.FILTERS))

        masterView.addFiterTextListener(this.&filterTable)
        masterView.addFilterTextMouseClicked(this.&resetFilterTextStyle)

        sortedData = new SortedList<>(masterData)
        sortedData.comparatorProperty().bind(masterView.getTableComparatorProperty())
        sortedData.addListener(this.&sortedDataListener as ListChangeListener)

        masterView.setMainTableItems(sortedData)
        masterView.setSelectItemPropertyListener(this.&showContent)
        masterView.setTableSelectionMode(SelectionMode.MULTIPLE)

//        TableUtils.installCopyPasteHandler(masterView.getTableKeyEvent())

        masterView.setNewAction(this.&newAction)
        masterView.setOpenAction(this.&openAction)
        masterView.setExportAction(this.&exportAction)
        masterView.setLoadAction(this.&loadAction)
        masterView.setSaveAction(this.&saveAction)
        masterView.setSettingsAction(this.&settingsAction)
        masterView.setCloseAction(this.&closeAction)

        masterView.setAboutHelpAction(this.&aboutHelpAction)
        masterView.setSummaryAction(this.&summaryAction)
        masterView.setAboutAction(this.&aboutAction)

        masterView.setFilterIpAction(this.&filterIpAction)
        masterView.setFilterPluginAction(this.&filterPluginAction)
        masterView.setFilterPortAction(this.&filterPortAction)
        masterView.setFilterServiceAction(this.&filterServiceAction)
        masterView.setFilterOnScannerAction(this.&filterScannerAction)
        masterView.setFilterOnPortStatusAction(this.&filterPortStatusAction)
        masterView.setFilterOnProtocolAction(this.&filterProtocolAction)
        masterView.setFilterOnRiskAction(this.&filterRiskAction)

        masterView.setModifyAction(this.&modifyAction)

        masterView.setCopySelectedIpsAction(this.&copySelectedIpAction)
        masterView.setCopySelectedIpsPortsAction(this.&copySelectedIpPortAction)

        masterView.setCopyIpAction(this.&copyIpAction)
        masterView.setCopyIpPortAction(this.&copyIpPortAction)

        masterView.setClearAction(this.&clearAction)
        masterView.setFilterTableAction(this.&filterTableAction)
    }

    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp
    }

    void filterTable(ObservableValue observable, Object oldValue, Object newValue) {
        filterTableAction(null)
    }


    void showContent(ObservableValue<? extends Finding> observable, Finding oldValue, Finding newValue) {
        masterView.textArealoadContent(newValue?.htmlDescription() ?: '')
    }

    void resetFilterTextStyle(MouseEvent e) {
        masterView.getFilterTextStyleClass().clear()
        masterView.getFilterTextStyleClass().add('text-input')
    }

    void sortedDataListener(ListChangeListener.Change<? extends Finding> c) {
        masterView.setRowInfoLabel String.format('%6d Findings', sortedData.size())
        HashSet<String> ips = new HashSet<>()
        sortedData.each { ips << it.ip }
        masterView.setIpInfoLabel String.format('%6d unique IPs', ips.size())
    }

    void newAction(ActionEvent e) {
        db.deleteAll()
        masterData.removeAll(masterData)
        masterView.textArealoadContent('')
    }

    void openAction(ActionEvent e) {
        openFiles(masterView.getRetentionFileChooser().showOpenMultipleDialog(masterView.getWindow(), 'Load Reports'))
    }


    void saveAction(ActionEvent e) {
        File location = masterView.getRetentionFileChooser().showSaveDialog(masterView.getWindow(), 'Save DB')
        if (location != null) {
            db.saveDb(location.getAbsolutePath())
        }
    }

    void loadAction(ActionEvent e) {
        File location = masterView.getRetentionFileChooser().showOpenDialog(masterView.getWindow(), 'Load DB')
        if (location != null) {
            db.deleteAll()
            db.loadDb(location.getAbsolutePath())
            loadAll()
        }
    }

    void exportAction(ActionEvent e) {}

    void settingsAction(ActionEvent e) {
        mainApp.showSettings()
        masterView.getFilterTextItems().removeAll(masterView.getFilterTextItems())
        masterView.setFilterTextItems((List<String>) Config.getInstance().getProperty(Config.FILTERS))
        filterTableAction(e)
    }

    void closeAction(ActionEvent e) {
        System.exit(0)
    }

    void summaryAction(ActionEvent e) {
        Map<String, Container> summaryInfo = new HashMap()
        sortedData.each { f ->
            Container c = summaryInfo.get(f.ip) ?: new Container()
            if (f.portStatus == 'open') {
                c.listedports << f.port
                c.listedservices << f.service
            }
            if (f.severity < c.highest) { // Severity runs from Critical (0) to UNKNOWN (5)
                c.plugins.clear()
                c.highest = f.severity
            }
            if (f.severity == c.highest) {
                c.plugins << f.plugin
            }
            summaryInfo.put(f.ip, c)
        }

        mainApp.showSummary(summaryInfo)
    }

    void aboutHelpAction(ActionEvent e) {
        mainApp.showHelp()
    }

    void aboutAction(ActionEvent e) {
        mainApp.showAbout()
    }

    void filterIpAction(ActionEvent e) {
        setFilterTextValue(String.format("IP == '%s'", masterView.getSelectedFinding().ip))
    }

    void filterPluginAction(ActionEvent e) {
        setFilterTextValue(String.format("PLUGIN == '%s'", masterView.getSelectedFinding().plugin))
    }

    void filterPortAction(ActionEvent e) {
        setFilterTextValue(String.format("PORT == '%s'", masterView.getSelectedFinding().port))
    }

    void filterServiceAction(ActionEvent e) {
        setFilterTextValue(String.format("SERVICE == '%s'", masterView.getSelectedFinding().service))
    }

    void filterScannerAction(ActionEvent e) {
        setFilterTextValue(String.format("SCANNER == '%s'", masterView.getSelectedFinding().scanner))
    }

    void filterPortStatusAction(ActionEvent e) {
        setFilterTextValue(String.format("STATUS == '%s'", masterView.getSelectedFinding().portStatus))
    }

    void filterProtocolAction(ActionEvent e) {
        setFilterTextValue(String.format("PROTOCOL == '%s'", masterView.getSelectedFinding().protocol))
    }

    void filterRiskAction(ActionEvent e) {
        setFilterTextValue(String.format("RISK == '%s'", masterView.getSelectedFinding().risk))
    }

    void setFilterTextValue(String value) {
        masterView.setFilterText(null)
        resetFilterTextStyle(null)
        masterView.setFilterText(value)
    }

    void modifyAction(ActionEvent e) {
        mainApp.showEditor(masterView.getSelectedFindings())
    }

    void copySelectedIpAction(ActionEvent e) {
        copyUniqueIps(masterView.getSelectedFindings())
    }

    void copySelectedIpPortAction(ActionEvent e) {
        copyUniqueIpPorts(masterView.getSelectedFindings())
    }

    void copyIpAction(ActionEvent e) {
        copyUniqueIps(sortedData)

    }

    void copyIpPortAction(ActionEvent e) {
        copyUniqueIpPorts(sortedData)
    }

    void copyUniqueIpPorts(List<Finding> findings) {
        Map<String, String> ips = new TreeMap<>()
        String formatString = Config.getInstance().getProperty(Config.IP_PORT_FORMAT_STRING)
        findings.each { f ->
            String port = f.port.split('/')[0]
            if (port.isNumber() && port != '0') {
                if (!f.ip.equalsIgnoreCase('none')) // FIXME due to NetSparkerParser
                    ips.put(f.ip + ":" + port, f.formatString(formatString))
            }
        }
        Map<String, String> sorted = ips.sort({ Map.Entry a, Map.Entry b ->
            def ip1 = a.key.toString().split(":")[0].split("\\.")
            def port1 = a.key.toString().split(":")[1]
            def ip2 = b.key.toString().split(":")[0].split("\\.")
            def port2 = b.key.toString().split(":")[1]
            def uip1 = String.format("%03d.%03d.%03d.%03d.%05d", ip1[0] as int, ip1[1] as int, ip1[2] as int, ip1[3] as int, port1 as int)
            def uip2 = String.format("%03d.%03d.%03d.%03d.%05d", ip2[0] as int, ip2[1] as int, ip2[2] as int, ip2[3] as int, port2 as int)
            uip1 <=> uip2
        })
        final ClipboardContent clipboardContent = new ClipboardContent()
        clipboardContent.putString(sorted.values().join("\n"))
        masterView.setClipboardContent(clipboardContent)
    }

    void copyUniqueIps(List<Finding> findings) {
        Set<String> ips = new TreeSet<>()
        findings.each { ips << it.ip }
        ips.remove('none') // FIXME due to NetSparkerParser
        def sorted = ips.sort { a, b ->
            def ip1 = a.split("\\.")
            def ip2 = b.split("\\.")
            def uip1 = String.format("%3s.%3s.%3s.%3s", ip1[0], ip1[1], ip1[2], ip1[3])
            def uip2 = String.format("%3s.%3s.%3s.%3s", ip2[0], ip2[1], ip2[2], ip2[3])
            uip1 <=> uip2
        }
        final ClipboardContent clipboardContent = new ClipboardContent()
        clipboardContent.putString(sorted.join("\n"))
        masterView.setClipboardContent(clipboardContent)
    }

    void filterTableAction(ActionEvent e) {

        String val = masterView.getFilterTextValue() ?: ''
        if (!val?.equals('')) {
            FindingPredicate fp
            try {
                fp = findingPredicateParser.parse(val)
                if (fp != null) {
                    masterData.removeAll(masterData)
                    masterData.addAll(db.getAllFinding(fp.toString()))
                    testSize()
                    List<String> list = masterView.getFilterTextItems()
                    if (!list.contains(val)) {
                        List l = new ArrayList()
                        l.add(val)
                        l.addAll(list)
                        masterView.setFilterTextItems(l)
                    }

                } else {
                    clearAction(null)
                }
            } catch (StringIndexOutOfBoundsException ex) {
                log.log(Level.FINE, 'Got an exception', ex)
                masterView.getFilterTextStyleClass().add("text-input-wrong")

            } catch (FindingPredicateParserRuntimeException ex) {
                log.log(Level.FINE, 'Got an exception', ex)
                masterView.getFilterTextStyleClass().add("text-input-wrong")
            }
        } else {
            masterData.removeAll(masterData)
            masterData.addAll(db.getAllFinding(null))
        }
    }

    void clearAction(ActionEvent e) {
        masterView.setFilterText(null)
        resetFilterTextStyle(null)
        loadAll()
    }

    def openFiles(List<File> files) {
        if (files?.size() > 0)
            mainApp.showProgressDialog(files)
        else log.info 'No files to process'
    }

    def loadAll() {
        masterData.removeAll(masterData)
        masterData.addAll(db.getAllFinding(null))
        testSize()
    }

    void dbUpdated() {
        log.fine 'Db updated, reloading data'
        loadAll()
    }

    void testSize() {
        int sizeList = masterData.size()
        int sizeDb = db.getNumrows()
        if (sizeDb > sizeList) {
            mainApp.showWarning(String.format("Only showing %d of %d Findings\nPlease use a filter to reduce the number of finding", sizeList, sizeDb))
            masterView.setRowInfoLabel String.format("Showing %d of %d Findings", sizeList, sizeDb)
        }
    }
}
