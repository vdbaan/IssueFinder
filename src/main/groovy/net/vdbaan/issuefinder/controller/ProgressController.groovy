package net.vdbaan.issuefinder.controller

import javafx.beans.InvalidationListener
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.stage.Stage
import net.vdbaan.issuefinder.model.Finding

class ProgressController  implements Initializable {
    @FXML
    ProgressBar working
    @FXML
    ProgressBar files
    Label statusLabel
    List<File> fileList
    Stage dialogPane
    ObservableList<Finding> masterData


    SimpleBooleanProperty finished = new SimpleBooleanProperty(false)
    SimpleDoubleProperty progress = new SimpleDoubleProperty(0)

    @Override
    void initialize(URL url, ResourceBundle rb) {
        files.progressProperty().bind(progress)
        working.progressProperty().bind(
                Bindings
                .when(finished)
                .then(new SimpleDoubleProperty(1))
                .otherwise(new SimpleDoubleProperty(ProgressBar.INDETERMINATE_PROGRESS)))
    }

    def process() {
        int total = fileList.size()
        int done = 0
        fileList.each{ it ->
            Service<ObservableList<Finding>> service = new FindingService(it)
            service.stateProperty().addListener({
                if (service.getState().equals(Worker.State.SUCCEEDED)) {
                    masterData.addAll(service.getValue())
                    done += 1
                    progress.set((Double)(done/total))
                    finished.set(progress == 1)
                    if(done == total) {
                        dialogPane.hide()
                        statusLabel.setText('Done importing')
                    }
                }
            } as InvalidationListener)
            service.start()
        }
    }
}

/*
        statusLabel.setText('Processing files')
        int total = files.size()
        int done = 0
        SimpleDoubleProperty progress = new SimpleDoubleProperty(0)
        SimpleBooleanProperty finished = new SimpleBooleanProperty(false)
        Stage dialog = new Stage()
        GridPane progressPane = new GridPane()
        progressPane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: cornsilk;")
        progressPane.setPadding(new Insets(5,5,5,5))
        progressPane.setHgap(5)
        progressPane.setVgap(5)
        Label lbl = new Label('Processing new files')
        lbl.setFont(Font.font(24))

        progressPane.add(lbl,0,0,2,1)
        progressPane.addRow(1, new Label("File:     :"),     createBoundProgressBar(progress))
        progressPane.addRow(2, new Label("Processing:"),
                createBoundProgressBar(
                        Bindings
                                .when(finished)
                                .then(new SimpleDoubleProperty(1))
                                .otherwise(new SimpleDoubleProperty(ProgressBar.INDETERMINATE_PROGRESS))
                )
        )


        dialog.initOwner(mainTable.getScene().getWindow())
        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.setScene(new Scene(progressPane))
        dialog.initStyle(StageStyle.UNDECORATED)
        dialog.show()
        files.each { it ->
            println 'Working on '+it.getAbsolutePath()
            Service<ObservableList<Finding>> service = new FindingService(it)
            service.stateProperty().addListener({
                if (service.getState().equals(Worker.State.SUCCEEDED)) {
                    println 'Done parsing'
                    masterData.addAll(service.getValue())
                    done += 1
                    progress.set((Double)(done/total))
                    finished.set(progress == 1)
                    println String.format("%d of %d to go",done,total)
                    if(done == total) {
                        dialog.hide()
                        statusLabel.setText('Done importing')
                    }
                }
            } as InvalidationListener)
            println 'Starting service'
            service.start()
        }

            private ProgressBar createBoundProgressBar(NumberExpression progressProperty) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefWidth(400.0)
        progressBar.progressProperty().bind(progressProperty);
        GridPane.setHgrow(progressBar, Priority.ALWAYS);
        return progressBar;
    }
 */