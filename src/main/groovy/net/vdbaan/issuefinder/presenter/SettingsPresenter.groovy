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

    SettingsPresenter(final SettingsView view) {
        this.settingsView = view

        settingsView.maxRows = Config.instance.getProperty(Config.MAX_ROWS) as String
        settingsView.batchSize = Config.instance.getProperty(Config.BATCH_SIZE) as String
        settingsView.copyString = Config.instance.getProperty(Config.IP_PORT_FORMAT_STRING) as String
        final Map<Finding.Severity, Boolean> preload_filter = (Map) Config.instance.getProperty(Config.PRELOAD_FILTER)
        settingsView.isCritical = preload_filter[Finding.Severity.CRITICAL]
        settingsView.isHigh = (Boolean) preload_filter[Finding.Severity.HIGH]
        settingsView.isMedium = (Boolean) preload_filter[Finding.Severity.MEDIUM]
        settingsView.isLow = (Boolean) preload_filter[Finding.Severity.LOW]
        settingsView.isInfo = (Boolean) preload_filter[Finding.Severity.INFO]
        settingsView.colouredRow = Config.instance.getProperty(Config.COLOURED_ROWS) as boolean
        settingsView.removeListItems()
        final List list = (List) Config.instance.getProperty(Config.FILTERS)
        list.each {
            settingsView.addToListItems(it.toString())
        }

        settingsView.addFilterAction = this.&addFilter
        settingsView.editFilterAction = this.&editFilter
        settingsView.deleteFilterAction = this.&deleteFilter
        settingsView.saveAllAction = this.&save
        settingsView.cancelAllAction = this.&cancel
    }

    private void addFilter(final ActionEvent event) {
        final def text = settingsView.filterText
        if (!text?.equals(""))
            settingsView.addToListItems(text)
    }

    private void editFilter(final ActionEvent event) {
        final def filter = settingsView.selectedListItem
        settingsView.filterText = filter
    }

    private void deleteFilter(final ActionEvent event) {
        final def filter = settingsView.selectedListItem
        settingsView.removeItem(filter)
    }

    private void save(final ActionEvent event) {
        final Map<Finding.Severity, Boolean> loadfilter = new HashMap()
        loadfilter[Finding.Severity.CRITICAL] = settingsView.isCritical
        loadfilter[Finding.Severity.HIGH] = settingsView.isHigh
        loadfilter[Finding.Severity.MEDIUM] = settingsView.isMedium
        loadfilter[Finding.Severity.LOW] = settingsView.isLow
        loadfilter[Finding.Severity.INFO] = settingsView.isInfo
        Config.instance.setProperty(Config.PRELOAD_FILTER, loadfilter)

        Config.instance.setProperty(Config.COLOURED_ROWS, settingsView.colouredRow)
        Config.instance.setProperty(Config.MAX_ROWS, settingsView.maxRows as Integer)
        Config.instance.setProperty(Config.BATCH_SIZE, settingsView.batchSize as Integer)
        Config.instance.setProperty(Config.IP_PORT_FORMAT_STRING, settingsView.copyString)
        Config.instance.setProperty(Config.FILTERS, settingsView.allItems)

        settingsView.close()
    }

    private void cancel(final ActionEvent event) {
        settingsView.close()
    }
}
