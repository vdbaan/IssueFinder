package net.vdbaan.issuefinder.db

import groovy.io.FileType
import groovy.sql.Sql
import net.vdbaan.issuefinder.model.Finding
import org.h2.jdbcx.JdbcDataSource
import org.h2.tools.Restore
import org.junit.Before
import org.junit.Test

import java.sql.Connection
import java.sql.DriverManager

class H2Test {

    @Test
    void create() {
        Class.forName("org.h2.Driver")
        Connection conn = DriverManager.getConnection("jdbc:h2:./out/h2test", "sa", "")
        // add application code here
        conn.close()
    }

    @Test
    void groovyCreate() {
        JdbcDataSource ds = new JdbcDataSource(user: 'sa', password: 'sa', url: 'jdbc:h2:./out/h2test')
//        Sql sql = Sql.newInstance("jdbc:h2:./out/h2test", "sa", "sa", "org.h2.Driver")
        Sql sql = new Sql(ds)
        sql.execute Finding.CREATE
        sql.execute('BACKUP TO \'./out/backup.zip\'')
        sql.execute Finding.DELETE_ALL
        sql.execute('DROP TABLE finding IF EXISTS')
    }

    @Before
    void clean() {
        File outdir = new File('./out')
        if(outdir.exists()) {
            outdir.eachFile(FileType.FILES) { f ->
                if (f.getAbsolutePath().endsWith('.db')) {
                    println 'Deleting ' + f.getAbsolutePath()
                    f.delete()
                }
            }
        }
    }

    @Test
    void groovyRestore() {

        Sql sql = Sql.newInstance("jdbc:h2:./out/h2test;DB_CLOSE_ON_EXIT=FALSE", "sa", "sa", "org.h2.Driver")
        sql.execute Finding.CREATE
        sql.withBatch(100, Finding.INSERT) { ps ->
            ps.addBatch(makeFinding('1'))
        }
//        sql.execute('COMMIT')

        sql.execute('BACKUP TO \'./out/backup.zip\'')
//        File b = new File('./out/backup.zip')
//        b.renameTo(new File('./out/restore.zip'))
//        sql.eachRow ('show columns from finding') {
//            println it
//        }
        sql.execute Finding.DELETE_ALL
        sql.execute('DROP TABLE finding IF EXISTS')
        sql.executeUpdate('SHUTDOWN')

//        println 'restoring'
        Restore.execute('./out/backup.zip', './out/', 'h2test2')
        sql = Sql.newInstance("jdbc:h2:./out/h2test2", "sa", "sa", "org.h2.Driver")
        println '-------------------------------------------------------------------------------------------'
        sql.eachRow('show columns from finding') {
            println it
        }
        println '-------------------------------------------------------------------------------------------'
        sql.eachRow(Finding.SELECT) { row ->
            println row
        }
    }

    private Finding makeFinding(String txt) {
        return new Finding(scanner: 'scanner' + txt, ip: 'ip' + txt, port: 'port' + txt, portStatus: 'portStatus' + txt,
                protocol: 'protocol' + txt, plugin: 'plugin' + txt, service: 'service' + txt, hostName: 'hostName' + txt,
                severity: Finding.Severity.HIGH)
    }
}
