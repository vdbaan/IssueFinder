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
    }

    @Override
    void bindWorkingProgressIndicator(Binding binding) {

    }

    @Override
    void bindFilesProgressIndicator(SimpleDoubleProperty binding) {

    }
}