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

import javafx.scene.control.TreeItem
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.Container
import net.vdbaan.issuefinder.util.Wrapper
import net.vdbaan.issuefinder.view.SummaryView
import net.vdbaan.issuefinder.view.impl.AbstractSummaryView
import org.junit.Test

import static org.junit.Assert.assertEquals

class SummaryPresenterTest {

    @Test
    void testSetup() {
        SummaryView mock = new SummaryViewMock()
        mock.setSummary(new HashMap<String, Container>())
        TreeItem<Wrapper> root = mock.getTreeRoot()

        assertEquals("IPs (0)", root.getValue().getKey())
    }

    @Test
    void testSummary() {
        Container c = new Container()
        c.listedports << "80"
        c.listedservices << "html"
        c.highest = Finding.Severity.CRITICAL
        c.plugins << "Some HTML plugin"
        Map<String, Container> summaryInfo = new HashMap()
        summaryInfo.put("127.0.0.1", c)
        SummaryView mock = new SummaryViewMock()
        mock.setSummary(summaryInfo)

        TreeItem<Wrapper> root = mock.getTreeRoot()
        assertEquals("IPs (1)", root.getValue().getKey())
        assertEquals(1, root.getChildren().size())
        TreeItem<Wrapper> child = root.getChildren().get(0)
        assertEquals("127.0.0.1", child.getValue().getKey())
        child = child.getChildren().get(0)
        assertEquals("open ports (1)", child.getValue().getKey())
    }
}

class SummaryViewMock extends AbstractSummaryView implements SummaryView {

    TreeItem treeRoot

    SummaryViewMock() {
        this.presenter = new SummaryPresenter(this)
    }

    @Override
    void setTreeRoot(TreeItem root) {
        this.treeRoot = root
    }

    @Override
    TreeItem getTreeRoot() {
        return treeRoot
    }
}
