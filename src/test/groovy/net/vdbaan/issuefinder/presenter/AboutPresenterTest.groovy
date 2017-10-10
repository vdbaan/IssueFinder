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