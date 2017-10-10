package net.vdbaan.issuefinder.db

import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

class DbHandlerTest {

    @Before
    void memDB() {
        Config.getInstance().setProperty(Config.DATA_SOURCE, [database: 'jdbc:h2:mem:issueDB;DB_CLOSE_DELAY=-1', user: 'sa', password: ''])
    }

    @Test
    void testEmptyDb() {
        DbHandler handler = new DbHandlerImpl()
        handler.getAllFinding(null)
        assertEquals('', 0, handler.getNumrows())
    }

    @Test
    void testSimpleDb() {

        DbHandler handler = new DbHandlerImpl()
        handler.saveFindings([makeFinding('1')])
        assertEquals('Should still be 0', 0, handler.getNumrows())
        List<Finding> result = handler.getAllFinding(null)
        assertEquals('Can\'t load the issue', result.size(), handler.getNumrows())
        result = handler.getAllFinding("SERVICE == 'www'")
        assertEquals('Doesn\'t exist', 0, result.size())
        handler.deleteAll()
        result = handler.getAllFinding(null)
        assertEquals('Should be empty', 0, result.size())
    }

    @Test
    void testUpdate() {

        String txt = 'New service'
        DbHandler handler = new DbHandlerImpl()
        handler.saveFindings([makeFinding('1')])
        assertEquals('Should still be 0', 0, handler.getNumrows())
        List<Finding> result = handler.getAllFinding(null)
        assertEquals('Can\'t load the issue', result.size(), handler.getNumrows())
        Finding f = result.get(0)
        f.service = txt
        handler.updateFinding(f)
        result = handler.getAllFinding(null)
        assertEquals('Can\'t load the issue', result.size(), handler.getNumrows())
        assertEquals(txt, result.get(0).service)
    }

    @Test
    void deleteDBAfterUse() {
        String dir = './build/tmp/'
        String name = 'issueDB'
        File d = new File(dir)
        if (!d.exists()) d.mkdirs()

        Config.getInstance().setProperty(Config.DATA_DIR, dir)
        Config.getInstance().setProperty(Config.DB_NAME, name)
        Config.getInstance().setProperty(Config.DATA_SOURCE, [database: 'jdbc:h2:./build/tmp/issueDB', user: 'sa', password: ''])
        DbHandler handler = new DbHandlerImpl()
        File db = new File(dir, name.concat('.mv.db'))

        handler.saveFindings([makeFinding('1')])
        assert db.exists()
        handler.deleteAll()
        handler.deleteDB()
        assert !db.exists()
//        handler.execute('SHUTDOWN')
    }


    private Finding makeFinding(String txt) {
        return new Finding(scanner: 'scanner' + txt, ip: 'ip' + txt, port: 'port' + txt, portStatus: 'portStatus' + txt,
                protocol: 'protocol' + txt, plugin: 'plugin' + txt, service: 'service' + txt, hostName: 'hostName' + txt,
                severity: Finding.Severity.HIGH)
    }
}