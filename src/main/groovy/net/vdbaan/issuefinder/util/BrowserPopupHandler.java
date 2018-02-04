package net.vdbaan.issuefinder.util;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.util.Callback;

/**
 * Code base on:
 * https://stackoverflow.com/questions/15555510/javafx-stop-opening-url-in-webview-open-in-browser-instead#18536564
 */
public class BrowserPopupHandler implements Callback<PopupFeatures, WebEngine> {

    Application application;
    private WebEngine popupHandlerEngine;

    public BrowserPopupHandler(Application mainApp) {
        this.application = mainApp;
    }

    public WebEngine call(PopupFeatures popupFeatures) {
        // by returning null here the action would be canceled
        // by returning a different WebEngine (than the main one where we register our listener) the load-call will go to that one
        // we return a different WebEngine here and register a location change listener on it (see blow)
        return getPopupHandler();
    }

    private WebEngine getPopupHandler() {
        synchronized (this) // double checked synchronization
        {
            if (popupHandlerEngine == null) {
                popupHandlerEngine = initEngine();
            }
        }
        return popupHandlerEngine;
    }

    private WebEngine initEngine() {
        final WebEngine popupHandlerEngine = new WebEngine();

        // this change listener will trigger when our secondary popupHandlerEngine starts to load the url ...
        popupHandlerEngine.locationProperty().addListener((observable, oldValue, location) -> {
            if (!location.isEmpty()) {
                Platform.runLater(() -> {
                    popupHandlerEngine.loadContent(""); // stop loading and unload the url
                    // -> does this internally: popupHandlerEngine.getLoadWorker().cancelAndReset();
                });

                try {

                    HostServices hostServices = application.getHostServices();
                    hostServices.showDocument(location);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return popupHandlerEngine;
    }

}

