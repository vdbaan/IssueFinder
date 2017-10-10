package net.vdbaan.issuefinder;

import net.vdbaan.issuefinder.model.Finding;
import net.vdbaan.issuefinder.util.Container;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MainApp {
    void showProgressDialog(List<File> files) throws IOException;

    void showAbout() throws IOException;

    void showEditor(List<Finding> data) throws IOException;

    void showSummary(Map<String, Container> summary) throws IOException;

    void showSettings() throws IOException;
}