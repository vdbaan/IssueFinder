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

import javafx.event.ActionEvent
import net.vdbaan.issuefinder.view.AboutView

import java.util.jar.Manifest

class AboutPresenter {
    private AboutView aboutView

    AboutPresenter(final AboutView view) {
        this.aboutView = view

        String version
        getClass().classLoader.getResources('META-INF/MANIFEST.MF').each { final uri ->
            uri.openStream().withStream { final is ->
                final def attributes = new Manifest(is).mainAttributes
                version = attributes.getValue('version') ?: 'Dev'
            }
        }
        aboutView.setVersion('Version: ' + version)
        aboutView.setCloseAction(this.&close)
    }

    void close(final ActionEvent event) {
        aboutView.close()
    }
}
