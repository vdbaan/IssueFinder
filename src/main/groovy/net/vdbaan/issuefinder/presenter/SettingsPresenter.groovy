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
package net.vdbaan.issuefinder.presenter

import groovy.transform.CompileStatic
import javafx.event.ActionEvent
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.SettingsView

@CompileStatic
class SettingsPresenter {
    private SettingsView settingsView

    SettingsPresenter(SettingsView view) {
        this.settingsView = view

        settingsView.setMaxRows(Config.getInstance().getProperty(Config.MAX_ROWS) as String)
        settingsView.setBatchSize(Config.getInstance().getProperty(Config.BATCH_SIZE) as String)
        Map<Finding.Severity, Boolean> preload_filter = (Map) Config.getInstance().getProperty(Config.PRELOAD_FILTER)
        settingsView.setIsCritical(preload_filter.get(Finding.Severity.CRITICAL))
        settingsView.setIsHigh((Boolean) preload_filter.get(Finding.Severity.HIGH))
        settingsView.setIsMedium((Boolean) preload_filter.get(Finding.Severity.MEDIUM))
        settingsView.setIsLow((Boolean) preload_filter.get(Finding.Severity.LOW))
        settingsView.setIsInfo((Boolean) preload_filter.get(Finding.Severity.INFO))
        settingsView.setColouredRow(Config.getInstance().getProperty(Config.COLOURED_ROWS) as boolean)
        settingsView.removeListItems()
        List list = (List) Config.getInstance().getProperty(Config.FILTERS)
        list.each {
            settingsView.addToListItems(it.toString())
        }

        settingsView.setAddFilterAction(this.&addFilter)
        settingsView.setEditFilterAction(this.&editFilter)
        settingsView.setDeleteFilterAction(this.&deleteFilter)
        settingsView.setSaveAllAction(this.&save)
        settingsView.setCancelAllAction(this.&cancel)
    }

    private void addFilter(ActionEvent event) {
        def text = settingsView.getFilterText()
        if (!text?.equals(""))
            settingsView.addToListItems(text)
    }

    private void editFilter(ActionEvent event) {
        def filter = settingsView.getSelectedListItem()
        settingsView.setFilterText(filter)
    }

    private void deleteFilter(ActionEvent event) {
        def filter = settingsView.getSelectedListItem()
        settingsView.removeItem(filter)
    }

    private void save(ActionEvent event) {
        Map<Finding.Severity, Boolean> loadfilter = new HashMap()
        loadfilter.put(Finding.Severity.CRITICAL, settingsView.getIsCritical())
        loadfilter.put(Finding.Severity.HIGH, settingsView.getIsHigh())
        loadfilter.put(Finding.Severity.MEDIUM, settingsView.getIsMedium())
        loadfilter.put(Finding.Severity.LOW, settingsView.getIsLow())
        loadfilter.put(Finding.Severity.INFO, settingsView.getIsInfo())
        Config.getInstance().setProperty(Config.PRELOAD_FILTER, loadfilter)

        Config.getInstance().setProperty(Config.COLOURED_ROWS, settingsView.isColouredRow())
        Config.getInstance().setProperty(Config.MAX_ROWS, settingsView.getMaxRows() as Integer)
        Config.getInstance().setProperty(Config.BATCH_SIZE, settingsView.getBatchSize() as Integer)

//        ((List) ConfigHandler.getIFProperty(Config.FILTERS)).clear()
        Config.getInstance().setProperty(Config.FILTERS, settingsView.getAllItems())

        settingsView.close()
    }

    private void cancel(ActionEvent event) {
        settingsView.close()
    }
}
