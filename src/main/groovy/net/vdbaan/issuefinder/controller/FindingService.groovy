package net.vdbaan.issuefinder.controller

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.concurrent.Service
import javafx.concurrent.Task
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.Parser

class FindingService extends Service<ObservableList<Finding>> {
    File file

    FindingService(File file) {
        this.file = file
    }
    @Override
    protected Task<ObservableList<Finding>> createTask() {
        return new Task<ObservableList<Finding>>() {
            @Override
            protected ObservableList<Finding> call() {
                ObservableList<Finding> answer = FXCollections.observableArrayList()
                try {
                    Parser p = Parser.getParser(file.getText())
                    answer.addAll(p.parse())
                } finally {
                    return answer
                }
            }
        }
    }
}
