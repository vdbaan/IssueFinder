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

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import net.vdbaan.issuefinder.model.Finding

import java.util.jar.Attributes
import java.util.jar.Manifest

abstract class Config {

    static private Config configInstance = null

    final static String MAX_ROWS = 'maxRows'
    final static String FILTERS = 'filters'
    final static String PRELOAD_FILTER = 'preloadFilter'
    final static String BATCH_SIZE = 'batchSize'
    final static String IP_PORT_FORMAT_STRING = 'ipPortFormatString'
    static String CONFIGFILE_NAME = "issueFinder-VERSION.config"
    final static String DATA_SOURCE = 'datasource'
    final static String DB_NAME = 'databaseName'
    final static String DATA_DIR = 'dataDirectory'
    final static String COLOURED_ROWS = 'colouredRows'

    static Config getInstance() {

        if (configInstance == null) {
            configInstance = new ConfigImpl()
        }
        return configInstance
    }

    static void setInstance(Config instance) {
        configInstance = instance
    }

    abstract Object getProperty(String key)

    abstract void setProperty(String key, Object value)

    abstract boolean hasPropertyFor(String key)

    abstract void attachShutDownHook()

    abstract void saveConfig()

    abstract void loadConfig()

    abstract String getApplicationVersionString()

    abstract String getUserDataDirectory()

    abstract void checkDataDirectory()
}


@Log
@CompileStatic
class ConfigImpl extends Config {

    String configVersion
    ConfigObject configObject = new ConfigObject()

    ConfigImpl() {
        CONFIGFILE_NAME = CONFIGFILE_NAME.replace('VERSION', getApplicationVersionString())
        configObject.put(MAX_ROWS, 5000)
        configObject.put(FILTERS, ['IP == "127.0.0.1"', 'SCANNER == \'nmap\'', 'SERVICE LIKE \'http\'', 'PORT LIKE 443', '!EXPLOITABLE', '(SERVICE LIKE \'SMB\') && EXPLOITABLE'])
        configObject.put(PRELOAD_FILTER, [(Finding.Severity.CRITICAL): true, (Finding.Severity.HIGH): true,
                                          (Finding.Severity.MEDIUM)  : true, (Finding.Severity.LOW): true, (Finding.Severity.INFO): true])
        configObject.put(BATCH_SIZE, 100)
        configObject.put(IP_PORT_FORMAT_STRING, 'IP:PORT')
        configObject.put(COLOURED_ROWS, true)
        configObject.put(DATA_DIR, System.getProperty("user.home") + '/.issuefinder/data/')
        configObject.put(DB_NAME, 'issueDB')

        configObject.put(DATA_SOURCE, [database: 'jdbc:h2:' + configObject.get(DATA_DIR) + configObject.get(DB_NAME), user: 'sa', password: ''])
    }

    @Override
    Object getProperty(String key) {
        return configObject?.get(key)
    }

    @Override
    void setProperty(String key, Object value) {
        if (value == null) {
            configObject.remove(key)
        } else {
            configObject.put(key, value)
        }
    }

    @Override
    boolean hasPropertyFor(String key) {
        return configObject.containsKey(key)
    }

    @Override
    void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            void run() {
                getInstance().saveConfig()
            }
        })
    }

    @Override
    void saveConfig() {
        File configFile = new File(getUserDataDirectory(), CONFIGFILE_NAME)
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
        }
        ConfigObject saver = configObject.clone()
        saver.keySet().each { key ->
            if (key.toString().startsWith('VOLATILE')) {
                saver.remove(key)
            }
        }
        new StringWriter().with { sw ->
            saver.writeTo(sw)
            configFile.write(sw.toString())
        }
    }

    @Override
    void loadConfig() {
        File configFile = new File(getUserDataDirectory(), CONFIGFILE_NAME)
        if (configFile.exists()) {
            configObject = new ConfigSlurper().parse(configFile.text)
            Map<String, Boolean> preload_filter = (Map) getInstance().getProperty(PRELOAD_FILTER)
            Map<Finding.Severity, Boolean> parsed = new HashMap<>()
            parsed.put(Finding.Severity.CRITICAL, preload_filter.get(Finding.Severity.CRITICAL.toString()))
            parsed.put(Finding.Severity.HIGH, preload_filter.get(Finding.Severity.HIGH.toString()))
            parsed.put(Finding.Severity.MEDIUM, preload_filter.get(Finding.Severity.MEDIUM.toString()))
            parsed.put(Finding.Severity.LOW, preload_filter.get(Finding.Severity.LOW.toString()))
            parsed.put(Finding.Severity.INFO, preload_filter.get(Finding.Severity.INFO.toString()))
            configObject.put(PRELOAD_FILTER, parsed)
            // TODO Find a way to merge with original configObject
        }
    }


    String getApplicationVersionString() {
        Manifest mf = new Manifest()
        mf.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"))
        Attributes atts = mf.getMainAttributes()
        configVersion = atts.getValue('Version') ?: 'DEVELOPMENT'
        log.fine('Version: ' + configVersion)
        return configVersion
    }

    String getUserDataDirectory() {
        return System.getProperty("user.home") + File.separator + ".issuefinder" + File.separator + 'config' + File.separator
    }

    @Override
    void checkDataDirectory() {
        log.info('Checking data dir')
        File directory = new File(configObject.getProperty(DATA_DIR).toString())
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }
}
