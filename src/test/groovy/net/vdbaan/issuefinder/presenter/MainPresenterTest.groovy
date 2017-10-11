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
package net.vdbaan.issuefinder.presenter

import javafx.beans.InvalidationListener
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.SelectionMode
import javafx.scene.input.ClipboardContent
import javafx.stage.Window
import net.vdbaan.issuefinder.MainApp
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.Container
import net.vdbaan.issuefinder.util.RetentionFileChooser
import net.vdbaan.issuefinder.view.MainView
import net.vdbaan.issuefinder.view.impl.AbstractMainView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.ExpectedSystemExit

import static org.junit.Assert.assertEquals

class MainPresenterTest {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none()

    @Test
    void setup() {
        exit.expectSystemExit()
        MainView mock = new MainViewMock()
        mock.getNewAction().handle(null)
        mock.getCloseAction().handle(null)
    }

    @Test
    void processOneFile() {
        File testFile = new File('testdata/Nessus.nessus')
        MainView mock = new MainViewMock()
        mock.setRetentionResult([testFile])
        mock.setMainApp(new MainApp() {
            @Override
            void showProgressDialog(List<File> files) throws IOException {

            }

            @Override
            void showAbout() throws IOException {

            }

            @Override
            void showEditor(List<Finding> data) throws IOException {

            }

            @Override
            void showSummary(Map<String, Container> summary) throws IOException {

            }

            @Override
            void showSettings() throws IOException {

            }
        })
        mock.getOpenAction().handle(null)
    }

    @Test
    void testFilters() {
        Finding f = new Finding(ip: 'newip', port: 'newport', plugin: 'newplugin', service: 'newservice')
        MainView mock = new MainViewMock()
        mock.setMasterData([f])
        // as they all do the same, call all for coverage :D
        mock.getFilterServiceAction().handle(null)
        mock.getFilterPluginAction().handle(null)
        mock.getFilterPortAction().handle(null)
        mock.getFilterIpAction().handle(null)
        assertEquals('IP == \'newip\'', mock.getFilterTextValue())
    }

    @Test
    void testClipboard() {
        Finding f1 = new Finding(ip: '127.0.0.1', port: '80', plugin: 'newplugin', service: 'newservice')
        Finding f2 = new Finding(ip: '127.0.0.2', port: '8080', plugin: 'newplugin', service: 'newservice')
        MainView mock = new MainViewMock()
        mock.setMasterData([f1,f2])
        mock.getCopyIpAction().handle(null)
        assertEquals('127.0.0.1\n127.0.0.2',mock.getClipboardContent().getString())
        mock.getCopyIpPortAction().handle(null)
        assertEquals('127.0.0.1:80\n127.0.0.2:8080',mock.getClipboardContent().getString())
    }

    @Before
    void memDB() {
        Config.getInstance().setProperty(Config.DATA_SOURCE, [database: 'jdbc:h2:mem:issueDB;DB_CLOSE_DELAY=-1', user: 'sa', password: ''])
    }

    @Test
    void testFilterText() {
        MainView mock = new MainViewMock()
        mock.setFilterText('risk == CRITICAL')
        mock.getFilterTableAction().handle(null)
        assert mock.getFilterTextItems().contains("RISK == 'CRITICAL'")
        // check if filtertext = present in filters
        mock.getClearAction().handle(null)
        // check if filtertext == clear again
        assert mock.getFilterText() == null
    }

}

class MainViewMock extends AbstractMainView implements MainView {

    MainPresenter presenter

    MainViewMock() {
        presenter = new MainPresenter(this)
    }

    @Override
    void setMainApp(MainApp mainApp) {
        presenter.setMainApp(mainApp)
    }

    void setMasterData(List<Finding> data) {
        presenter.getMasterData().addAll(data)
    }

    @Override
    void aboutAction(ActionEvent event) {

    }

    @Override
    void buildSummary(ActionEvent event) {

    }

    @Override
    void clearFilter(ActionEvent event) {

    }

    @Override
    void closeAction(ActionEvent event) {

    }

    @Override
    void copyUniqueIps(ActionEvent event) {

    }

    @Override
    void copyUniquePortsAndIps(ActionEvent event) {

    }

    @Override
    void exportAction(ActionEvent event) {

    }

    @Override
    void filterOnIp(ActionEvent event) {

    }

    @Override
    void filterOnPlugin(ActionEvent event) {

    }

    @Override
    void filterOnPort(ActionEvent event) {

    }

    @Override
    void filterOnService(ActionEvent event) {

    }

    @Override
    void filterOnScanner(ActionEvent event) {

    }

    @Override
    void filterOnPortStatus(ActionEvent event) {

    }

    @Override
    void filterOnProtocol(ActionEvent event) {

    }

    @Override
    void filterOnRisk(ActionEvent event) {

    }

    @Override
    void filterTable(ActionEvent event) {

    }

    @Override
    void modifyEntry(ActionEvent event) {

    }

    @Override
    void copySelectedIps(ActionEvent event) {

    }

    @Override
    void copySelectedIpsPorts(ActionEvent event) {

    }

    @Override
    void newAction(ActionEvent event) {

    }

    @Override
    void openAction(ActionEvent event) {

    }

    @Override
    void openSettings(ActionEvent event) {

    }

    @Override
    void setRowInfoLabel(String text) {

    }

    @Override
    void setIpInfoLabel(String text) {

    }

    @Override
    void setStatusLabel(String text) {

    }

    List<String> filterTextItems = new ArrayList<>()
    @Override
    void setFilterTextItems(List<String> items) {
        this.filterTextItems = items
    }

    @Override
    List<String> getFilterTextItems() {
        return filterTextItems
    }

    @Override
    void addFiterTextListener(ChangeListener listener) {

    }

    String filterText

    @Override
    void setFilterText(String text) {
        this.filterText = text
    }

    @Override
    void setMainTableItems(ObservableList items) {

    }

    @Override
    void setSelectItemPropertyListener(ChangeListener listener) {

    }

    @Override
    void setTableSelectionMode(SelectionMode mode) {

    }
    ObservableList<String> filterTextStyleClass = FXCollections.observableArrayList()
    @Override
    ObservableList<String> getFilterTextStyleClass() {
        return filterTextStyleClass
    }

    @Override
    void addFilterTextMouseClicked(EventHandler handler) {

    }

    @Override
    ObjectProperty<EventHandler> getTableKeyEvent() {
        return null
    }

    @Override
    void textArealoadContent(String content) {

    }

    @Override
    Finding getSelectedFinding() {
        return presenter.getMasterData().get(0)
    }

    @Override
    Window getWindow() {
        return null
    }

    @Override
    List<Finding> getSelectedFindings() {
        return null
    }

    @Override
    String getFilterTextValue() {
        return filterText
    }

    void setRetentionResult(List<File> result) {
        this.retentionResult = result
    }
    List<File> retentionResult

    @Override
    RetentionFileChooser getRetentionFileChooser() {
        return new RetentionFileChooser() {
            @Override
            List<File> showOpenMultipleDialog(Window ownerWindow) {
                return retentionResult
            }
        }
    }

    @Override
    ReadOnlyObjectProperty<Comparator> getTableComparatorProperty() {
        return new ReadOnlyObjectProperty<Comparator>() {
            Comparator comparator = new Comparator() {

                @Override
                int compare(Object o1, Object o2) {
                    return 0
                }
            }

            @Override
            Object getBean() {
                return comparator
            }

            @Override
            String getName() {
                return 'Comparator'
            }

            @Override
            Comparator get() {
                return comparator
            }

            @Override
            void addListener(ChangeListener<? super Comparator> listener) {

            }

            @Override
            void removeListener(ChangeListener<? super Comparator> listener) {

            }

            @Override
            void addListener(InvalidationListener listener) {

            }

            @Override
            void removeListener(InvalidationListener listener) {

            }
        }
    }

    ClipboardContent clipboardContent
    @Override
    void setClipboardContent(ClipboardContent content) {
        this.clipboardContent = content
    }

    ClipboardContent getClipboardContent() {
        return clipboardContent
    }
}

