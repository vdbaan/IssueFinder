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

import ca.odell.glazedlists.*
import ca.odell.glazedlists.event.ListEvent
import ca.odell.glazedlists.event.ListEventListener
import ca.odell.glazedlists.gui.TableFormat
import ca.odell.glazedlists.matchers.AbstractMatcherEditor
import ca.odell.glazedlists.matchers.CompositeMatcherEditor
import ca.odell.glazedlists.matchers.Matcher
import ca.odell.glazedlists.swing.AdvancedTableModel
import ca.odell.glazedlists.swing.GlazedListsSwing
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.Parser
import net.vdbaan.issuefinder.view.MView
import groovy.swing.SwingBuilder

import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener
import java.awt.*
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseListener
import java.util.List

class MC implements ListEventListener<Finding> {

    SwingBuilder swing = new SwingBuilder()
    MView main
    IssuesLoader loader = new IssuesLoader()
    private EventList<Finding> findingEventList = new BasicEventList<Finding>()
    CompositeMatcherEditor<Finding> compositeFilter
    FilterList<Finding> filteredFindings


    MC() {
        findingEventList.getReadWriteLock().readLock().lock()
        try {
            compositeFilter = new CompositeMatcherEditor()
            EventList<Finding> threadProxyList = GlazedListsSwing.swingThreadProxyList(findingEventList)
            SortedList<Finding> sortedFindings = new SortedList<Finding>(threadProxyList, new FindingComparator())
            filteredFindings = new FilterList<>(sortedFindings, compositeFilter)


            AdvancedTableModel<Finding> findingTableModel =
                    GlazedListsSwing.eventTableModelWithThreadProxyList(filteredFindings, new FindingTableFormat())
            main = new MView(swing, sortedFindings, findingTableModel)
            filteredFindings.addListEventListener(this)
        } finally {
            findingEventList.getReadWriteLock().readLock().unlock()
        }
    }

    void show() {
        main.show()
        swing.doLater {

            openAction.closure = { onOpen() }
            newAction.closure = { onNew() }

            compositeFilter.matcherEditors << new IssueSelector(scannerFilter,"scanner")
            compositeFilter.matcherEditors << new IssueSelector(ipFilter,"ip")
            compositeFilter.matcherEditors << new IssueSelector(portFilter,"port")
            compositeFilter.matcherEditors << new IssueSelector(serviceFilter,"service")
            compositeFilter.matcherEditors << new IssueSelector(pluginFilter,"plugin")
            compositeFilter.matcherEditors << new IssueSelector(riskFilter,"risk")
            compositeFilter.matcherEditors << new IssueSelector(descFilter,"description")


            mainTable.selectionModel.addListSelectionListener({ e ->
                if (e.isAdjusting) return
                int min = e.source.minSelectionIndex
                int max = e.source.maxSelectionIndex
                int size = filteredFindings.size()
                List<Finding> result = new ArrayList<>()
                filteredFindings.getReadWriteLock().readLock().lock()
                for (int i = min; i <= max; i++) {
                    if (i >= size) break
                    if (e.source.isSelectedIndex(i))
                        result << filteredFindings.get(i)
                }
                filteredFindings.getReadWriteLock().readLock().unlock()
                display(result)
            })

            mainTable.addMouseListener({ e ->
                if (e.isPopupTrigger()) {
                    int r = mainTable.rowAtPoint(e.point)
                    main.createPopup().show(e.getComponent(), e.getX(), e.getY())
                    Finding f = filteredFindings.get(r)
                    filterOnIpAction.closure = { ipFilter.text = f.ip }
                    filterOnPortAction.closure = { portFilter.text = f.port }
                    filterOnServiceAction.closure = { serviceFilter.text = f.service }
                    filterOnPluginAction.closure = { pluginFilter.text = f.plugin }

                }
            } as MouseListener)
            statusPanel.addMouseListener({ e ->
                if (e.isPopupTrigger()) {
                    main.createStatusPopup().show(e.component, e.x, e.y)
                    copyIps.closure = {
                        swing.doLater {
                            Set<String> ips = new TreeSet<>()
                            filteredFindings.each { ips << it.ip }
                            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard()
                            clpbrd.setContents(new StringSelection(ips.join("\n")), null)
                        }
                    }
                    copyIpPorts.closure = {
                        swing.doLater {
                            Set<String> ips = new TreeSet<>()
                            filteredFindings.each { f ->
                                String port = f.port.split('/')[0]
                                if (port == '0' || port == 'generic') {
                                    // ignore
                                } else {
                                    ips << f.ip + ":" + port
                                }
                            }
                            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard()
                            clpbrd.setContents(new StringSelection(ips.join("\n")), null)
                        }
                    }
                }
            } as MouseListener)

        }
    }

    void display(ArrayList<Finding> findings) {
        swing.doLater {
            summaryText.text = ""
            findings.each { summaryText.text += it.fullDescription() }
            summaryText.setCaretPosition(0)
        }
    }

    void onOpen() {
        main.createOpenFileChooser().with {
            showChooser({ openFiles(Arrays.asList(selectedFiles)) })

        }
    }

    void onNew() {
        findingEventList.clear()
        swing.doLater {
            scannerFilter.text = ""
            ipFilter.text = ""
            portFilter.text = ""
            serviceFilter.text = ""
            pluginFilter.text = ""
            riskFilter.text = ""
            descFilter.text = ""
            summaryText.text = ""
        }
    }

    void openFiles(List<String> files) {
        swing.doLater {
            if (files.size() == 0) return
            main.showLoading()
            statusLabel.text = "Importing files"
            loader.load(files, findingEventList,this)
        }
    }

    void doneLoading() {
        swing.doLater {
            statusLabel.text = "Done"
            main.hideLoading()
        }
    }
    @Override
    void listChanged(ListEvent<Finding> listChanges) {
        swing.doLater {
            HashSet<String> ips = new HashSet<>()
            filteredFindings.each { ips << it.ip }
            ipLabel.text = ips.size()
        }
    }
}

class FindingComparator implements Comparator<Finding> {

    @Override
    int compare(Finding o1, Finding o2) {
        return o1.toString().compareTo(o2.toString())
    }
}

class IssuesLoader implements Runnable {
    private EventList<Finding> loadList = new BasicEventList<Finding>()
    private List<String> files
    private MC mc


    void load(List<String> files, EventList<Finding> list,MC mc) {
        this.files = files
        this.loadList = list
        this.mc = mc
        Thread backThread = new Thread(this)
        backThread.daemon = true
        backThread.start()
    }

    private parseFile(File file) {
        List<Finding> result = Parser.getParser(file.text).parse()
        loadList.getReadWriteLock().writeLock().lock()
        try {
            loadList.addAll(result)
        } finally {
            loadList.getReadWriteLock().writeLock().unlock()
        }
    }
    void run() {
        files.each { file ->
            parseFile(file)
        }
        mc.doneLoading()
    }

}

class FindingTableFormat implements TableFormat<Finding> {
    static Columns = ["Scanner", "IP", "Port", "Service", "Plugin", "Risk"]

    @Override
    int getColumnCount() {
        return Columns.size()
    }

    @Override
    String getColumnName(int column) {
        return Columns[column]
    }

    @Override
    Object getColumnValue(Finding finding, int column) {
        switch (column) {
            case 0: return finding.scanner
            case 1: return finding.ip
            case 2: return finding.port
            case 3: return finding.service
            case 4: return finding.plugin
            case 5: return finding.severity
        }
    }
}

