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
package net.vdbaan.issuefinder.util

import de.saxsys.javafx.test.JfxRunner
import de.saxsys.javafx.test.TestInJfxThread
import javafx.scene.control.TableView
import javafx.scene.input.Clipboard
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JfxRunner.class)
class TableUtilsTest {

    @Test
    @TestInJfxThread
    void testUtilsWrongKeys() {
        TableView view = new TableView()
        TableUtils.installCopyPasteHandler(view)
        KeyEvent key = new KeyEvent(KeyEvent.KEY_PRESSED, "a", "",
                KeyCode.UNDEFINED, false, false, false, false)
        view.fireEvent(key)
    }

    @Test
    @TestInJfxThread
    void testUtilsRightKeys() {
        TableView view = new TableView()
        TableUtils.installCopyPasteHandler(view)
        KeyEvent key = new KeyEvent(KeyEvent.KEY_PRESSED,"ctrl-C", "",
                KeyCode.C, false, true, false, false)
        key.source = view
        view.getOnKeyPressed().handle(key)
    }
}
