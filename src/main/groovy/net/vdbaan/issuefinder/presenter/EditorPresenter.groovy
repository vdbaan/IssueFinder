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
import javafx.event.ActionEvent
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.EditorView

@CompileStatic
class EditorPresenter {

    private EditorView editorView
    private List<Finding> data

    EditorPresenter(final EditorView view) {
        this.editorView = view
        this.editorView.setCancelAction(this.&cancel)
        this.editorView.setOkAction(this.&ok)

        // for a weird reason, when you make it into a one lines, it treats it as an error
        final List values = Arrays.asList(Finding.Severity.values())
        this.editorView.setEditRiskOptions(values)
    }

    private cancel(final ActionEvent event) {
        editorView.close()
    }

    private ok(final ActionEvent event) {
        final DbHandler db = editorView.getDbHandler()
        data.each { final finding ->
            finding.scanner = editorView.getEditScanner() ?: finding.scanner
            finding.hostName = editorView.getEditHostname() ?: finding.hostName
            finding.ip = editorView.getEditIp() ?: finding.ip
            finding.port = editorView.getEditPort() ?: finding.port
            finding.service = editorView.getEditService() ?: finding.service
            finding.plugin = editorView.getEditPlugin() ?: finding.plugin
            finding.severity = editorView.getEditRisk() ?: finding.severity
            db.updateFinding(finding)
        }
        editorView.dbUpdated()
        editorView.close()
    }

    void setEditData(final List<Finding> data) {
        this.data = data
        if (data.size() == 1) {
            final Finding finding = data[0]
            editorView.setEditScanner(finding.scanner)
            editorView.setEditIp(finding.ip)
            editorView.setEditHostname(finding.hostName)
            editorView.setEditPort(finding.port)
            editorView.setEditService(finding.service)
            editorView.setEditPlugin(finding.plugin)
            editorView.setEditRisk(finding.severity)
        }
    }
}
