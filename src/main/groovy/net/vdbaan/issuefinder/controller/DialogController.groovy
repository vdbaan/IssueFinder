package net.vdbaan.issuefinder.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.stage.Stage

import java.util.jar.Manifest


class DialogController implements Initializable {

    Stage dialogPane

    @FXML
    Label version

    @Override
    void initialize(URL url, ResourceBundle rb) {
        def vers
        getClass().classLoader.getResources('META-INF/MANIFEST.MF').each { uri ->
            uri.openStream().withStream { is ->
                def attributes = new Manifest(is).mainAttributes
                vers = attributes.getValue('version')
            }
        }
        version.text = (vers == null)?'Version: DEV':'Version: '+vers
    }
    def closeAction() {
        dialogPane.hide()
    }
}
