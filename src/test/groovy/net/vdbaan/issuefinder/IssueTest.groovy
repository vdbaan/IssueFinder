/*
 *  Copyright (C) 2018  S. van der Baan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.model.FindingIdentifier
import net.vdbaan.issuefinder.model.Issue
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class IssueTest {

    @Test
    void testLoadFromConfig() {
        List<Issue> issues = Config.instance.getProperty(Config.ISSUE_LIST) as List?:new ArrayList<>()
        assertTrue (issues.size() == 0)
    }

    @Test
    void testFormat() {
//        ConfigObject configObject = new ConfigObject()
        List<Issue> issues = new ArrayList()

        Issue i = new Issue(title: 'test Title', severity: Finding.Severity.UNKNOWN, description: '', recommendations: '' )
        i.findings = new ArrayList<>()
        i.findings += new FindingIdentifier(scanner: 'scanner',plugin: 'plugin'+System.currentTimeMillis())
        issues += i
        String json =  new JsonBuilder(issues).toString()
//        String json =  JsonOutput.toJson(issues)

        List<Issue> issueMap = new JsonSlurper().parseText(json)
        Issue t = new Issue(issueMap.get(0))
        assertEquals(t.findings[0].plugin,i.findings[0].plugin)
    }

    @Test
    void testJoin() {
        List<Issue> issues = new ArrayList()
        issues += new Issue(title: 'test Title', severity: Finding.Severity.UNKNOWN, description: '', recommendations: '')
        issues += new Issue(title: 'test Title', severity: Finding.Severity.HIGH, description: '', recommendations: '')

        println issues.collect {String.format('%s:%s',it.title,it.severity)}.join(',')
        println issues.min{it.severity}.severity
    }
}
