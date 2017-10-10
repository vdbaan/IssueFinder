package net.vdbaan.issuefinder.config

import org.junit.Test

import static org.junit.Assert.assertEquals

class ConfigTester {


    @Test
    void presetValues() {
        Config.setInstance(null)
        Object val = Config.getInstance().getProperty(Config.BATCH_SIZE)
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
        }
        Config.setInstance(mock)
        assertEquals(txt,Config.getInstance().getApplicationVersionString())
    }
}