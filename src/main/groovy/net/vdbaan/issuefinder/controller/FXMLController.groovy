package net.vdbaan.issuefinder.controller

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList

/**
 * Created by Steven on 01/09/2017.
 */
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.web.WebView
import net.vdbaan.issuefinder.MainApp
import net.vdbaan.issuefinder.filechooser.RetentionFileChooser
import net.vdbaan.issuefinder.filter.FindingPredicate
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.filter.FindingPredicateParser
import net.vdbaan.issuefinder.filter.FindingPredicateParserRuntimeException

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection

class FXMLController implements Initializable {
    MainApp mainApp

    @FXML
    Label statusLabel
    @FXML
    Label rowInfoLabel
    @FXML
    Label ipInfoLabel
    @FXML
    ComboBox filterText
    @FXML
    TableView mainTable
    @FXML
    WebView textArea
    FilteredList<Finding> filteredData

    FindingPredicateParser findingPredicateParser = new FindingPredicateParser()

    ObservableList<Finding> masterData

    @Override
    void initialize(URL url, ResourceBundle rb) {
        statusLabel.text = 'Application started'
        rowInfoLabel.text = '0 Findings'
        ipInfoLabel.text = '0 unique IPs'

        filterText.items.addAll(['IP == "127.0.0.1"', 'SCANNER == \'nmap\'', 'SERVICE LIKE \'http\'', 'PORT LIKE 443', '!EXPLOITABLE', '(SERVICE LIKE \'SMB\') && EXPLOITABLE'])
    }

    def setMasterData(ObservableList<Finding> masterData) {
        this.masterData = masterData
    }

    def setup(MainApp mainApp) {
        this.mainApp = mainApp

//        String css = getClass().getResource('/style.css')
//        mainTable.getScene().getStylesheets().add(css)

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        filteredData = new FilteredList<>(masterData, { f -> true })

        // 2. Set the filter Predicate whenever the filter changes.

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Finding> sortedData = new SortedList<>(filteredData)

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(mainTable.comparatorProperty())

        // 5. Add sorted (and filtered) data to the table.
        mainTable.setItems(sortedData)

        mainTable.getSelectionModel().selectedItemProperty().addListener({
            ObservableValue<? extends Finding> observable, Finding oldValue, Finding newValue ->
                if (newValue != null) {
                    textArea.getEngine().loadContent(newValue.htmlDescription())
                }
        } as ChangeListener<Finding>)


        filteredData.addListener({ ListChangeListener.Change<? extends Finding> c ->
            rowInfoLabel.text = String.format('%6d Findings', filteredData.size())
            HashSet<String> ips = new HashSet<>()
            filteredData.each { ips << it.ip }
            ipInfoLabel.text = String.format('%6d unique IPs', ips.size())
        } as ListChangeListener)

        filterText.valueProperty().addListener({ obs, oldValue, newValue ->
            filterTable()
        } as ChangeListener)
        filterText.getEditor().setOnMouseClicked({ e ->
            filterText.getEditor().getStyleClass().clear()
            filterText.getEditor().getStyleClass().add('text-input')
        })
    }


    def filterOnIp() {
        Finding f = mainTable.selectionModel.selectedItem
        filterText.setValue(String.format("IP == '%s'", f.ip))
    }

    def filterOnPort() {
        Finding f = mainTable.selectionModel.selectedItem
        filterText.setValue(String.format("PORT == '%s'", f.port))
    }

    def filterOnService() {
        Finding f = mainTable.selectionModel.selectedItem
        filterText.setValue(String.format("SERVICE == '%s'", f.service))
    }

    def filterOnPlugin() {
        Finding f = mainTable.selectionModel.selectedItem
        def plugin = f.plugin
        filterText.setValue(plugin.contains('\'')?String.format("PLUGIN == \"%s\"", plugin):String.format("PLUGIN == '%s'", plugin))
    }

    // TODO: adjust to list to support multiline select
    def modifyEntry() {
        mainApp.showEditor(mainTable.selectionModel.selectedItem)
    }

    def newAction() {
        masterData.clear()
        textArea.getEngine().loadContent('')
    }


    RetentionFileChooser openFileChooser = new RetentionFileChooser()

    def openAction() {
        openFiles(openFileChooser.showOpenMultipleDialog(mainTable.getScene().getWindow()))
    }

    def exportAction() {}

    def closeAction() {
        System.exit(0)
    }

    def aboutAction() {
        mainApp.showAbout()
    }

    def filterTable() {
        if (filterText.getValue() != null && filterText.getValue() != "") {
            FindingPredicate fp = null
            try {
                fp = findingPredicateParser.parse(filterText.getValue())
                if (fp != null) {
                    filteredData.setPredicate(fp)
                    if (!filterText.getItems().contains(filterText.getValue())) {
                        List list = new ArrayList()
                        list.add(fp.toString())
                        list.addAll(filterText.getItems())
                        filterText.getItems().clear()
                        filterText.getItems().addAll(list)
                        filterText.setValue(fp.toString())
                    }
                } else {
                    clearFilter()
                }
            } catch (StringIndexOutOfBoundsException e) {
                filterText.getEditor().getStyleClass().add("text-input-wrong")
                statusLabel.requestFocus()
            } catch (FindingPredicateParserRuntimeException ex) {
                filterText.getEditor().getStyleClass().add("text-input-wrong")
                statusLabel.requestFocus()
            }
        }
    }

    @FXML
    def clearFilter() {
        filterText.setValue(null)
        filterText.getEditor().getStyleClass().clear()
        filterText.getEditor().getStyleClass().add("text-input")
        filteredData.setPredicate({ f -> true })
    }

    def openFiles(List<File> files) {
        mainApp.showProgressDialog(files, statusLabel)
    }


    def copyUniqueIps() {
        Set<String> ips = new TreeSet<>()
        filteredData.each { ips << it.ip }
        ips.remove('none') // FIXME due to NetSparkerParser
        def sorted = ips.sort { a, b ->
            def ip1 = a.split("\\.")
            def ip2 = b.split("\\.")
            def uip1 = String.format("%3s.%3s.%3s.%3s", ip1[0], ip1[1], ip1[2], ip1[3])
            def uip2 = String.format("%3s.%3s.%3s.%3s", ip2[0], ip2[1], ip2[2], ip2[3])
            uip1 <=> uip2
        }
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard()
        clpbrd.setContents(new StringSelection(sorted.join("\n")), null)
    }

    def copyUniquePortsAndIps() {
        Set<String> ips = new TreeSet<>()
        filteredData.each { f ->
            String port = f.port.split('/')[0]
            if (port.isNumber() && port != '0') {
                if (!f.ip.equalsIgnoreCase('none')) // FIXME due to NetSparkerParser
                    ips << f.ip + ":" + port
            }
        }
        def sorted = ips.sort { a, b ->
            def ip1 = a.split(":")[0].split("\\.")
            def ip2 = b.split(":")[0].split("\\.")
            def uip1 = String.format("%03d.%03d.%03d.%03d.%05d", ip1[0] as int, ip1[1] as int, ip1[2] as int, ip1[3] as int, (a.split(":")[1]) as int)
            def uip2 = String.format("%03d.%03d.%03d.%03d.%05d", ip2[0] as int, ip2[1] as int, ip2[2] as int, ip2[3] as int, (b.split(":")[1]) as int)
            uip1 <=> uip2
        }
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard()
        clpbrd.setContents(new StringSelection(sorted.join("\n")), null)
    }
}
