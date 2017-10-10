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
package net.vdbaan.issuefinder.view

import groovy.transform.CompileStatic
import javafx.beans.property.SimpleDoubleProperty

@CompileStatic
interface ProgressView extends BaseView {
//    ProgressIndicator getWorkingIndicator()
//
//    ProgressIndicator getFilesIndicator()

    void bindWorkingProgressIndicator(javafx.beans.binding.Binding binding)

    void bindFilesProgressIndicator(SimpleDoubleProperty binding)

    void setFileList(List<File> files)
}