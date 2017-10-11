/*
Taken from: http://respostas.guj.com.br/47439-habilitar-copypaste-tableview-funcionando-duvida-editar-funcionalidade
 */
package net.vdbaan.issuefinder.util;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.*;

public class TableUtils {

    /**
     * Install the keyboard handler:
     * + CTRL + C = copy to clipboard
     *
     * @param table
     */
    public static void installCopyPasteHandler(TableView<?> table) {

        // install copy/paste keyboard handler
        table.setOnKeyPressed(new TableKeyEventHandler());
    }

    public static void installCopyPasteHandler(ObjectProperty<EventHandler<? super KeyEvent>> handler) {
        handler.set(new TableKeyEventHandler());
    }

    public static void installCopyPasteHandler(TreeTableView<?> table) {
        table.setOnKeyPressed(new TreeTableKeyEventHandler());
    }
    /**
     * Copy/Paste keyboard event handler.
     * The handler uses the keyEvent's source for the clipboard data. The source must be of type TableView.
     */
    static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        final KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

        public void handle(final KeyEvent keyEvent) {

            if (copyKeyCodeCompination.match(keyEvent)) {

                if (keyEvent.getSource() instanceof TableView) {

                    // copy to clipboard
                    copySelectionToClipboard((TableView<?>) keyEvent.getSource());

                    System.out.println("Selection copied to clipboard");

                    // event is handled, consume it
                    keyEvent.consume();

                }

            }

        }

    }

    static class TreeTableKeyEventHandler implements EventHandler<KeyEvent> {
        final KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

        public void handle(final KeyEvent keyEvent) {
            if (copyKeyCodeCompination.match(keyEvent)) {
                if (keyEvent.getSource() instanceof TreeTableView) {
                    copySelectionToClipboard((TreeTableView<?>) keyEvent.getSource());
                    System.out.println("Selection copied to clipboard");
                    keyEvent.consume();
                }
            }
        }
    }
    /**
     * Get table selection and copy it to the clipboard.
     *
     * @param table
     */
    private static void copySelectionToClipboard(TableView<?> table) {

        StringBuilder clipboardString = new StringBuilder();

        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

        int prevRow = -1;
        int noColumns = table.getColumns().size();
        for (TablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            StringBuilder rowContent = new StringBuilder();
            for (int i = 0; i < noColumns; i++) {
                Object cell = table.getColumns().get(i).getCellData(row);
                rowContent.append("\"");
                rowContent.append((cell == null) ? "" : cell);
                rowContent.append("\"");
                if (i < (noColumns - 1))
                    rowContent.append(",");
            }

            // determine whether we advance in a row (tab) or a column (newline).
            if (prevRow == row) {

                clipboardString.append('\t');

            } else if (prevRow != -1) {

                clipboardString.append('\n');

            }

            // create string from cell
            String text = rowContent.toString();

            // add new item to clipboard
            clipboardString.append(text);

            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());

        // set clipboard content
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }

    private static void copySelectionToClipboard(TreeTableView<?> table) {
        StringBuilder clipboardString = new StringBuilder();

        ObservableList<? extends TreeTablePosition<?, ?>> positionList = table.getSelectionModel().getSelectedCells();

        int prevRow = -1;
        int noColumns = table.getColumns().size();
        for (TreeTablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            StringBuilder rowContent = new StringBuilder();
            for (int i = 0; i < noColumns; i++) {
                Object cell = table.getColumns().get(i).getCellData(row);
                rowContent.append("\"");
                rowContent.append((cell == null) ? "" : cell);
                rowContent.append("\"");
                if (i < (noColumns - 1))
                    rowContent.append(",");
            }

            // determine whether we advance in a row (tab) or a column (newline).
            if (prevRow == row) {

                clipboardString.append('\t');

            } else if (prevRow != -1) {

                clipboardString.append('\n');

            }

            // create string from cell
            String text = rowContent.toString();

            // add new item to clipboard
            clipboardString.append(text);

            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());

        // set clipboard content
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }
}