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
package net.vdbaan.issuefinder.view

import groovy.transform.CompileStatic
import javafx.event.ActionEvent
import javafx.event.EventHandler
import net.vdbaan.issuefinder.model.Finding

@CompileStatic
interface EditorView extends BaseView {

    String getEditScanner()

    void setEditScanner(String editScanner)

    String getEditHostname()

    void setEditHostname(String editHostname)

    String getEditIp()

    void setEditIp(String editIp)

    String getEditPort()

    void setEditPort(String editPort)

    String getEditService()

    void setEditService(String editService)

    String getEditPlugin()

    void setEditPlugin(String editPlugin)

    Finding.Severity getEditRisk()

    void setEditRisk(Finding.Severity editRisk)

    void setEditRiskOptions(List<Finding.Severity> options)

    EventHandler<ActionEvent> getOkAction()

    void setOkAction(EventHandler<ActionEvent> okAction)

    EventHandler<ActionEvent> getCancelAction()

    void setCancelAction(EventHandler<ActionEvent> cancelAction)

    void setEditData(List<Finding> data)
}