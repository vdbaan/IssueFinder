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

import de.saxsys.javafx.test.JfxRunner
import de.saxsys.javafx.test.TestInJfxThread
import javafx.beans.binding.Binding
import javafx.beans.property.SimpleDoubleProperty
import net.vdbaan.issuefinder.view.ProgressView
import net.vdbaan.issuefinder.view.impl.AbstractProgressView
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JfxRunner.class)
class ProgressPresenterTest {
    @Test
    void testSetup() {
        ProgressView mock = new ProgressViewMock()
        mock.setFileList(null)
    }

    @Test
    @TestInJfxThread
    void processOneFile() {
        ProgressView mock = new ProgressViewMock()
        mock.setFileList([new File('testdata/Nessus.nessus')])
    }
}

class ProgressViewMock extends AbstractProgressView implements ProgressView{
    ProgressViewMock() {
        presenter = new ProgressPresenter(this)
        presenter.setMasterView(new MainViewMock())
    }

    @Override
    void bindWorkingProgressIndicator(Binding binding) {

    }

    @Override
    void bindFilesProgressIndicator(SimpleDoubleProperty binding) {

    }
}