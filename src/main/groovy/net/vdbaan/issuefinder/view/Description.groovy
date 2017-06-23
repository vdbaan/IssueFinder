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


package net.vdbaan.issuefinder.view

import net.miginfocom.swing.MigLayout

import javax.swing.*
import javax.swing.border.TitledBorder
import java.awt.*
import java.util.jar.Manifest


actions {
    action(id: 'newAction',
            name: 'New...',
            mnemonic: 'N',
            accelerator: 'ctrl N',
            shortDescription: 'Start anew'
    )
    action(id: 'openAction',
            name: 'Open File..',
            mnemonic: 'O',
            accelerator: 'ctrl O',
            shortDescription: 'Open a File'
    )
    action(id: 'exportAction',
            name: 'Export as..',
            mnemonic: 'E',
            accelerator: 'ctrl shift S',
            shortDescription: 'Export to a file'
    )
    action(id: 'exitAction',
            name: 'Exit',
            mnemonic: 'x',
            accelerator: 'alt F4',
            shortDescription: 'Quit this program',
            closure: { System.exit(0) }
    )
    action(id: 'aboutAction',
            name: 'About...',
            accelerator: 'F1',
            shortDescription: 'About IssueFinder',
            closure: {
                aboutDialog.pack()
                aboutDialog.setLocationRelativeTo(mainFrame)
                aboutDialog.visible = true
            }
    )
    action(id: 'searchAction',
            name: 'Search',
            mnemonic: 'S',
            accelerator: 'ctrl F',
            shortDescription: 'Search for a value')
    action(id: 'againAction',
            name: 'Again',
            mnemonic: 'A',
            accelerator: 'F3')

    action(id: 'filterOnIpAction',
            name: 'Filter on IP')
    action(id: 'filterOnPortAction',
            name: 'Filter on Port')
    action(id: 'filterOnServiceAction',
            name: 'Filter on Service')
    action(id: 'filterOnPluginAction',
            name: 'Filter on Plugin')
    action(id:'modifyEntryAction',
            name: 'Modify Entry(s)...')
    action(id: 'copyIps',
            name: 'Copy Unique IPs')
    action(id: 'copyIpPorts',
            name: 'Copy Unique port and IPs')
}

frame(id: 'mainFrame',
        title: 'Issue Finder',
        size: [1200, 800] as Dimension,
        extendedState: JFrame.MAXIMIZED_BOTH,
        defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE) {
    menuBar {
        menu('File', mnemonic: 'F') {
            menuItem(newAction)
            menuItem(openAction)
            separator()
            menuItem(exportAction)
            separator()
            menuItem(exitAction)
        }
//        menu('Search', mnemonic: 'S') {
//            menuItem(searchAction)
//            menuItem(againAction)
//        }
        menu('Help', mnemonic: 'H') {
            menuItem(aboutAction)
        }
    }
    splitPane(constraints: BorderLayout.CENTER,
            orientation: JSplitPane.VERTICAL_SPLIT,
            id: 'mainSplit',
            dividerLocation: 0.05,
            enabled: false) {
        scrollPane(id: 'topSplit') {
            table(id: 'mainTable',
                    selectionMode: ListSelectionModel.MULTIPLE_INTERVAL_SELECTION,
                    autoCreateRowSorter: false,
                    autoResizeMode:JTable.AUTO_RESIZE_LAST_COLUMN) {
                tableModel() {
                    propertyColumn(header: 'Scanner', propertyName: 'scanner', preferredWidth: 100, )
                    propertyColumn(header: 'Ip', propertyName: 'ip', preferredWidth: 150)
                    propertyColumn(header: 'Port', propertyName: 'port', preferredWidth: 200)
                    propertyColumn(header: 'Service', propertyName: 'service', preferredWidth: 350)
                    propertyColumn(header: 'Plugin', propertyName: 'plugin', preferredWidth: 550)
                    propertyColumn(header: 'Risk', propertyName: 'severity', preferredWidth: 100)
                }
            }
        }
        splitPane(orientation: JSplitPane.HORIZONTAL_SPLIT, id: 'infoSplit') {
            panel(border: new TitledBorder("Filters: "),
                    id: 'filterPanel',
                    layout: new MigLayout('', '[][grow]', '')) {
                label("Scanner:", constraints: 'right')
                textField(id: 'scannerFilter', columns: 15, constraints: 'grow,wrap')
                label("IP:", constraints: 'right')
                textField(id: 'ipFilter', columns: 15, constraints: 'grow,wrap')
                label("Port:", constraints: 'right')
                textField(id: 'portFilter', columns: 15, constraints: 'grow,wrap')
                label("Service:", constraints: 'right')
                textField(id: 'serviceFilter', columns: 15, constraints: 'grow,wrap')
                label("Plugin:", constraints: 'right')
                textField(id: 'pluginFilter', columns: 15, constraints: 'grow,wrap')
                label("Risk:", constraints: 'right')
                textField(id: 'riskFilter', columns: 15, constraints: 'grow,wrap')
                label("Description:", constraints: 'right')
                textField(id: 'descFilter', columns: 15, constraints: 'grow,wrap')
                label("CVSS Base score:", constraints: 'right')
                textField(id: 'cvssFilter', columns: 15, constraints: 'grow,wrap')
                label("Exploitable:", constraints: 'right')
                checkBox(id: 'exploitFilter', constraints: 'grow,wrap')
            }
            scrollPane(border: new TitledBorder("Description: ")) {
                textArea(id: 'summaryText')
            }
        }
    }
    panel(id: 'statusPanel',
            constraints: BorderLayout.SOUTH,
            border: BorderFactory.createLoweredBevelBorder(),
            layout: new MigLayout('', '15[grow][][]15', '')) {
        label("Application started", id: 'statusLabel')
        label("0 findings", id:'rowLabel')
        label("      0 unique IPs", id: 'ipLabel') // FIXME: Should organise this via MigLayout
    }
}

optionPane(id:'editPane') {
    panel(id:'editPanel',
            border: emptyBorder(5),
            layout: new MigLayout('','[][grow]','')) {
        label("Scanner:", constraints: 'right')
        textField(id: 'scannerEdit', columns: 15, constraints: 'grow,wrap')
        label("Hostname:", constraints: 'right')
        textField(id: 'hostnameEdit', columns: 15, constraints: 'grow,wrap')
        label("IP:", constraints: 'right')
        textField(id: 'ipEdit', columns: 15, constraints: 'grow,wrap')
        label("Port:", constraints: 'right')
        textField(id: 'portEdit', columns: 15, constraints: 'grow,wrap')
        label("Service:", constraints: 'right')
        textField(id: 'serviceEdit', columns: 15, constraints: 'grow,wrap')
        label("Plugin:", constraints: 'right')
        textField(id: 'pluginEdit', columns: 15, constraints: 'grow,wrap')
        label("Risk:", constraints: 'right')
        textField(id: 'severityEdit', columns: 15, constraints: 'grow,wrap')
    }
}

dialog(id: 'aboutDialog',
        owner: mainFrame,
        title: 'About IssueFinder',
        modal: false,
        resizable: false) {
    panel(border: emptyBorder(15),
            layout: new MigLayout('', '','[top][bottom]')
    ) {
        panel(border: lineBorder(color: Color.BLACK), background: Color.WHITE) {
            vbox(border: emptyBorder([15, 0, 15, 15]), constraints: BorderLayout.EAST) {
                hbox {
                    label(text: 'Issue Finder', font: UIManager.getFont('Label.font').deriveFont(Font.BOLD, 14))
                    hglue()
                }
                hbox {
                    panel(layout: new GridLayout(2, 2), opaque: false, border: emptyBorder([5, 10, 5, 50])) {
                        String version = null

                        getClass().classLoader.getResources('META-INF/MANIFEST.MF').each { uri ->
                            uri.openStream().withStream { is ->
                                def attributes = new Manifest(is).mainAttributes
                                version = attributes.getValue('version')
                            }
                        }
                        if (version == null)
                            version = 'DEV'
                        label(text: 'IssueFinder')
                        label(text: 'version ' + version)
                    }
                    hglue()
                }
                hbox {
                    label(text: 'Copyright © 2017 Steven van der Baan')
                }
            }
        }
        button {
            action(id: 'aboutOkAction', name: '    Ok    ', closure: { aboutDialog.visible = false })
        }
    }

}