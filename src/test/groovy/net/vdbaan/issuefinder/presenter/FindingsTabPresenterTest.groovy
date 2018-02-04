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

package net.vdbaan.issuefinder.presenter

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.input.ClipboardContent
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.presenter.mock.FindingTabMock
import net.vdbaan.issuefinder.view.FindingTabView
import org.junit.Test

import static org.junit.Assert.assertEquals

class FindingsTabPresenterTest {

    @Test
    void testFilterText() {
        Finding f = new Finding(ip: 'newip', port: 'newport', plugin: 'newplugin', service: 'newservice', protocol: 'newProtocol', portStatus: 'newPortStatus',
                severity: Finding.Severity.UNKNOWN, scanner: 'newScanner')
        FindingTabView mock = new FindingTabMock() {

            @Override
            Finding getSelectedFinding() {
                return f
            }

            String filterTextValue

            @Override
            void setMainFilterText(String text) {
                filterTextValue = text
            }

        }
        mock.filterOnIP(null)
        assertEquals('IP == \'newip\'', mock.filterTextValue)
        mock.filterOnScanner(null)
        assertEquals('SCANNER == \'newScanner\'', mock.filterTextValue)
        mock.filterOnPortStatus(null)
        assertEquals('PORTSTATUS == \'newPortStatus\'', mock.filterTextValue)
        mock.filterOnPort(null)
        assertEquals('PORT == \'newport\'', mock.filterTextValue)
        mock.filterOnProtocol(null)
        assertEquals('PROTOCOL == \'newProtocol\'', mock.filterTextValue)
        mock.filterOnPlugin(null)
        assertEquals('PLUGIN == \'newplugin\'', mock.filterTextValue)
        mock.filterOnRisk(null)
        assertEquals('RISK == \'UNKNOWN\'', mock.filterTextValue)
        mock.filterOnService(null)
        assertEquals('SERVICE == \'newservice\'', mock.filterTextValue)
    }

    @Test
    void testClipboad() {
        Finding f1 = new Finding(ip: '127.0.0.1', port: '80', portStatus: 'open', plugin: 'newplugin', service: 'www')
        Finding f2 = new Finding(ip: '127.0.0.2', port: '8080', portStatus: 'open', plugin: 'newplugin', service: 'www')

        FindingTabView mock = new FindingTabMock() {


            @Override
            ObservableList<Finding> getSelectedFindingsList() {
                return FXCollections.observableList([f1, f2])
            }

            ClipboardContent result

            @Override
            void setClipboardContent(ClipboardContent content) {
                result = content
            }
        }

        mock.copySelectedIps(null)
        assertEquals('127.0.0.1\n127.0.0.2', mock.result.getString())
        mock.copySelectedPortsAndIps(null)
        assertEquals('127.0.0.1:80\n127.0.0.2:8080', mock.result.getString())
    }
}
