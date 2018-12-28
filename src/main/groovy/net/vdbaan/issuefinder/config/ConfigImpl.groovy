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
    static final String ISSUE_LIST = 'issueList'

    static Config getInstance() {

        if (configInstance == null) {
            configInstance = new ConfigImpl()
        }
        return configInstance
    }

    static void setInstance(final Config instance) {
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
        CONFIGFILE_NAME = CONFIGFILE_NAME.replace('VERSION', applicationVersionString)
        configObject[MAX_ROWS] = 5000
        configObject[FILTERS] = ['IP == "127.0.0.1"', 'SCANNER == \'nmap\'', 'SERVICE LIKE \'http\'', 'PORT LIKE 443', '!EXPLOITABLE', '(SERVICE LIKE \'SMB\') && EXPLOITABLE']
        configObject.put(PRELOAD_FILTER, [(Finding.Severity.CRITICAL): true, (Finding.Severity.HIGH): true,
                                          (Finding.Severity.MEDIUM)  : true, (Finding.Severity.LOW): true, (Finding.Severity.INFO): true])
        configObject[BATCH_SIZE] = 100
        configObject[IP_PORT_FORMAT_STRING] = '$ip:$port'
        configObject[COLOURED_ROWS] = true
        configObject[DATA_DIR] = System.getProperty("user.home") + '/.issuefinder/data/'
        configObject[DB_NAME] = 'issueDB'

        configObject.put(DATA_SOURCE, [database: 'jdbc:h2:' + configObject.get(DATA_DIR) + configObject.get(DB_NAME), user: 'sa', password: ''])
    }

    @Override
    Object getProperty(final String key) {
        return configObject?.get(key)
    }

    @Override
    void setProperty(final String key, final Object value) {
        if (value == null) {
            configObject.remove(key)
        } else {
            configObject[key] = value
        }
    }

    @Override
    boolean hasPropertyFor(final String key) {
        return configObject.containsKey(key)
    }

    @Override
    void attachShutDownHook() {
        Runtime.runtime.addShutdownHook(new Thread() {
            @Override
            void run() {
                instance.saveConfig()
            }
        })
    }

    @Override
    void saveConfig() {
        final File configFile = new File(userDataDirectory, CONFIGFILE_NAME)
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
        }
        final ConfigObject saver = configObject.clone()
        saver.keySet().each { final key ->
            if (key.toString().startsWith('VOLATILE')) {
                saver.remove(key)
            }
        }
        new StringWriter().with { final stringWriter ->
            saver.writeTo(stringWriter)
            configFile.write(stringWriter.toString())
        }
    }

    @Override
    void loadConfig() {
        final File configFile = new File(userDataDirectory, CONFIGFILE_NAME)
        if (configFile.exists()) {
            configObject = new ConfigSlurper().parse(configFile.text)
        } else {
            List filters = new ArrayList()
            ConfigSlurper slurper = new ConfigSlurper()
            (new File(userDataDirectory)).eachFile { config ->
                ConfigObject cfg = slurper.parse(config.text)
                // collect all predefined filters
                filters.addAll(cfg.get(Config.FILTERS) as List)
                // keep the (usually) last version
                configObject = cfg

                // not yet, maybe in one of the next versions
//                config.deleteOnExit()
            }
            configObject.put(FILTERS, filters.unique())
        }
        final Map<String, Boolean> preload_filter = (Map) instance.getProperty(PRELOAD_FILTER)
        final Map<Finding.Severity, Boolean> parsed = new HashMap<>()
        parsed[Finding.Severity.CRITICAL] = preload_filter[Finding.Severity.CRITICAL.toString()]
        parsed[Finding.Severity.HIGH] = preload_filter[Finding.Severity.HIGH.toString()]
        parsed[Finding.Severity.MEDIUM] = preload_filter[Finding.Severity.MEDIUM.toString()]
        parsed[Finding.Severity.LOW] = preload_filter[Finding.Severity.LOW.toString()]
        parsed[Finding.Severity.INFO] = preload_filter[Finding.Severity.INFO.toString()]
        configObject.put(PRELOAD_FILTER, parsed)
    }


    String getApplicationVersionString() {
        final Manifest manifest = new Manifest()
        manifest.read(Thread.currentThread().contextClassLoader.getResourceAsStream("META-INF/MANIFEST.MF"))
        final Attributes attributes = manifest.mainAttributes
        configVersion = attributes['Version'] ?: 'DEVELOPMENT'
        log.fine('Version: ' + configVersion)
        return configVersion
    }

    String getUserDataDirectory() {
        return System.getProperty("user.home") + File.separator + ".issuefinder" + File.separator + 'config' + File.separator
    }

    @Override
    void checkDataDirectory() {
        log.info('Checking data dir')
        final File directory = new File(configObject.getProperty(DATA_DIR).toString())
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }
}
