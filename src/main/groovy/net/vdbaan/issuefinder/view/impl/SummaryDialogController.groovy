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

package net.vdbaan.issuefinder.view.impl

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableCell
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import javafx.scene.text.Text
import javafx.util.Callback
import net.vdbaan.issuefinder.util.Container
import net.vdbaan.issuefinder.util.TableUtils
import net.vdbaan.issuefinder.util.Wrapper
import net.vdbaan.issuefinder.view.SummaryDialogView

class SummaryDialogController implements SummaryDialogView {

    @FXML
    // fx:id="treeTable"
    private TreeTableView<?> treeTable; // Value injected by FXMLLoader

    @FXML
    // fx:id="prop"
    private TreeTableColumn<?, ?> prop; // Value injected by FXMLLoader

    @FXML
    // fx:id="val"
    private TreeTableColumn<?, ?> val; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        TableUtils.installCopyPasteHandler(treeTable)

        prop.cellValueFactory = ({
            final TreeTableColumn.CellDataFeatures<Wrapper, String> param -> new ReadOnlyStringWrapper(param.value.value.key)
        } as Callback<TreeTableColumn.CellDataFeatures<?, ?>, ObservableValue<?>>)
        val.cellValueFactory = ({
            final TreeTableColumn.CellDataFeatures<Wrapper, String> param -> new ReadOnlyStringWrapper(param.value.value.value)
        } as Callback<TreeTableColumn.CellDataFeatures<?, ?>, ObservableValue<?>>)

        val.cellFactory = { final TreeTableColumn<Object, String> param ->
            new TreeTableCell<Object, String>() {
                private Text text

                @Override
                void updateItem(final String item, final boolean empty) {
                    super.updateItem(item, empty)
                    if (!isEmpty() && !("" == item.toString())) {
                        text = new Text(item.toString())
                        text.wrappingWidthProperty().bind(val.widthProperty())
                        this.wrapText = true
                        graphic = text

                    } else {
                        this.text = ''
                        graphic = null
                    }
                }
            }
        } as Callback<TreeTableColumn<?, ?>, TreeTableCell<?, ?>>
    }

    void setSummary(final Map<String, Container> summary) {
        final TreeItem<Wrapper> treeRoot = new TreeItem(new Wrapper(key: String.format("Location (%d)", summary.size()), value: ""))
        summary.each { final key, final Container container ->
            final TreeItem<Wrapper> treeItem = new TreeItem(new Wrapper(key: key, value: ""))
            treeItem.children << new TreeItem(new Wrapper(key: String.format("open ports (%d)", container.listedports.size()), value: container.listedports.join(", ")))
            treeItem.children << new TreeItem(new Wrapper(key: String.format("found services (%d)", container.listedservices.size()), value: container.listedservices.join(", ")))
            final TreeItem<Wrapper> vulns = new TreeItem(new Wrapper(key: String.format("Highest vulnerability (%d)", container.plugins.size()), value: container.highest.toString()))
            container.plugins.each { vulns.children << new TreeItem(new Wrapper(key: "", value: it)) }
            treeItem.children << vulns
            treeRoot.children << treeItem
        }
        treeTable.root = treeRoot
    }
}