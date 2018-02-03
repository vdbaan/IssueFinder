/*
 *  Copyright (C) 2017  S. van der Baan
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.vdbaan.issuefinder.presenter

import groovy.util.logging.Log
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.web.WebView
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.StringConverter
import net.vdbaan.issuefinder.Main2
import net.vdbaan.issuefinder.config.Config
import net.vdbaan.issuefinder.db.DbHandler
import net.vdbaan.issuefinder.db.DbHandlerImpl
import net.vdbaan.issuefinder.filter.FindingPredicate
import net.vdbaan.issuefinder.filter.FindingPredicateParser
import net.vdbaan.issuefinder.filter.FindingPredicateParserRuntimeException
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.util.BrowserPopupHandler
import net.vdbaan.issuefinder.util.Container
import net.vdbaan.issuefinder.view.*
import net.vdbaan.issuefinder.view.impl.FindingTabController
import net.vdbaan.issuefinder.view.impl.IssueTabController

import java.util.jar.Manifest
import java.util.logging.Level

@Log
//@CompileStatic
class MainPresenter {
    final static String BUILD_FILTER = 'Build Filter ...'

    private MainView view
    private FindingTabController findingTabController
    private IssueTabController issueTabController
    DbHandler db
    ObservableList<Finding> masterData = FXCollections.observableArrayList()

    FindingPredicateParser findingPredicateParser = new FindingPredicateParser()

    SimpleObjectProperty<FindingPredicate> findingPredicate = new SimpleObjectProperty<>()

    String buildFilter = ''

    MainPresenter(final MainView mainView) {
        this.view = mainView
        view.newActionHandler = (this.&newAction)
        view.loadReportHandler = (this.&loadReport)
        view.saveDatabaseHandler = (this.&saveDatabase)
        view.loadDatabaseHandler = (this.&loadDatabase)
        view.showPreferencesHandler = (this.&showPreferences)
        view.exitApplicationHandler = (this.&exitApplication)
        view.showHelpHandler = (this.&showHelp)
        view.showStatisticsHandler = (this.&showStatistics)
        view.showAboutHandler = (this.&showAbout)
        view.filterClickedHandler = (this.&filterClicked)
        view.doFilterHandler = (this.&doFilter)
        view.clearFilterHandler = (this.&clearFilter)
        view.copyUniqueIPsHandler = (this.&copyUniqueIPs)
        view.copyUniquePortAndIPsHandler = (this.&copyUniquePortAndIPs)
        view.tabChangedHandler = (this.&tabHandler)
//        view.addFilterTextListener(this.&handleReturn)
        view.onActionHandler = (this.&onAction)
        view.filterTextItems = FXCollections.observableList(buildItems((List<String>) Config.instance.getProperty(Config.FILTERS)))
        db = new DbHandlerImpl()
        masterData.addListener(this.&dataListener as ListChangeListener)
        view.setComboCellFactory(this.&comboCellFactory)
        view.mainFilter.converter = new StringConverter<String>() {
            @Override
            String toString(String object) {
                if (object == BUILD_FILTER)
                    return ''
                else
                    return object
            }

            @Override
            String fromString(String string) {
                return string
            }
        }
    }

    void setIssuesTabController(final FindingTabController issuesTabController) {
        this.findingTabController = issuesTabController
        issuesTabController.bindMasterData(masterData)
        issuesTabController.masterView = (view)
    }

    void setFindingsTabController(final IssueTabController findingsTabController) {
        this.issueTabController = findingsTabController
        findingsTabController.bindMasterData(masterData)
//        issueTabController.setMasterView(view)
    }

    void tabHandler(final Event event) {
        issueTabController.changeTab()
    }

    void newAction(final ActionEvent event) {
        db.deleteAll()
        masterData.removeAll(masterData)
        view.selectSelectIssueTab()
        view.disableFindingsTab()
        view.statusLabel = ('Reset')
    }

    void loadReport(final ActionEvent event) {
        openFiles(view.retentionFileChooser.showOpenMultipleDialog(view.window, 'Load Reports'))
    }

    void saveDatabase(final ActionEvent event) {
        final File location = view.retentionFileChooser.showSaveDialog(view.window, 'Save DataBase')
        if (location != null) {
            db.saveDb(location.absolutePath)
        }
    }

    void loadDatabase(final ActionEvent event) {
        final File location = view.retentionFileChooser.showOpenDialog(view.window, 'Load DB')
        if (location != null) {
            db.deleteAll()
            db.loadDb(location.absolutePath)
            loadAll()
        }
    }

    private Dialog settingsDialog

    void showPreferences(final ActionEvent event) {
        if (settingsDialog == null) {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource('/settingsDialog.fxml'))
            settingsDialog = new Dialog()
            settingsDialog.initOwner(view.window)
            settingsDialog.title = ('Preferences')
            settingsDialog.initModality(Modality.APPLICATION_MODAL)
            settingsDialog.dialogPane = (loader.load() as DialogPane)
            final SettingsDialogView settingsController = loader.controller

            settingsDialog.resultConverter = { final buttonType ->
                if (buttonType == ButtonType.APPLY) {
                    settingsController.storePreferences()
                }
            }
        }
        settingsDialog.showAndWait()
    }

    static void exitApplication(final ActionEvent event) { Platform.exit() }

    Stage helpDialog

    void showHelp(final ActionEvent event) {
        if (helpDialog == null) {
            helpDialog = new Stage()
            helpDialog.title = 'IssueFinder Help'
            final WebView view = new WebView()

            view.engine.load(getClass().getResource('/helptext.html').toExternalForm())
            view.engine.createPopupHandler = new BrowserPopupHandler(Main2.instance)
            helpDialog.initModality(Modality.NONE)
            helpDialog.scene = (new Scene(view))
        }
        helpDialog.show()
    }

    private Dialog summaryDialog
    private SummaryDialogView summaryController

    void showStatistics(final ActionEvent event) {
        if (summaryDialog == null) {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource('/summaryDialog.fxml'))
            summaryDialog = new Dialog()
            summaryDialog.initOwner(view.window)
            summaryDialog.title = 'Summary'
            summaryDialog.initModality(Modality.APPLICATION_MODAL)
            summaryDialog.dialogPane = (loader.load() as DialogPane)
            summaryController = loader.controller
        }
        summaryController.summary = (buildSummary())
        summaryDialog.showAndWait()
    }

    private Map<String, Container> buildSummary() {
        final Map<String, Container> summaryInfo = new HashMap()
        masterData.each { final finding ->
            final Container c = summaryInfo[finding.ip] ?: new Container()
            if (finding.portStatus == 'open') {
                c.listedports << finding.port
                c.listedservices << finding.service
            }
            if (finding.severity < c.highest) { // Severity runs from Critical (0) to UNKNOWN (5)
                c.plugins.clear()
                c.highest = finding.severity
            }
            if (finding.severity == c.highest) {
                c.plugins << finding.plugin
            }
            (summaryInfo[finding.ip] = c)
        }
        return summaryInfo
    }

    private Dialog aboutDialog

    void showAbout(final ActionEvent event) {
        if (aboutDialog == null) {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource('/aboutDialog.fxml'))
            aboutDialog = new Dialog()
            aboutDialog.initOwner(view.window)
            aboutDialog.title = 'About'
            aboutDialog.initStyle(StageStyle.UNDECORATED)
            aboutDialog.initModality(Modality.APPLICATION_MODAL)
            final DialogPane pane = loader.load()
            final AboutDialogView controller = loader.controller
            aboutDialog.dialogPane = (pane)
            controller.version = version
        }

        aboutDialog.showAndWait()
    }

    private String getVersion() {
        try {
            final InputStream inputStream = getClass().getResourceAsStream('/META-INF/MANIFEST.MF')
            final Manifest manifest = new Manifest(inputStream)

            return manifest.mainAttributes.getValue('version') ?: 'DEVELOPMENT'
        } catch (final Exception exception) {
            return "DEVELOPMENT"
        }
    }

    void filterClicked(final MouseEvent event) {
        view.filterTextStyleClass.clear()
        view.filterTextStyleClass.add('text-input')
    }

    void doFilter(final ActionEvent event) {
        final String value = view.filterTextValue ?: ''
        if (value != '' && value != BUILD_FILTER) {
            try {
                findingPredicate.value = findingPredicateParser.parse(value)
                if (findingPredicate.value != null) {
                    masterData.removeAll(masterData)
                    masterData.addAll(db.getAllFinding(findingPredicate.value.toString()))
                    testSize()
                    final List<String> list = view.filterTextItems
                    if (!list.contains(value)) {
                        final List templist = new ArrayList()
                        templist.add(BUILD_FILTER)
                        templist.add(value)
                        list.each { if (!templist.contains(it)) templist += it }
//                        templist.addAll(list)
                        view.filterTextItems = (templist)
                        view.mainFilter.selectionModel.select(value)
                    }
                } else {
                    clearFilter(null)
                }
            } catch (final StringIndexOutOfBoundsException exception) {
                log.log(Level.FINE, 'Got an exception', exception)
                view.filterTextStyleClass.add('text-input-wrong')

            } catch (final FindingPredicateParserRuntimeException runtimeException) {
                log.log(Level.FINE, 'Got an exception', runtimeException)
                view.filterTextStyleClass.add('text-input-wrong')
            }
        } else {
            masterData.removeAll(masterData)
            masterData.addAll(db.getAllFinding(null))
        }
    }

    void clearFilter(final ActionEvent event) {
        view.filterText = (null)
        filterClicked(null)
        loadAll()
    }

    void copyUniqueIPs(final ActionEvent event) {
        findingTabController.copyUniqueIPs(masterData)
    }

    void copyUniquePortAndIPs(final ActionEvent event) {
        findingTabController.copyUniquePortAndIPs(masterData)
    }

    private Stage progressDialog
    private ProgressDialogView progressController

    private openFiles(final List<File> files) {
        if (files?.size() > 0) {
            if (progressDialog == null) {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource('/progressDialog.fxml'))
                progressDialog = new Stage()
                progressDialog.initOwner(view.window)
                progressDialog.initStyle(StageStyle.UNDECORATED)
                progressDialog.initModality(Modality.APPLICATION_MODAL)
                progressDialog.scene = (new Scene(loader.load() as Parent))

                progressController = loader.controller
                progressController.masterView = (view)
                progressController.callback = (this.&loadAll)
            }

            progressController.fileList = (files)

            progressDialog.showAndWait()
        } else log.info('No files to process')
    }

    void testSize() {
        final int sizeList = masterData.size()
        final int sizeDb = db.numrows
        if (sizeList < sizeDb) {
            view.rowInfoLabel = (String.format('Showing %d of %d Findings', sizeList, sizeDb))
            final Alert popup = new Alert(Alert.AlertType.WARNING)
            popup.title = ('Warning')
            popup.contentText = String.format('Only showing %d of %d Findings\nPlease use a filter to reduce the number of finding', sizeList, sizeDb)
            popup.showAndWait()
        }
    }

    void loadAll() {
        log.fine('Loading All')
        masterData.removeAll(masterData)
        masterData.addAll(db.getAllFinding(null))
        testSize()
        // trigger findings
        findingPredicate.value = (new FindingPredicate())
        view.enableFindingsTab()
    }

    void dataListener(final ListChangeListener.Change<? extends Finding> c) {
        view.rowInfoLabel = (String.format('%6d Findings', masterData.size()))
        final HashSet<String> ipsSet = new HashSet<>()
        masterData.each { ipsSet << it.ip }
        view.ipInfoLabel = (String.format('%6d unique Location', ipsSet.size()))
    }

    Dialog predicateDialog
    PredicateDialogView predicateController

    EventHandler<ActionEvent> filter = { e -> e.consume() }

    void onAction(ActionEvent event) {
        final String value = view.filterTextValue ?: ''
        if (value == BUILD_FILTER) {
            if (predicateDialog == null) {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource('/predicateDialog.fxml'))
                predicateDialog = new Dialog()
                predicateDialog.initOwner(view.window)
                predicateDialog.title = 'Create Filter'
                predicateDialog.initModality(Modality.APPLICATION_MODAL)
                predicateDialog.dialogPane = (loader.load() as DialogPane)
                predicateController = loader.controller
                predicateDialog.resultConverter = { final buttonType ->
                    if (buttonType == ButtonType.APPLY) {
                        String predicate = predicateController.buildPredicate()
                        if (predicate != '') {
                            // to avoid a retriggering of this handler, we temporarily disable it
                            view.mainFilter.addEventFilter(ActionEvent.ACTION, filter)
                            view.mainFilter.value = predicate
                            doFilter(null)
                            view.mainFilter.removeEventFilter(ActionEvent.ACTION, filter)
                        }
                    }
                }
            }
            // as we are within the handling of the action that was triggered by the selection, we should not
            // wait for it, but just show the dialog otherwise the created predicate will be overwritten by
            // the values from this action
            predicateController.resetElements()
            predicateDialog.show()
        } else
            doFilter(null)
    }

    void handleReturn(final ObservableValue observable, final Object oldValue, final Object newValue) {
        if (oldValue == BUILD_FILTER) {
            // after list[0] has been changed, this will be triggered again, but then with the new value
            // we need to check if BUID_FILTER is still an option
            final List<String> list = view.filterTextItems
            if (!list.contains(BUILD_FILTER))
                list.add(0, BUILD_FILTER)
        }
        if (newValue == BUILD_FILTER) {

            if (predicateDialog == null) {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource('/predicateDialog.fxml'))
                predicateDialog = new Dialog()
                predicateDialog.initOwner(view.window)
                predicateDialog.title = 'Create Filter'
                predicateDialog.initModality(Modality.APPLICATION_MODAL)
                predicateDialog.dialogPane = (loader.load() as DialogPane)
                predicateController = loader.controller
                predicateDialog.resultConverter = { final buttonType ->
                    if (buttonType == ButtonType.APPLY) {
                        String value = predicateController.buildPredicate()
                        if (value != '') {
                            // to avoid a retriggering of this handler, we temporarily disable it
                            view.mainFilter.addEventFilter(ActionEvent.ACTION, filter)
                            view.mainFilter.value = value
                            view.mainFilter.removeEventFilter(ActionEvent.ACTION, filter)
                        }
                    }
                }
            }
            // as we are within the handling of the action that was triggered by the selection, we should not
            // wait for it, but just show the dialog otherwise the created predicate will be overwritten by
            // the values from this action
            predicateDialog.show()

        }

        doFilter(null)
    }

    ListCell<String> comboCellFactory(Object listView) {
        return new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty)
                if (empty) {
                    setText(null)
                } else {
                    getStyleClass().remove('first')
                    if (item == BUILD_FILTER) {
                        getStyleClass().add('first')
                    }
                    setText(item)
                }
            }
        }
    }

    List<String> buildItems(List<String> items, String value = '') {
        List<String> result = new ArrayList<>()
        result += BUILD_FILTER
        if (value != '' && !items.contains(value))
            result.add(value)
        items.each { if (it != BUILD_FILTER) result += it }
        return result
    }
}
