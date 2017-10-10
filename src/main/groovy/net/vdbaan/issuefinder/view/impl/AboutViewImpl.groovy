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
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Label
import net.vdbaan.issuefinder.presenter.AboutPresenter
import net.vdbaan.issuefinder.view.AboutView

@CompileStatic
class AboutViewImpl extends AbstractAboutView implements AboutView {
    @FXML
    Label version

    void setVersion(String version) {
        this.version.text = version
    }

    @FXML
    void initialize() {
        presenter = new AboutPresenter(this)
    }

    @FXML
    void close(ActionEvent event) {
        closeAction?.handle(event)

    }

}

abstract class AbstractAboutView extends BaseViewImpl implements AboutView {

    AboutPresenter presenter

    private EventHandler<ActionEvent> closeAction


    EventHandler<ActionEvent> getCloseAction() {
        return closeAction
    }

    void setCloseAction(EventHandler<ActionEvent> closeAction) {
        this.closeAction = closeAction
    }
}