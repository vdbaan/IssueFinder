/*
 *  Copyright (C) 2017  S. van der Baan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import net.vdbaan.issuefinder.controller.*;
import net.vdbaan.issuefinder.model.Finding;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainApp extends Application {
    Stage primaryStage;
    private ObservableList<Finding> masterData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout.fxml"));

        Scene mainScene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Issue Finder");
        primaryStage.setMaximized(true);
        primaryStage.setScene(mainScene);


        FXMLController controller = fxmlLoader.getController();
        controller.setMasterData(masterData);
        controller.setup(this);

        primaryStage.show();
    }

    public void showAbout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/about.fxml"));

        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setScene(scene);
        DialogController controller = loader.getController();

        controller.setDialogPane(dialog);
        dialog.showAndWait();
    }

    public void showProgressDialog(List<File> files, Label statusLabel) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/progress.fxml"));
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        ProgressController controller = loader.getController();

        controller.setDialogPane(dialog);
        controller.setStatusLabel(statusLabel);
        controller.setFileList(files);
        controller.setMasterData(masterData);
        controller.process();
        dialog.showAndWait();
    }

    public void showEditor(Finding finding) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        EditorController controller = loader.getController();

        controller.setDialogPane(dialog);
        controller.setMasterData(masterData);
        controller.setFinding(finding);
        controller.setup();
        dialog.showAndWait();
    }

    public void showSummary(Map<String, FXMLController.Container> summary) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/summary.fxml"));
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.setTitle("Summary");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);
        SummaryController controller = loader.getController();
        controller.setSummary(summary);
        controller.setup();
        dialog.showAndWait();
    }

    public static void startup(String[] args) {
        launch(args);
    }
}
