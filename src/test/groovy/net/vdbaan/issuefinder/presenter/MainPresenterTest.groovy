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

package net.vdbaan.issuefinder.presenter

import de.saxsys.javafx.test.JfxRunner
import de.saxsys.javafx.test.TestInJfxThread
import javafx.stage.Window
import net.vdbaan.issuefinder.presenter.mock.MainViewMock
import net.vdbaan.issuefinder.util.RetentionFileChooser
import net.vdbaan.issuefinder.view.MainView
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.assertEquals

@RunWith(JfxRunner.class)
class MainPresenterTest {
//
    @Test
    @TestInJfxThread
    void base() {
        MainView mock = new MainViewMock()
        mock.newAction(null)
//        mock.exitApplication(null) // This will kill the JfxRunner, so do not test ;)
    }

    @Test
    @TestInJfxThread
    void process1File() {
        File testFile = new File('testdata/Nessus.nessus')
        MainView mock = new MainViewMock() {

            @Override
            RetentionFileChooser getRetentionFileChooser() {


                return new RetentionFileChooser() {
                    @Override
                    List<File> showOpenMultipleDialog(Window ownerWindow, String title) {
                        return [testFile]
                    }
                }
            }
        }
        mock.loadReport(null)
        assertEquals([testFile], mock.progressFileList)
    }

    @Test
    void testDoFilter() {

    }


}
