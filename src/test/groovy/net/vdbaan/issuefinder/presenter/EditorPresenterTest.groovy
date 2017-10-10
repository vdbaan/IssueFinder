package net.vdbaan.issuefinder.presenter

import javafx.event.ActionEvent
import javafx.event.EventHandler
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.db.DbListener
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.EditorView
import net.vdbaan.issuefinder.view.MainView
import net.vdbaan.issuefinder.view.impl.AbstractEditorView
import org.junit.Test

import static org.junit.Assert.assertEquals

class EditorPresenterTest {
    @Test
    void testSingleFinding() {
        EditorView mock = new EditorViewMock()
        mock.setEditData([new Finding(scanner: 'mockScanner', hostName: 'mockHostName', ip: 'mockIp', port: 'mockPort', service: 'mockService', plugin: 'mockPlugin', severity: Finding.Severity.UNKNOWN)])
        assertEquals('Wrong Scanner', 'mockScanner', mock.getScanner())

    }

    @Test
    void testMultipleFindings() {
        EditorView mock = new EditorViewMock()
        mock.setEditData([new Finding(scanner: 'mockScanner', hostName: 'mockHostName', ip: 'mockIp', port: 'mockPort', service: 'mockService', plugin: 'mockPlugin', severity: Finding.Severity.UNKNOWN),
                          new Finding(scanner: 'mockScanner', hostName: 'mockHostName', ip: 'mockIp', port: 'mockPort', service: 'mockService', plugin: 'mockPlugin', severity: Finding.Severity.UNKNOWN)])
        assertEquals('Wrong Scanner', null, mock.getScanner())
    }

    @Test
    void pressOk() {
        Finding f = new Finding(scanner: 'mockScanner', hostName: 'mockHostName', ip: 'mockIp', port: 'mockPort', service: 'mockService', plugin: 'mockPlugin', severity: Finding.Severity.UNKNOWN)
        EditorView mock = new EditorViewMock() {
            @Override
            DbHandler getDbHandler() {
                return new DbHandlerMock() {
                    @Override
                    void updateFinding(Finding finding) {
                        assertEquals('Wrong finding',f,finding)
                    }
                }
            }
        }
        mock.setEditData([f])
        assertEquals('Wrong Scanner', 'mockScanner', mock.getScanner())
        mock.getOkAction().handle(null)
    }

    @Test
    void pressCancel() {
        Finding f = new Finding(scanner: 'mockScanner', hostName: 'mockHostName', ip: 'mockIp', port: 'mockPort', service: 'mockService', plugin: 'mockPlugin', severity: Finding.Severity.UNKNOWN)
        EditorView mock = new EditorViewMock() {
            @Override
            DbHandler getDbHandler() {
                return new DbHandlerMock() {

                    @Override
                    void updateFinding(Finding finding) {
                        assertEquals('Wrong finding',f,finding)
                    }
                }
            }
        }
        mock.setEditData([f])
        assertEquals('Wrong Scanner', 'mockScanner', mock.getScanner())
        mock.getCancelAction().handle(null)
    }
}

class EditorViewMock extends AbstractEditorView implements EditorView{
    String scanner, hostName, ip, port, service, plugin
    Finding.Severity risk

    private EventHandler<ActionEvent> okAction
    private EventHandler<ActionEvent> cancelAction

    EditorPresenter presenter

    EditorViewMock() {
        presenter = new EditorPresenter(this)
    }

    @Override
    String getEditScanner() {
        scanner
    }

    @Override
    void setEditScanner(String editScanner) {
        scanner = editScanner
    }

    @Override
    String getEditHostname() {
        return hostName
    }

    @Override
    void setEditHostname(String editHostname) {
        hostName = editHostname
    }

    @Override
    String getEditIp() {
        return ip
    }

    @Override
    void setEditIp(String editIp) {
        ip = editIp
    }

    @Override
    String getEditPort() {
        return port
    }

    @Override
    void setEditPort(String editPort) {
        port = editPort
    }

    @Override
    String getEditService() {
        return service
    }

    @Override
    void setEditService(String editService) {
        service = editService
    }

    @Override
    String getEditPlugin() {
        return plugin
    }

    @Override
    void setEditPlugin(String editPlugin) {
        plugin = editPlugin
    }

    @Override
    Finding.Severity getEditRisk() {
        return risk
    }

    @Override
    void setEditRisk(Finding.Severity editRisk) {
        risk = editRisk
    }

    @Override
    void setEditRiskOptions(List<Finding.Severity> options) {

    }

    @Override
    EventHandler<ActionEvent> getOkAction() {
        return okAction
    }

    @Override
    void setOkAction(EventHandler<ActionEvent> okAction) {
        this.okAction = okAction
    }

    @Override
    EventHandler<ActionEvent> getCancelAction() {
        return cancelAction
    }

    @Override
    void setCancelAction(EventHandler<ActionEvent> cancelAction) {
        this.cancelAction = cancelAction
    }

    @Override
    void setEditData(List<Finding> data) {
        presenter.setEditData(data)
    }

    @Override
    DbHandler getDbHandler() {
        return null
    }

    @Override
    void close() {

    }

    @Override
    void setMasterView(MainView view) {

    }

    @Override
    void addDbListener(DbListener listener) {

    }

    @Override
    void dbUpdated() {

    }
}

class DbHandlerMock implements DbHandler {

    @Override
    List<Finding> getAllFinding(String where) {
        return null
    }

    @Override
    void saveFindings(List<Finding> list) {

    }

    @Override
    void deleteAll() {

    }

    @Override
    void updateFinding(Finding finding) {

    }

    @Override
    int getNumrows() {
        return 0
    }

    @Override
    void deleteDB() {

    }

    @Override
    void attachShutdownHook() {

    }
}