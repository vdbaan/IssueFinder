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

import javafx.fxml.FXML
import javafx.scene.Parent
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.db.DbHandlerImpl
import net.vdbaan.issuefinder.db.DbListener
import net.vdbaan.issuefinder.view.BaseView
import net.vdbaan.issuefinder.view.LayoutView


class BaseViewImpl implements BaseView {

    List<DbListener> listeners = new ArrayList()
    @FXML
    Parent root


    void close() {
        root?.getScene()?.getWindow()?.hide()
    }

    void addDbListener(final DbListener listener) {
        listeners.add(listener)
    }

    void dbUpdated() {
        listeners?.each {
            it.dbUpdated()
        }
    }

    void setMasterView(final LayoutView view) {
    }

    DbHandler getDbHandler() {
        return new DbHandlerImpl()
    }
}
