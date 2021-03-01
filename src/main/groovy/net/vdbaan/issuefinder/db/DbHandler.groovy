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
package net.vdbaan.issuefinder.db

import groovy.io.FileType
import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import groovy.util.logging.Log
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import org.h2.jdbc.JdbcSQLException
import org.h2.jdbcx.JdbcDataSource

import java.util.logging.Level

interface DbHandler {

    List<Finding> getAllFinding(String where)

    List<Finding> getAllFinding(String where, int maxRows)

    void saveFindings(List<Finding> list)

    void deleteAll()

    void updateFinding(Finding finding)

    int getNumrows()

    void deleteDB()

    void attachShutdownHook()

    void resetJdbc()

    void saveDb(String fileName)

    void loadDb(String fileName)

    List<String> getAllPlugins(String where)
}

@Log
class DbHandlerImpl implements DbHandler {

    static final class MyDataSource {

        public static final MyDataSource INSTANCE = new MyDataSource()
        public static boolean create = true
        private JdbcDataSource jdbcDataSource

        JdbcDataSource getDataSource() {
            if (jdbcDataSource == null) {
                final Object dataSource = Config.instance.getProperty(Config.DATA_SOURCE).clone()
                final String dbName = Config.instance.getProperty(Config.DB_NAME)
                if (!Config.instance.hasPropertyFor('LEAVE_DB')) {
                    final String name
                    name = dbName + '-' + System.currentTimeMillis()
                    Config.instance.setProperty('VOLATILE-DB', name)
                    dataSource.database = dataSource.database.replace(dbName, name)
                }
                log.info 'Using db: ' + dataSource
                jdbcDataSource = new JdbcDataSource(url: dataSource.database, user: dataSource.user, password: dataSource.password)
            }
            return jdbcDataSource
        }

        void resetJdbc() {
            jdbcDataSource = null
            create = true
        }
    }
    Sql sql = null

    private Sql getSql() {
        if (sql == null) {
            sql = new Sql(MyDataSource.INSTANCE.dataSource)
            if (MyDataSource.create) {
                try {
                    createTable(sql)
                } catch (final JdbcSQLException e) {
                    log.warning(' Database wasn\'t deleted.....something went wrong')
                    log.log(Level.FINE, 'Got this exception', e)
                }
                MyDataSource.create = false
                attachShutdownHook()
            }
        }
        return sql
    }

    private def void createTable(final Sql sql) {
        sql.execute Finding.CREATE
    }

    private String buildQry(final String where, final String base = Finding.SELECT) {
        final StringBuilder sb = new StringBuilder(base)
        if (where != null && !''.equalsIgnoreCase(where)) {
            sb.append(' WHERE ')
            sb.append(where.replace(' == ', ' = ').replace(' && ', ' AND ').replace(' || ', ' OR '))
        }
        return sb.toString()
    }

    private int numrows

    int getNumrows() {
        return numrows
    }

    List<Finding> getAllFinding(final String where) {
        return getAllFinding(where, Config.instance.getProperty(Config.MAX_ROWS))
    }

    List<Finding> getAllFinding(final String where, int maxRows) {
        log.info(String.format('Filtering on <%s>', where))
        final def response = getSql().firstRow(buildQry(where, Finding.COUNT))
        numrows = response.'COUNT(*)'
        final List<Finding> result = new ArrayList<>()
        if (maxRows == -1) {
            maxRows = numrows
        }
        getSql().eachRow(buildQry(where), 1, maxRows) { final row ->
            result << buildFinding((GroovyResultSet) row)
        }
        return result
    }

    Finding buildFinding(final GroovyResultSet p) {
        return new Finding(id: p.id, scanner: p.scanner, ip: p.ip, port: p.port, portStatus: p.status, location: p.location,
                protocol: p.protocol, hostName: p.hostName, service: p.service, plugin: p.plugin, baseCVSS: p.cvss,
                severity: getSeverity(p.risk), summary: p.summary, description: p.description, reference: p.reference, pluginOutput: p.pluginOutput,
                solution: p.solution, cvssVector: p.cvssVector)
    }

    Finding.Severity getSeverity(final String risk) {
        return Finding.Severity.valueOf(risk)
    }

    void saveFindings(final List<Finding> list) {
        log.info String.format('Saving %s findings', list.size())
        if (list.size() > 0) {
            getSql().withBatch(100, Finding.INSERT) { final ps ->
                list.each { final f ->
                    ps.addBatch(f)
                }
            }
        }
    }

    void deleteAll() {
        getSql().execute(Finding.DELETE_ALL)
    }

    void updateFinding(final Finding finding) {
        getSql().executeUpdate(Finding.UPDATE, finding)
    }

    void deleteDB() {
        log.fine 'Deleting DB'
        getSql().execute('TRUNCATE TABLE finding')
        try {
            getSql().execute('SHUTDOWN')
        } catch (final JdbcSQLException e) {
            log.log(Level.FINE, 'Got this exception', e)
        } finally {
            final File dir = new File(Config.instance.getProperty(Config.DATA_DIR).toString())
            dir.eachFile(FileType.FILES) { final File f ->
                final String name = Config.instance.getProperty('VOLATILE-DB')
                if (f?.name?.startsWith(name)) {
                    log.fine 'Deleting ' + f.absolutePath
                    f.delete()
                }
            }
        }
    }

    void attachShutdownHook() {
        Runtime.runtime.addShutdownHook(new Thread() {

            @Override
            void run() {
                try {
                    deleteAll()
                    deleteDB()
                } catch (final JdbcSQLException e) {
                    log.warning 'Table already deleted'
                    log.log(Level.FINE, 'Got this exception', e)
                }
            }

        })
    }

    void resetJdbc() {
        MyDataSource.INSTANCE.resetJdbc()
    }

    void saveDb(final String fileName) {
        getSql().execute('SCRIPT NOPASSWORDS NOSETTINGS DROP TO \'$fileName\'')
    }

    void loadDb(final String fileName) {
        getSql().execute('RUNSCRIPT FROM \'$fileName\'')
        getSql().execute(Finding.UPDATE_TABLE)
    }

    List<String> getAllPlugins(final String where) {
        log.info(String.format('Filtering on <%s>', where))

        final List<String> result = new ArrayList()
        getSql().eachRow(buildQry(where, 'SELECT DISTINCT plugin,risk FROM finding')) { final row ->
            result << row.plugin
        }
        return result
    }

}
