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

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import net.vdbaan.issuefinder.view.AboutDialogView

class AboutDialogViewImpl implements AboutDialogView {

    @FXML
    // fx:id="root"
    private AnchorPane root; // Value injected by FXMLLoader

    @FXML
    // fx:id="version"
    private Label version; // Value injected by FXMLLoader


    void setVersion(final String text) {
        version.text = text
    }
}
