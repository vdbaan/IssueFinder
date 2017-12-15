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
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.vdbaan.issuefinder.db.DbHandler;
import net.vdbaan.issuefinder.db.DbHandlerImpl;
import net.vdbaan.issuefinder.model.Finding;
import net.vdbaan.issuefinder.util.BrowserPopupHandler;
import net.vdbaan.issuefinder.util.Container;
import net.vdbaan.issuefinder.view.EditorView;
import net.vdbaan.issuefinder.view.MainView;
import net.vdbaan.issuefinder.view.ProgressView;
import net.vdbaan.issuefinder.view.SummaryView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MainAppImpl extends Application implements MainApp {
    private Stage primaryStage;

    private MainView mainView;

    public static void startup(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // inits the database
        DbHandler handler = new DbHandlerImpl();
        handler.getAllFinding(null);
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout.fxml"));

        Scene mainScene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Issue Finder");
        primaryStage.setMaximized(true);
        primaryStage.setScene(mainScene);

        mainView = fxmlLoader.getController();
        mainView.setMainApp(this);

        primaryStage.getIcons().add(new Image(MainAppImpl.class.getResourceAsStream("/539822430.jpg")));

        primaryStage.setOnHidden(e -> Platform.exit());
        primaryStage.show();
    }

    public void showAbout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/about.fxml"));

        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setScene(scene);

        dialog.showAndWait();
    }

    public void showProgressDialog(List<File> files) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/progress.fxml"));
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);

        ProgressView controller = loader.getController();
        controller.setFileList(files);
        controller.setMasterView(mainView);
        controller.addDbListener(mainView);

        dialog.showAndWait();
    }

    public void showEditor(List<Finding> data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);

        EditorView controller = loader.getController();
        controller.setEditData(data);

        dialog.showAndWait();
    }

    public void showSummary(Map<String, Container> summary) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/summary.fxml"));
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.setTitle("Summary");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);

        SummaryView controller = loader.getController();
        controller.setSummary(summary);

        dialog.showAndWait();
    }

    public void showSettings() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        Scene scene = new Scene(loader.load());
        dialog.setTitle("Summary");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(scene);

        dialog.showAndWait();
    }

    public void showHelp() {
        Stage dialog = new Stage();
        dialog.setTitle("IssueFinder Help");
        WebView view = new WebView();

        view.getEngine().load(MainAppImpl.class.getResource("/helptext.html").toExternalForm());
        view.getEngine().setCreatePopupHandler(new BrowserPopupHandler(this));
        dialog.initModality(Modality.NONE);
        dialog.setScene(new Scene(view));
        dialog.show();
    }

    public void showWarning(String warning) {
        Alert popup = new Alert(Alert.AlertType.WARNING);
        popup.setTitle("Warning");
        popup.setContentText(warning);
        popup.showAndWait();
    }
}
