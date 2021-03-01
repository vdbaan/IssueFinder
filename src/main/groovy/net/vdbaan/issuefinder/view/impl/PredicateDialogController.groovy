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
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.PredicateDialogView

class PredicateDialogController implements PredicateDialogView {
    @FXML
    private TextField scanner

    @FXML
    private TextField service

    @FXML
    private TextField hostname

    @FXML
    private TextField ip

    @FXML
    private TextField port

    @FXML
    private TextField status

    @FXML
    private TextField description

    @FXML
    private TextField plugin

    @FXML
    private TextField cvss

    @FXML
    private ComboBox<Finding.Severity> risk

    @FXML
    private CheckBox exploitabe

    @FXML
    private TextField protocol

    @FXML
    void initialize() {
        final List<Finding.Severity> values = new ArrayList()
        values.addAll(Finding.Severity.values())
        risk.items = FXCollections.observableList(values)
    }

    @Override
    void resetElements() {
        scanner.text = ''
        service.text = ''
        hostname.text = ''
        ip.text = ''
        port.text = ''
        status.text = ''
        description.text = ''
        plugin.text = ''
        cvss.text = ''
        protocol.text = ''
        risk.value = null
    }

    @Override
    String buildPredicate() {
        List<String> result = new ArrayList<>()
        if (scanner.text != '') {
            result.add(String.format("SCANNER == '%s'", scanner.text))
        }
        if (service.text != '') {
            result.add(String.format("SERVICE == '%s'", service.text))
        }
        if (hostname.text != '') {
            result.add(String.format("HOSTNAME == '%s'", hostname.text))
        }
        if (ip.text != '') {
            result.add(String.format("IP == '%s'", ip.text))
        }
        if (port.text != '') {
            result.add(String.format("PORT == '%s'", port.text))
        }
        if (status.text != '') {
            result.add(String.format("STATUS == '%s'", status.text))
        }
        if (description.text != '') {
            result.add(String.format("DESCRIPTION == '%s'", description.text))
        }
        if (plugin.text != '') {
            result.add(String.format("PLUGIN == '%s'", plugin.text))
        }
        if (cvss.text != '') {
            result.add(String.format("CVSS == '%s'", cvss.text))
        }
        if (protocol.text != '') {
            result.add(String.format("PROTOCOL == '%s'", protocol.text))
        }
        if (exploitabe.selected) {
            result.add('EXPLOITABLE')
        }
        if (risk.value != null) {
            result.add(String.format("RISK == '%s'", risk.value))
        }
        if (result.size() > 1) {
            StringBuffer sb = new StringBuffer()
            sb.append('(')
            sb.append(result.join(') AND ('))
            sb.append(')')
            return sb.toString()

        } else {
            return result.size() == 0 ? null : result.get(0)
        }
    }
}
