package net.vdbaan.issuefinder.controller

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback
import net.vdbaan.issuefinder.model.Finding

class FormattedTableCellFactory<S, T> implements Callback<TableColumn<Finding,Finding.Severity>, TableCell<Finding,Finding.Severity>> {
    @Override
    TableCell<S, T> call(TableColumn<Finding,Finding.Severity> param) {

        List<String> styles = ['cell','indexed-cell','table-cell','table-column']
        TableCell result = new TableCell<Finding,Finding.Severity>() {

            @Override
            protected void updateItem(Finding.Severity item, boolean empty) {
                super.updateItem(item, empty)
                setText(item.toString())

                getStyleClass().clear()
                getStyleClass().addAll(styles)
                getStyleClass().add(item.toString())
            }
        }
        return result
    }
}
