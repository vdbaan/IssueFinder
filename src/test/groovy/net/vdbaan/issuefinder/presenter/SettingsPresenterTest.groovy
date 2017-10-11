package net.vdbaan.issuefinder.presenter

import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.view.SettingsView
import net.vdbaan.issuefinder.view.impl.AbstractSettingsView
import org.junit.Test

import static org.junit.Assert.*

class SettingsPresenterTest {

    @Test
    void setup() {
        SettingsView mock = new SettingsViewMock()
        String newValue = "2000"
        assertEquals(Config.getInstance().getProperty(Config.MAX_ROWS).toString(), mock.getMaxRows())
        mock.setMaxRows(newValue)
        mock.getCancelAllAction().handle(null)
        mock.getSaveAllAction().handle(null)
        assertEquals(newValue,Config.getInstance().getProperty(Config.MAX_ROWS).toString())
    }

    @Test
    void testAddFilter() {
        SettingsView mock = new SettingsViewMock()
        String txt = 'this is a new filter'
        assertFalse(mock.getAllItems().contains(txt))
        mock.setFilterText(txt)
        mock.getAddFilterAction().handle(null)
        assertTrue(mock.getAllItems().contains(txt))
    }

    @Test
    void testEditFilter() {
        SettingsView mock = new SettingsViewMock()
        String txt = 'IP == "127.0.0.1"'
        assertTrue(mock.getAllItems().contains(txt))
        mock.getEditFilterAction().handle(null)
        assertEquals(txt, mock.getFilterText())
    }

    @Test
    void testDeleteFilter() {
        SettingsView mock = new SettingsViewMock()
        String txt = 'IP == "127.0.0.1"'
        assertTrue(mock.getAllItems().contains(txt))
        mock.getDeleteFilterAction().handle(null)
        assertFalse(mock.getAllItems().contains(txt))
    }
}

class SettingsViewMock extends AbstractSettingsView implements SettingsView {

    SettingsPresenter presenter
    List filters = new ArrayList()

    SettingsViewMock() {
        presenter = new SettingsPresenter(this)
    }

    @Override
    void removeListItems() {

    }

    @Override
    List getAllItems() {
        return filters
    }

    @Override
    void addToListItems(List items) {

    }

    @Override
    void addToListItems(String items) {
        filters.add(items)
    }


    @Override
    String getSelectedListItem() {
        return filters.get(0)
    }

    @Override
    void removeItem(String item) {
    filters.remove(item)
    }

    boolean critical, high, medium, low, info

    @Override
    boolean getIsCritical() {
        return critical
    }

    @Override
    void setIsCritical(boolean isCritical) {
        this.critical = isCritical
    }

    @Override
    boolean getIsHigh() {
        return high
    }

    @Override
    void setIsHigh(boolean isHigh) {
        this.high = isHigh
    }

    @Override
    boolean getIsMedium() {
        return medium
    }

    @Override
    void setIsMedium(boolean isMedium) {
        this.medium = isMedium
    }

    @Override
    boolean getIsLow() {
        return low
    }

    @Override
    void setIsLow(boolean isLow) {
        this.low = isLow
    }

    @Override
    boolean getIsInfo() {
        return info
    }

    @Override
    void setIsInfo(boolean isInfo) {
        this.info = isInfo
    }

    String filterText

    @Override
    String getFilterText() {
        return filterText
    }

    @Override
    void setFilterText(String filterText) {
        this.filterText = filterText
    }

    String batchSize

    @Override
    String getBatchSize() {
        return batchSize
    }

    @Override
    void setBatchSize(String batchSize) {
        this.batchSize = batchSize
    }

    String maxRows

    @Override
    String getMaxRows() {
        return maxRows
    }

    @Override
    void setMaxRows(String maxRows) {
        this.maxRows = maxRows
    }

    String copyString

    @Override
    String getCopyString() {
        return copyString
    }

    @Override
    void setCopyString(String copyString) {
        this.copyString = copyString
    }

    @Override
    boolean isColouredRow() {
        return false
    }

    @Override
    void setColouredRow(boolean coloured) {

    }
}