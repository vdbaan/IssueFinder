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
package net.vdbaan.issuefinder.service

import groovy.transform.CompileStatic
import javafx.concurrent.Service
import javafx.concurrent.Task
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.parser.Parser

@CompileStatic
class FindingService extends Service<List<Finding>> {
    File file

    FindingService(File file) {
        this.file = file
    }

    @Override
    protected Task<List<Finding>> createTask() {
        return new Task<List<Finding>>() {
            @Override
            protected List<Finding> call() {
                List<Finding> answer = new ArrayList<>()
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
