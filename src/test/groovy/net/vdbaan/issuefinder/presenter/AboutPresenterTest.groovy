/**
 * Copyright (C) 2017 S. van der Baan

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.vdbaan.issuefinder.presenter

import net.vdbaan.issuefinder.view.AboutView
import net.vdbaan.issuefinder.view.impl.AbstractAboutView
import org.junit.Test

import static org.junit.Assert.assertEquals

class AboutPresenterTest {


    @Test
    void getVersionString() {

        AboutView mock = new AboutViewMock()
        assertEquals('Wrong result', 'Version: Dev', mock.getVersion())
        mock.getCloseAction().handle(null)
    }
}

class AboutViewMock extends AbstractAboutView {
    AboutPresenter presenter
    String mockVersion

    AboutViewMock() {
        this.presenter = new AboutPresenter(this)
    }

    @Override
    void setVersion(String version) {
        this.mockVersion = version
    }

    String getVersion() {
        return mockVersion
    }
}