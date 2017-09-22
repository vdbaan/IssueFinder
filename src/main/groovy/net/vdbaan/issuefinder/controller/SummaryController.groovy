package net.vdbaan.issuefinder.controller

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.SelectionMode
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import net.vdbaan.issuefinder.util.TableUtils

class SummaryController {
    @FXML
    TreeTableColumn prop
    @FXML
    TreeTableColumn val
    @FXML
    TreeTableView treeTable

    Map<String, FXMLController.Container> summary
    TreeItem<Wrapper> root

    private class Wrapper {
        SimpleStringProperty key
        SimpleStringProperty value

        def setKey(String key) {
            if (this.key == null) this.key = new SimpleStringProperty(key)
            else this.key.set(key)
        }

        def setValue(String value) {
            if (this.value == null) this.value = new SimpleStringProperty(value)
            else this.value.set(value)
        }

        String getKey() {
            return key.get()
        }

        String getValue() {
            return value.get()
        }
    }

    /*
- IPs (3)
  - 192.168.0.1
    - open ports (12)                       22,80,81,8080,<etc>
    - highest vulnerability (1 CRITICAL)
      - <affected service>                  <Name of plugin>
    - found services                        ssh,http,<etc>
  - 192.168.0.2
    - open ports
    + highest vulnerability (3 HIGH)
    - found services
  + 192.168.0.3 <can expand>

     */

    def setup() {
        root = new TreeItem(new Wrapper(key: String.format("IPs (%d)", summary.size()), value: ""))
        summary.each { k, FXMLController.Container v ->
//            final TreeItem<Main.Employee> root = new TreeItem<>(new Main.Employee("Sales Department", ""));
            TreeItem<Wrapper> ip = new TreeItem(new Wrapper(key: k, value: ""))
            ip.children << new TreeItem(new Wrapper(key: String.format("open ports (%d)", v.listedports.size()), value: v.listedports.join(", ")))
            ip.children << new TreeItem(new Wrapper(key: String.format("found services (%d)", v.listedservices.size()), value: v.listedservices.join(", ")))
            TreeItem<Wrapper> vulns = new TreeItem(new Wrapper(key: String.format("Highest vulnerability (%d)", v.plugins.size()), value: v.highest))
            v.plugins.each { vulns.children << new TreeItem(new Wrapper(key: "", value: it)) }
            ip.children << vulns
            root.children << ip
        }
        //empColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Employee, String> param) -> new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        treeTable.setRoot(root)
        prop.setCellValueFactory({ TreeTableColumn.CellDataFeatures<Wrapper, String> param -> new ReadOnlyStringWrapper(param.getValue().getValue().getKey()) })
        val.setCellValueFactory({ TreeTableColumn.CellDataFeatures<Wrapper, String> param -> new ReadOnlyStringWrapper(param.getValue().getValue().getValue()) })
        treeTable.selectionModel.selectionMode = SelectionMode.MULTIPLE
        TableUtils.installCopyPasteHandler(treeTable)
    }
}

/*
        String toString() {
            StringBuilder sb = new StringBuilder()
            sb.append(String.format("open ports (%d)",listedports.size()))
            sb.append("\t")
            sb.append(listedports.join(","))
            sb.append("\n")
            sb.append(String.format("found services (%d)",listedservices.size()))
            sb.append("\t")
            sb.append(listedservices.join(","))
            sb.append("\n")
            sb.append(String.format("Highest vulnerability (%d %s)",plugins.size(),highest))
            sb.append("\t")
            sb.append(plugins.join(","))
            sb.append("\n")
            return sb.toString()
        }
 */