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

package net.vdbaan.issuefinder.controller

import ca.odell.glazedlists.BasicEventList
import ca.odell.glazedlists.EventList
import net.vdbaan.issuefinder.model.Finding
import org.junit.Before

import org.junit.Test

import javax.swing.JTextField

class MCTestCustomMatcher {
    List<Finding> findings

    @Before
    void setupFindings() {
        findings = new ArrayList<>()
        findings.add(new Finding("1","","80","","",Finding.Severity.CRITICAL,""))
        findings.add(new Finding("2","","8080","","",Finding.Severity.CRITICAL,""))
        findings.add(new Finding("3","","443","","",Finding.Severity.CRITICAL,""))
        findings.add(new Finding("1","","1234","","",Finding.Severity.CRITICAL,""))
    }

    @Test
    void testCustomMatcher1() {
        JTextField f = new JTextField("80")
        IssueSelector m = new IssueSelector(f,"port")
        m.caretUpdate(null)
        int count = 0
        findings.each { finding ->
            if (m.getMatcher().matches(finding)){
                count += 1
            }
        }
        assert(count == 2)
        count = 0
    }

    @Test
    void testCustomMatcher2() {
        JTextField f = new JTextField("80,443")
        IssueSelector m = new IssueSelector(f,"port")
        m.caretUpdate(null)
        int count = 0
        findings.each { finding ->
            if (m.getMatcher().matches(finding)) {
                count += 1
            }
        }
        assert(count == 3)
        count = 0
    }
}

class MCTestLoader {
    @Test
    void testLoadFile() {
        IssuesLoader loader = new IssuesLoader();
        EventList<Finding> findingEventList = new BasicEventList<Finding>()
        MC mc = new MC() {
            void doneLoading() {
                //
            }
        }
        List<String> test = ['testdata/Nmap.xml']
        loader.load(test,findingEventList,mc)
        loader.run()
        assert(findingEventList.size() > 0)
    }
}