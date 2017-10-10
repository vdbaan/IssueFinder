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
package net.vdbaan.issuefinder.presenter

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import javafx.beans.InvalidationListener
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.concurrent.Service
import javafx.concurrent.Worker
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.service.FindingService
import net.vdbaan.issuefinder.view.MainView
import net.vdbaan.issuefinder.view.ProgressView

@Log
@CompileStatic
class ProgressPresenter {

    ProgressView progressView
    MainView masterView


    SimpleBooleanProperty finished = new SimpleBooleanProperty(false)
    SimpleDoubleProperty progress = new SimpleDoubleProperty(0)

    // Copied from ProgressBar to avoid IllegalStateException: Toolkit not initialized when running test
    public static final double INDETERMINATE_PROGRESS = -1

    ProgressPresenter(ProgressView view) {
        this.progressView = view

        progressView.bindWorkingProgressIndicator(
                Bindings
                        .when(finished)
                        .then(new SimpleDoubleProperty(1))
                        .otherwise(new SimpleDoubleProperty(INDETERMINATE_PROGRESS)))
        progressView.bindFilesProgressIndicator(progress)
    }

    void process(List<File> fileList) {
        DbHandler handler = progressView.getDbHandler()
        int total = fileList?.size() ?: 0
        int done = 0
        fileList?.each { it ->
            Service<List<Finding>> service = new FindingService(it)
            service.stateProperty().addListener({
                if (service.getState().equals(Worker.State.SUCCEEDED)) {
                    handler.saveFindings(service.getValue())
                    log.fine 'saved issues'
                    done += 1
                    progress.set((Double) (done / total))
                    finished.set(progress == 1)
                    log.fine String.format('done: %d, total: %d', done, total)
                    if (done == total) {
                        masterView.setStatusLabel('Done importing')
                        progressView.dbUpdated()
                        progressView.close()
                    }
                }
            } as InvalidationListener)
            service.start()
        }
    }
}
