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
import ca.odell.glazedlists.matchers.CompositeMatcherEditor
import ca.odell.glazedlists.swing.AdvancedTableModel
import ca.odell.glazedlists.swing.GlazedListsSwing
import com.example.myapp.console.Autocomplete
import groovy.xml.MarkupBuilder
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.Parser
import net.vdbaan.issuefinder.view.MView
import groovy.swing.SwingBuilder

import javax.swing.JOptionPane
import javax.swing.KeyStroke
import java.awt.*
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseListener
import java.util.List
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MC implements ListEventListener<Finding> {

    int WARNING_LEVEL = 100000
    SwingBuilder swing = new SwingBuilder()
    MView main
    IssuesLoader loader = new IssuesLoader()
    private EventList<Finding> findingEventList = new BasicEventList<Finding>()
    CompositeMatcherEditor<Finding> compositeFilter
    EventList<Finding> threadProxyList



    MC() {
        findingEventList.getReadWriteLock().readLock().lock()
        try {
            compositeFilter = new CompositeMatcherEditor()
            FilterList<Finding> filteredFindings = new FilterList<>(findingEventList, compositeFilter)
            SortedList<Finding> sortedFindings   = new SortedList<Finding>(filteredFindings,null)

            threadProxyList = GlazedListsSwing.swingThreadProxyList(sortedFindings)
            AdvancedTableModel<Finding> findingTableModel =
                    GlazedListsSwing.eventTableModelWithThreadProxyList(threadProxyList, new FindingTableFormat())
            main = new MView(swing, sortedFindings, findingTableModel)
            threadProxyList.addListEventListener(this)

        } finally {
            findingEventList.getReadWriteLock().readLock().unlock()
        }
    }

    void show() {
        main.show()
        swing.doLater {

            openAction.closure = { onOpen() }
            newAction.closure = { onNew() }
            exportAction.closure = {onExport()}

            compositeFilter.matcherEditors << new IssueSelector(scannerFilter,"scanner")
            compositeFilter.matcherEditors << new IssueSelector(ipFilter,"ip")
            compositeFilter.matcherEditors << new IssueSelector(portFilter,"port")
            compositeFilter.matcherEditors << new IssueSelector(serviceFilter,"service")
            compositeFilter.matcherEditors << new IssueSelector(pluginFilter,"plugin")
            compositeFilter.matcherEditors << new IssueSelector(riskFilter,"risk")
            compositeFilter.matcherEditors << new IssueSelector(descFilter,"description")
            compositeFilter.matcherEditors << new IssueSelector(cvssFilter,"cvssbasescore")
            compositeFilter.matcherEditors << new IssueSelector(exploitFilter,"exploitable")

            mainTable.selectionModel.addListSelectionListener({ e ->
                if (e.isAdjusting) return
                int min = e.source.minSelectionIndex
                int max = e.source.maxSelectionIndex
                int size = threadProxyList.size()
                List<Finding> result = new ArrayList<>()
                threadProxyList.getReadWriteLock().readLock().lock()
                for (int i = min; i <= max; i++) {
                    if (i >= size) break
                    if (e.source.isSelectedIndex(i))
                        result << threadProxyList.get(i)
                }
                threadProxyList.getReadWriteLock().readLock().unlock()
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
                    modifyEntryAction.closure = {onEntry()}

                }
            } as MouseListener)
            statusPanel.addMouseListener({ e ->
                if (e.isPopupTrigger()) {
                    main.createStatusPopup().show(e.component, e.x, e.y)
                    copyIps.closure = {
                        swing.doLater {
                            Set<String> ips = new TreeSet<>()
                            threadProxyList.each { ips << it.ip }
                            ips.remove('none') // FIXME due to NetSparkerParser
                            def sorted = ips.sort {a,b ->
                                def ip1 = a.split("\\.")
                                def ip2 = b.split("\\.")
                                def uip1 = String.format("%3s.%3s.%3s.%3s",ip1[0],ip1[1],ip1[2],ip1[3])
                                def uip2 = String.format("%3s.%3s.%3s.%3s",ip2[0],ip2[1],ip2[2],ip2[3])
                                uip1 <=> uip2
                            }
                            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard()
                            clpbrd.setContents(new StringSelection(sorted.join("\n")), null)
                        }
                    }
                    copyIpPorts.closure = {
                        swing.doLater {
                            Set<String> ips = new TreeSet<>()
                            threadProxyList.each { f ->
                                String port = f.port.split('/')[0]
                                if (port.isNumber() && port != '0') {
                                    if (!f.ip.equalsIgnoreCase('none')) // FIXME due to NetSparkerParser
                                        ips << f.ip + ":" + port
                                }
                            }
                            def sorted = ips.sort {a,b ->
                                def ip1 = a.split(":")[0].split("\\.")
                                def ip2 = b.split(":")[0].split("\\.")
                                def uip1 = String.format("%03d.%03d.%03d.%03d.%05d",ip1[0] as int,ip1[1] as int,ip1[2] as int,ip1[3] as int,(a.split(":")[1]) as int)
                                def uip2 = String.format("%03d.%03d.%03d.%03d.%05d",ip2[0] as int,ip2[1] as int,ip2[2] as int,ip2[3] as int,(b.split(":")[1]) as int)
                                uip1 <=> uip2
                            }
                            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard()
                            clpbrd.setContents(new StringSelection(sorted.join("\n")), null)
                        }
                    }
                }
            } as MouseListener)

        }
    }

    def COMMIT_ACTION = "commit"
    void setupAutoComplete() {
        swing.doLater {
            SortedSet<String> risks = new TreeSet<>()
            threadProxyList.each { risks << it.severity.name() }
            setupAutoComplete(riskFilter, risks.asList())

            SortedSet<String> plugins = new TreeSet<>()
            threadProxyList.each { plugins << it.plugin }
            setupAutoComplete(pluginFilter, plugins.asList())

            SortedSet<String> services = new TreeSet<>()
            threadProxyList.each { services << it.service }
            setupAutoComplete(serviceFilter, services.asList())
        }
    }

    void setupAutoComplete(filter,list) {
            filter.setFocusTraversalKeysEnabled(false)

            Autocomplete autoComplete = new Autocomplete(filter, list)
            filter.getDocument().addDocumentListener(autoComplete)
            filter.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION)
            filter.getActionMap().put(COMMIT_ACTION, autoComplete.getCommitAction())
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

    void onEntry() {
        swing.doLater {
            int ret = JOptionPane.showConfirmDialog(mainFrame, editPanel, "Edit finding(s)", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
            if(ret == JOptionPane.OK_OPTION) {
                def min =  mainTable.selectionModel.minSelectionIndex
                def max = mainTable.selectionModel.maxSelectionIndex
                (min..max).each {pos ->
                    if (mainTable.selectionModel.isSelectedIndex(pos)) {
                        threadProxyList.get(pos).scanner = scannerEdit.text?: filteredFindings.get(pos).scanner
                        threadProxyList.get(pos).hostName = hostnameEdit.text?:filteredFindings.get(pos).hostName
                        threadProxyList.get(pos).ip = ipEdit.text?: filteredFindings.get(pos).ip
                        threadProxyList.get(pos).port = portEdit.text?: filteredFindings.get(pos).port
                        threadProxyList.get(pos).service = serviceEdit.text?: filteredFindings.get(pos).service
                        threadProxyList.get(pos).severity = getSeverity(severityEdit.text)?: filteredFindings.get(pos).severity
                    }
                }
                mainTable.model.fireTableDataChanged()
                scannerEdit.text = null
                hostnameEdit.text = null
                ipEdit.text = null
                portEdit.text = null
                serviceEdit.text = null
                severityEdit.text = null
            }
        }
    }

    private Finding.Severity getSeverity(text) {
        switch(text?.toUpperCase()){
            case 'CRITICAL':return Finding.Severity.CRITICAL
            case 'HIGH':return Finding.Severity.HIGH
            case 'MEDIUM':return Finding.Severity.MEDIUM
            case 'LOW':return Finding.Severity.LOW
            case 'INFO':return Finding.Severity.INFO
            case 'UNKNOWN':return Finding.Severity.UNKNOWN
            default: return null
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

    void onExport() {
        // filteredFindings
        main.createSaveFileChooser().with {
            showChooser({ saveAs(selectedFile,fileFilter) })
        }
    }

    void saveAs(File fileName, javax.swing.filechooser.FileFilter filter) {
        if(! filter.accept(fileName)) {
            fileName = new File(fileName.getAbsolutePath().concat(filter.@extensions[0]))
        }
        if(fileName.path.endsWith('xml')) {
            exportAsXML(fileName)
        } else if(fileName.path.endsWith('csv')) {
            exportAsCSV(fileName)
        }

    }

    void exportAsXML(File fileName) {
        fileName.withWriter { out ->
            def xml = new MarkupBuilder(out)
            xml.findings {
                threadProxyList.each { f ->
                    finding(scanner: f.scanner, ip: f.ip, port: f.port, service: f.service) {
                        plugin("" + f.plugin)
                        severity("" + f.severity)
                        summary("" + f.summary)
                    }
                }
            }
        }
    }

    void exportAsCSV(File fileName) {
        fileName.withWriter { out ->
            out.writeLine('"Scanner","ip","port","service","plugin","severity"')
            threadProxyList.each { f ->
                out.writeLine("\"${f.scanner}\",\"${f.ip}\",\"${f.port}\",\"${f.service}\",\"${f.plugin}\",\"${f.severity}\"")
            }
        }
    }

    static int filesDone
    static int filesTotal
    void openFiles(List<String> files) {
        swing.doLater {
            if (files.size() == 0) return
            filesTotal = files.size()
            filesDone = 0
            main.showLoading()
            statusLabel.text = String.format("Importing files %d/%d",filesDone,filesTotal)
            loader.load(files, findingEventList,this)
        }
    }

    void fileDone() {
        swing.doLater {
            filesDone += 1
            statusLabel.text = String.format("Importing files %d/%d",filesDone,filesTotal)
        }

    }
    void doneLoading() {
        swing.doLater {
            statusLabel.text = "Done"
            main.hideLoading()
            setupAutoComplete()
        }
    }

    void warnToManyRows() {
        swing.doLater {
            main.showWarning()
        }
    }

    @Override
    void listChanged(ListEvent<Finding> listChanges) {
        swing.doLater {
            HashSet<String> ips = new HashSet<>()
            threadProxyList.each { ips << it.ip }
            ips.remove('none') // FIXME due to NetSparkerParser
            ipLabel.text = String.format(' %6d unique IPs',ips.size())
            rowLabel.text = String.format('%d findings',threadProxyList.size())
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
        if (file == null) return
        def parser = Parser.getParser(file.text)
        if (parser == null) {
            println "No parser found for: "+file.getName()
            return
        }
        List<Finding> result = parser.parse()
        loadList.getReadWriteLock().writeLock().lock()
        try {
            loadList.addAll(result)
//            println String.format("Added: %8d, new size: %8d rows",result.size(),loadList.size())
            if (loadList.size() > mc.WARNING_LEVEL) {
                mc.warnToManyRows()
            }
        } catch(Exception e) {
            // pass (for now)
        } finally {
            loadList.getReadWriteLock().writeLock().unlock()
        }
        mc.fileDone()
    }
    void run() {

        int numCores = Runtime.getRuntime().availableProcessors()
        def threadPool = Executors.newFixedThreadPool(numCores)
        try {
            List<Future> futures = files.collect(){ file ->
                threadPool.submit({->
                    parseFile file } as Callable)
            }
            futures.each{it.get()}
        } finally {
            threadPool.shutdown()
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
            case 1: return String.format('%s (%s)',finding.ip,finding.hostName)
            case 2: return finding.port
            case 3: return finding.service
            case 4: return finding.plugin
            case 5: return finding.severity
        }
    }
}

