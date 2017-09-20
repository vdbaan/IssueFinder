package net.vdbaan.issuefinder

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import net.vdbaan.issuefinder.controller.DialogController
import net.vdbaan.issuefinder.controller.EditorController
import net.vdbaan.issuefinder.controller.FXMLController
import net.vdbaan.issuefinder.controller.ProgressController
import net.vdbaan.issuefinder.model.Finding


import static groovyx.javafx.GroovyFX.start

class MainApp {
    Stage primaryStage
    private ObservableList<Finding> masterData = FXCollections.observableArrayList()

    def run(String... args) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout.fxml"))
        start {
            primaryStage = stage(title: 'Issue Finder', width: 1200, height: 500, visible: true, maximized: true) {
                def mainScene = scene() {
                }
                mainScene.root = fxmlLoader.load()
            }
            FXMLController controller = fxmlLoader.getController()
            controller.setMasterData(masterData)
            controller.setup(this)
//            controller.openFiles(args.collect { new File(it) })
        }
    }

    def showAbout() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/about.fxml"))
        Parent root = loader.load()
        Stage dialog = new Stage()
        dialog.initOwner(primaryStage)
        Scene scene = new Scene(root)
        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.setScene(scene)
        DialogController controller = loader.getController()
        controller.dialogPane = dialog
        dialog.showAndWait()
    }

    void showProgressDialog(List<File> files, Label statusLabel) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/progress.fxml"))
        Parent root = loader.load()
        Stage dialog = new Stage()
        dialog.initOwner(primaryStage)
        Scene scene = new Scene(root)
        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.initModality(Modality.APPLICATION_MODAL)
        dialog.setScene(scene)
        ProgressController controller = loader.getController()
        controller.dialogPane = dialog
        controller.statusLabel = statusLabel
        controller.fileList = files
        controller.masterData = masterData
        controller.process()
        dialog.showAndWait()
    }

    void showEditor(Finding finding) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"))
        Parent root = loader.load()
        Stage dialog = new Stage()
        dialog.initOwner(primaryStage)
        Scene scene = new Scene(root)
        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.initModality(Modality.APPLICATION_MODAL)
        dialog.setScene(scene)
        EditorController controller = loader.getController()
        controller.dialogPane = dialog
        controller.masterData = masterData
        controller.finding = finding
        controller.setup()
        dialog.showAndWait()
    }


}
