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
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableCell
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import javafx.scene.text.Text
import net.vdbaan.issuefinder.presenter.SummaryPresenter
import net.vdbaan.issuefinder.util.Container
import net.vdbaan.issuefinder.util.TableUtils
import net.vdbaan.issuefinder.util.Wrapper
import net.vdbaan.issuefinder.view.SummaryView

@CompileStatic
class SummaryViewImpl extends AbstractSummaryView implements SummaryView {
    @FXML
    TreeTableColumn prop
    @FXML
    TreeTableColumn val
    @FXML
    TreeTableView treeTable

    @FXML
    void initialize() {
        presenter = new SummaryPresenter(this)

        TableUtils.installCopyPasteHandler(treeTable)

        prop.setCellValueFactory({ TreeTableColumn.CellDataFeatures<Wrapper, String> param -> new ReadOnlyStringWrapper(param.getValue().getValue().getKey()) })
        val.setCellValueFactory({ TreeTableColumn.CellDataFeatures<Wrapper, String> param -> new ReadOnlyStringWrapper(param.getValue().getValue().getValue()) })

        val.setCellFactory({ TreeTableColumn<Object, String> param ->
            new TreeTableCell<Object, String>() {
                private Text text

                @Override
                void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty)
                    if (!isEmpty() && !("" == item.toString())) {
                        text = new Text(item.toString())
                        text.wrappingWidthProperty().bind(val.widthProperty())
                        this.setWrapText(true)
                        setGraphic(text)

                    } else {
                        this.setText('')
                        setGraphic(null)
                    }
                }
            }
        })
    }

    @Override
    void setTreeRoot(TreeItem root) {
        treeTable.setRoot(root)
    }

    @Override
    TreeItem getTreeRoot() {
        return treeTable.getRoot()
    }


}

abstract class AbstractSummaryView extends BaseViewImpl implements SummaryView {
    SummaryPresenter presenter

    @Override
    void setSummary(Map<String, Container> summary) {
        presenter.setSummary(summary)
    }
}