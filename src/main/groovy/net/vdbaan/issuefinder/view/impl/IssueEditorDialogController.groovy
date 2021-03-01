/*
 *  Copyright (C) 2018  S. van der Baan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder.view.impl

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.DialogPane
import javafx.scene.control.ListView
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Issue

class IssueEditorDialogController {

    ObservableList<Issue> issues

    @FXML
    // fx:id="issueList"
    private ListView<Issue> issueList // Value injected by FXMLLoader

    @FXML
    void addIssue(ActionEvent event) {
        buildDialog()
        issueDialog.resultConverter = { final buttonType ->
            if (buttonType == ButtonType.APPLY) {
                // W.I.P.
            }
        }
        controller.setIssue()
        issueDialog.showAndWait()

    }


    @FXML
    void deleteIssue(ActionEvent event) {
        issueList.items.remove(issueList.selectionModel.selectedItem)
    }

    @FXML
    void editIssue(ActionEvent event) {
        buildDialog()
        issueDialog.resultConverter = { final buttonType ->
            if (buttonType == ButtonType.APPLY) {
                // W.I.P.
            }
        }
        controller.setIssue(issueList.selectionModel.selectedItem)

        issueDialog.showAndWait()

    }

    @FXML
    void initialize() {
        List savedIssues = Config.instance.getProperty(Config.ISSUE_LIST) as List
        issues = FXCollections.observableList(savedIssues)
        issueList.items = issues
    }

    Dialog issueDialog
    SingleIssueDialogController controller

    private void buildDialog() {
        if (issueDialog == null) {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource('/singleIssueDialog.fxml'))
            issueDialog = new Dialog()
            issueDialog.initOwner(issueList.scene.window)
            issueDialog.title = 'Edit issue(s)'
            issueDialog.dialogPane = (loader.load() as DialogPane)
            controller = loader.getController()
            issueDialog.getDialogPane().getButtonTypes().add(new ButtonType('Save', ButtonBar.ButtonData.APPLY))
        }
    }
}
