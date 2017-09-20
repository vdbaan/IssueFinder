package net.vdbaan.issuefinder;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.vdbaan.issuefinder.controller.DialogController;
import net.vdbaan.issuefinder.controller.EditorController;
import net.vdbaan.issuefinder.controller.FXMLController;
import net.vdbaan.issuefinder.controller.ProgressController;
import net.vdbaan.issuefinder.model.Finding;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainApp extends Application {
    Stage primaryStage;
    private ObservableList<Finding> masterData = FXCollections.observableArrayList();
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout.fxml"));

        VBox vbox = fxmlLoader.load();
        Scene mainScene = new Scene(vbox);

        primaryStage.setTitle("Issue Finder");
        primaryStage.setMaximized(true);
        primaryStage.setScene(mainScene);

        FXMLController controller = fxmlLoader.getController();
        controller.setMasterData(masterData);
        controller.setup(this);

        primaryStage.show();
    }

    public void showAbout()  throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/about.fxml"));
        Parent root = loader.load();
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(root);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setScene(scene);
        DialogController controller = loader.getController();
//        controller.dialogPane = dialog;
        controller.setDialogPane(dialog);
        dialog.showAndWait();
    }

    public void showProgressDialog(List<File> files, Label statusLabel)  throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/progress.fxml"));
        Parent root = loader.load();
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(root);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        ProgressController controller = loader.getController();
//        controller.dialogPane = dialog;
//        controller.statusLabel = statusLabel;
//        controller.fileList = files;
//        controller.masterData = masterData;
        controller.setDialogPane(dialog);
        controller.setStatusLabel(statusLabel);
        controller.setFileList(files);
        controller.setMasterData(masterData);
        controller.process();
        dialog.showAndWait();
    }

    public void showEditor(Finding finding) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
        Parent root = loader.load();
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(root);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        EditorController controller = loader.getController();
//        controller.dialogPane = dialog;
//        controller.masterData = masterData;
//        controller.finding = finding;
        controller.setDialogPane(dialog);
        controller.setMasterData(masterData);
        controller.setFinding(finding);
        controller.setup();
        dialog.showAndWait();
    }

    public static void startup(String[] args) {
        launch(args);
    }
}
