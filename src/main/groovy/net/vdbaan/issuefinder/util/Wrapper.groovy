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
package net.vdbaan.issuefinder.util

import groovy.transform.CompileStatic
import javafx.beans.property.SimpleStringProperty

@CompileStatic
class Wrapper {
    SimpleStringProperty key
    SimpleStringProperty value

    def setKey(final String key) {
        if (this.key == null) {
            this.key = new SimpleStringProperty(key)
        } else {
            this.key.set(key)
        }
    }

    def setValue(final String value) {
        if (this.value == null) {
            this.value = new SimpleStringProperty(value)
        } else {
            this.value.set(value)
        }
    }

    String getKey() {
        return key.get()
    }

    String getValue() {
        return value.get()
    }
}