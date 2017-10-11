/**
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
package net.vdbaan.issuefinder.config

import org.junit.Test

import static org.junit.Assert.assertEquals

class ConfigTester {


    @Test
    void presetValues() {
        Config.setInstance(null)
//        Object val = Config.getInstance().getProperty(Config.BATCH_SIZE)
        assertEquals('Wrong value', 100, (Integer)Config.getInstance().getProperty(Config.BATCH_SIZE))
    }

    @Test
    void saveData() {
        Config.setInstance(null)
        String value = 'test'
        String key = 'testKey'
        Config.getInstance().setProperty(key, value)
        Config.getInstance().saveConfig()
        Config.getInstance().loadConfig()
        assertEquals('Wrong value', value, Config.getInstance().getProperty(key))
        Config.getInstance().setProperty(key,null)
        Config.getInstance().saveConfig()
        Config.getInstance().loadConfig()
        assertEquals('key and value still there', null, Config.getInstance().getProperty(key))
    }

    @Test
    void testMock() {
        String txt = 'nothing here'
        Config mock = new Config() {

            @Override
            void attachShutDownHook() {

            }

            @Override
            void saveConfig() {

            }

            @Override
            void loadConfig() {

            }

            @Override
            String getApplicationVersionString() {
                return txt
            }

            @Override
            String getUserDataDirectory() {
                return null
            }

            @Override
            void checkDataDirectory() {

            }

            @Override
            Object getProperty(String propertyName) {
                return super.getProperty(propertyName)
            }

            @Override
            void setProperty(String propertyName, Object newValue) {
                super.setProperty(propertyName, newValue)
            }

            @Override
            boolean hasPropertyFor(String key) {
                return false
            }
        }
        Config.setInstance(mock)
        assertEquals(txt,Config.getInstance().getApplicationVersionString())
    }
}