/*
 *  Copyright (C) 2017  S. van der Baan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder.view.impl

import groovy.util.logging.Log
import javafx.beans.InvalidationListener
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.concurrent.Service
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control.ProgressBar
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.db.DbHandlerImpl
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.service.FindingService
import net.vdbaan.issuefinder.view.MainView
import net.vdbaan.issuefinder.view.ProgressDialogView

@Log
class ProgressDialogViewImpl implements ProgressDialogView {
    @FXML
    // fx:id="files"
    private ProgressBar files; // Value injected by FXMLLoader

    @FXML
    // fx:id="working"
    private ProgressBar working; // Value injected by FXMLLoader

    @FXML
    void initialize() {
        working.progressProperty().bind(
                Bindings
                        .when(finished)
                        .then(new SimpleDoubleProperty(1))
                        .otherwise(new SimpleDoubleProperty(INDETERMINATE_PROGRESS)))
        files.progressProperty().bind(progress)
    }

    @Override
    void setFileList(final List<File> fileList) {
        final DbHandler handler = new DbHandlerImpl()
        final int total = fileList?.size() ?: 0
        int done = 0
        fileList?.each { final file ->
            final Service<List<Finding>> service = new FindingService(file)
            service.stateProperty().addListener({
                if (service.state.equals(Worker.State.SUCCEEDED)) {
                    handler.saveFindings(service.value)
                    log.fine 'saved issues'
                    done += 1
                    progress.set((Double) (done / total))
                    finished.set(progress.get() == 1)
                    log.fine String.format('done: %d, total: %d', done, total)
                    if (done == total) {
                        masterView.statusLabel = 'Done importing'
                        callable.call()
                        files?.scene?.window?.hide()
                    }
                }
            } as InvalidationListener)
            service.start()
        }
    }

    Closure callable

    void setCallback(final Closure callback) {
        callable = callback
    }
    SimpleBooleanProperty finished = new SimpleBooleanProperty(false)
    SimpleDoubleProperty progress = new SimpleDoubleProperty(0)
    public static final double INDETERMINATE_PROGRESS = -1
    private MainView masterView

    @Override
    void setMasterView(final MainView mainView) {
        this.masterView = mainView
    }
}
