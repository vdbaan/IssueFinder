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
package net.vdbaan.issuefinder.service

import de.saxsys.javafx.test.JfxRunner
import de.saxsys.javafx.test.TestInJfxThread
import javafx.beans.InvalidationListener
import javafx.concurrent.Worker
import net.vdbaan.issuefinder.model.Finding
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.assertEquals

@RunWith(JfxRunner.class)
class FindingServiceTest {
    @Test
    @TestInJfxThread
    void testService() {
        File testFile = new File('.', '/testdata/Nessus.nessus')
        FindingService service = new FindingService(testFile)
        assertEquals('Got the wrong file', testFile, service.file)

        service.stateProperty().addListener({
            if (service.getState().equals(Worker.State.SUCCEEDED)) {
                Object result = service.getValue()
                assert result instanceof List<Finding>
            }
        } as InvalidationListener)

        service.start()
    }
}
