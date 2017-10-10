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
        mock.setSummary(new HashMap<String, Container> ())
        TreeItem<Wrapper> root = mock.getTreeRoot()

        assertEquals("IPs (0)",root.getValue().getKey())
    }

    @Test
    void testSummary() {
        Container c = new Container()
        c.listedports << "80"
        c.listedservices << "html"
        c.highest = Finding.Severity.CRITICAL
        c.plugins << "Some HTML plugin"
        Map<String, Container> summaryInfo = new HashMap()
        summaryInfo.put("127.0.0.1",c)
        SummaryView mock = new SummaryViewMock()
        mock.setSummary(summaryInfo)

        TreeItem<Wrapper> root = mock.getTreeRoot()
        assertEquals("IPs (1)",root.getValue().getKey())
        assertEquals(1,root.getChildren().size())
        TreeItem<Wrapper> child = root.getChildren().get(0)
        assertEquals("127.0.0.1",child.getValue().getKey())
        child = child.getChildren().get(0)
        assertEquals("open ports (1)",child.getValue().getKey())
    }
}

class SummaryViewMock extends AbstractSummaryView implements SummaryView{

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
