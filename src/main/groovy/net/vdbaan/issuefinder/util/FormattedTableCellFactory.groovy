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
package net.vdbaan.issuefinder.util

import groovy.transform.CompileStatic
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding

@CompileStatic
class FormattedTableCellFactory<S, T> implements Callback<TableColumn<Finding, Finding.Severity>, TableCell<Finding, Finding.Severity>> {
    @Override
    TableCell<S, T> call(TableColumn<Finding, Finding.Severity> param) {

        List<String> styles = ['cell', 'indexed-cell', 'table-cell', 'table-column']
        TableCell result = new TableCell<Finding, Finding.Severity>() {

            @Override
            protected void updateItem(Finding.Severity item, boolean empty) {
                super.updateItem(item, empty)
                getStyleClass().clear()
                getStyleClass().addAll(styles)
                if(item != null) {
                    setText(item.toString())
                    getStyleClass().add(item.toString())
                }
            }
        }
        return result
    }
}
