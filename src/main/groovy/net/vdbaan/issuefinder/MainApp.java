/*
 * Copyright (C) 2017 S. van der Baan

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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