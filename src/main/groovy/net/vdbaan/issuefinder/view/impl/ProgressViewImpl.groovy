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
package net.vdbaan.issuefinder.view.impl

import groovy.transform.CompileStatic
import javafx.beans.property.SimpleDoubleProperty
import javafx.fxml.FXML
import javafx.scene.control.ProgressBar
import net.vdbaan.issuefinder.presenter.ProgressPresenter
import net.vdbaan.issuefinder.view.MainView
import net.vdbaan.issuefinder.view.ProgressView

@CompileStatic
class ProgressViewImpl extends AbstractProgressView implements ProgressView {
    @FXML
    ProgressBar working
    @FXML
    ProgressBar files



    @FXML
    void initialize() {
        presenter = new ProgressPresenter(this)
    }

    void bindWorkingProgressIndicator(javafx.beans.binding.Binding binding) {
        working.progressProperty().bind(binding)
    }

    void bindFilesProgressIndicator(SimpleDoubleProperty binding) {
        files.progressProperty().bind(binding)
    }


}

abstract class AbstractProgressView extends BaseViewImpl implements ProgressView {
    ProgressPresenter presenter

    void setFileList(List<File> files) {
        presenter.process(files)
    }

    @Override
    void setMasterView(MainView view) {
        presenter.setMasterView(view)
    }
}