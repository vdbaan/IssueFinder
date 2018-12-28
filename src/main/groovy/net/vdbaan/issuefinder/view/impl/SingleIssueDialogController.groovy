/*
 *  Copyright (C) 2018  S. van der Baan
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

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.model.Issue

class SingleIssueDialogController {
    @FXML
    // fx:id="title"
    private TextField title; // Value injected by FXMLLoader

    @FXML
    // fx:id="issues"
    private TextField issues; // Value injected by FXMLLoader

    @FXML
    // fx:id="risk"
    private ChoiceBox<Finding.Severity> risk; // Value injected by FXMLLoader

    @FXML
    // fx:id="description"
    private TextArea description; // Value injected by FXMLLoader

    @FXML
    // fx:id="remediation"
    private TextArea remediation; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        final List<Finding.Severity> values = new ArrayList()
        values.addAll(Finding.Severity.values())
        risk.items = FXCollections.observableList(values)
    }

    void setIssue(Issue issue = null) {
        if (issue == null) {
            title.text = ''
            issues.text = ''
            risk.value = Finding.Severity.UNKNOWN
            description.text = ''
            remediation.text = ''
        } else {
            title.text = issue.title
            issues.text = issue.findings
            risk.value = issue.severity
            description.text = issue.description
            remediation.text = issue.recommendations
        }
    }
}
