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
import net.vdbaan.issuefinder.view.ExtensionFilter
import org.junit.Before

import org.junit.Test

import javax.swing.JTextField
import javax.swing.filechooser.FileFilter

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
        File testFile = new File('testdata/Nmap.xml')
        if(testFile.exists()) {
            IssuesLoader loader = new IssuesLoader()
            EventList<Finding> findingEventList = new BasicEventList<Finding>()
            MC mc = new MC() {
                void doneLoading() {
                    //
                }
            }
            loader.load([testFile], findingEventList, mc)
            loader.run()
            assert (findingEventList.size() > 0)
        }
    }
}

class MCExportTest {
    @Test
    void testXML() {
        File testFile = new File('testdata/Nmap.xml')

        if(testFile.exists()) {
            MC mc = new MC()
            IssuesLoader loader = new IssuesLoader()
            EventList<Finding> findingEventList = new BasicEventList<Finding>()
            loader.load([testFile], findingEventList, mc)
            loader.run()
            mc.@filteredFindings = findingEventList
            File tmpOut = File.createTempFile('tmp','.xml')
//            tmpOut.deleteOnExit()
            mc.exportAsXML(tmpOut)
            XmlSlurper xmlslurper = new XmlSlurper()
            xmlslurper.parse(tmpOut)

        }
    }

    @Test
    void testCSV() {
        File testFile = new File('testdata/Nmap.xml')

        if(testFile.exists()) {
            String xml = testFile.text
            MC mc = new MC()
            IssuesLoader loader = new IssuesLoader()
            EventList<Finding> findingEventList = new BasicEventList<Finding>()
            loader.load([testFile], findingEventList, mc)
            loader.run()
            mc.@filteredFindings = findingEventList
            File tmpOut = File.createTempFile('tmp','.csv')
            tmpOut.deleteOnExit()
            mc.exportAsCSV(tmpOut)
        }
    }

    @Test
    void testOnExport() {
        File testFile = new File('testdata/Nmap.xml')

        if(testFile.exists()) {
            MC mc = new MC()
            IssuesLoader loader = new IssuesLoader()
            EventList<Finding> findingEventList = new BasicEventList<Finding>()
            loader.load([testFile], findingEventList, mc)
            loader.run()
            mc.@filteredFindings = findingEventList
            File tmpOut = File.createTempFile('tmp','.xml')
//            tmpOut.deleteOnExit()
            mc.saveAs(tmpOut,new ExtensionFilter("XML Files", ".xml"))
            XmlSlurper xmlslurper = new XmlSlurper()
            xmlslurper.parse(tmpOut)
            File tmpOut2 = File.createTempFile('tmp','.csv')
            mc.saveAs(tmpOut,new ExtensionFilter("CSV Files", ".csv"))
        }
    }
}
