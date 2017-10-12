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
        Config.getInstance().setProperty(Config.DATA_SOURCE, [database: "jdbc:h2:${dir}${name};DB_CLOSE_ON_EXIT=FALSE", user: 'sa', password: ''])
        DbHandler handler = new DbHandlerImpl()
        handler.saveFindings([makeFinding('1')])
        String n = Config.getInstance().getProperty('VOLATILE-DB')
        File db = new File(dir, n.concat('.mv.db'))
        assert db.exists()
        handler.deleteAll()
        handler.deleteDB()
        assert !db.exists()
    }

    private Finding makeFinding(String txt) {
        return new Finding(scanner: 'scanner' + txt, ip: 'ip' + txt, port: 'port' + txt, portStatus: 'portStatus' + txt,
                protocol: 'protocol' + txt, plugin: 'plugin' + txt, service: 'service' + txt, hostName: 'hostName' + txt,
                severity: Finding.Severity.HIGH)
    }
}