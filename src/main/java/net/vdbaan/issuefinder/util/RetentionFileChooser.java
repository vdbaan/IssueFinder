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
package net.vdbaan.issuefinder.util;

import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public class RetentionFileChooser {
    final private static SimpleObjectProperty<File> lastKnownDirectoryProperty = new SimpleObjectProperty<>();
    private static FileChooser instance = null;

    public RetentionFileChooser() {
    }

    private static FileChooser getInstance() {
        if (instance == null) {
            instance = new FileChooser();
            instance.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);

        }
        return instance;
    }

    public static File showOpenDialog() {
        return showOpenDialog(null);
    }

    private static File showOpenDialog(Window ownerWindow) {
        File chosenFile = getInstance().showOpenDialog(ownerWindow);
        if (chosenFile != null) {
            //Set the property to the directory of the chosenFile so the fileChooser will open here next
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }

    public static File showSaveDialog() {
        return showSaveDialog(null);
    }

    private static File showSaveDialog(Window ownerWindow) {
        File chosenFile = getInstance().showSaveDialog(ownerWindow);
        if (chosenFile != null) {
            //Set the property to the directory of the chosenFile so the fileChooser will open here next
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }

    public List<File> showOpenMultipleDialog(final Window ownerWindow) {
        List<File> result = getInstance().showOpenMultipleDialog(ownerWindow);
        if (result != null) {
            lastKnownDirectoryProperty.setValue(result.get(0).getParentFile());
        }
        return result;
    }
}