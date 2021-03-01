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
package net.vdbaan.issuefinder

import groovy.transform.CompileStatic
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

@CompileStatic
class Main2 extends Application {

    @SuppressWarnings('PrivateFieldCouldBeFinal')
    private static Main2 instance

    Main2() {
        instance = this
    }

    @SuppressWarnings('UnusedMethodParameter') // Is used by the Application
    static void startup(String[] args) {
        Application.launch(Main2)
    }

    synchronized static Main2 getInstance() {
        if (!instance) {
            Thread.start {
                Application.launch(Main2)
            }
            while (!instance) {
                Thread.sleep(100)
            }
        }
        return instance
    }

    @Override
    void start(final Stage primaryStage) throws Exception {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource('/main.fxml'))
        final Scene mainScene = new Scene(fxmlLoader.load() as Parent)
        primaryStage.title = 'Issue Finder'
        primaryStage.maximized = true
        primaryStage.scene = mainScene
        primaryStage.icons.add(new Image(getClass().getResourceAsStream('/539822430.jpg')))
        primaryStage.onHidden = { final exit -> Platform.exit() }
        primaryStage.show()
    }

}
