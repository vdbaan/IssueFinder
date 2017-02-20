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

package com.example.myapp.console;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/*
Taken from Scott Robinson
http://stackabuse.com/example-adding-autocomplete-to-jtextfield/

The following small additions where made:
- added getCommitAction as Groovy has trouble instantiating that class
- removed toLower to have case sensitive searching
- removed static from enum
 */

public class Autocomplete implements DocumentListener {

    private enum Mode {
        INSERT,
        COMPLETION
    };

    private JTextField textField;
    private final List<String> keywords;
    private Mode mode = Mode.INSERT;

    public Autocomplete(JTextField textField, List<String> keywords) {
        this.textField = textField;
        this.keywords = keywords;
        Collections.sort(keywords);
    }

    @Override
    public void changedUpdate(DocumentEvent ev) { }

    @Override
    public void removeUpdate(DocumentEvent ev) { }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        if (ev.getLength() != 1)
            return;

        int pos = ev.getOffset();
        String content = null;
        try {
            content = textField.getText(0, pos + 1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (!Character.isLetter(content.charAt(w))) {
                break;
            }
        }

        // Too few chars
        if (pos - w < 2)
            return;

        String prefix = content.substring(w + 1);
        int n = Collections.binarySearch(keywords, prefix);
        if (n < 0 && -n <= keywords.size()) {
            String match = keywords.get(-n - 1);
            if (match.startsWith(prefix)) {
                // A completion is found
                String completion = match.substring(pos - w);
                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
            }
        } else {
            // Nothing found
            mode = Mode.INSERT;
        }
    }

    public CommitAction getCommitAction() {
        return new CommitAction();
    }
    public class CommitAction extends AbstractAction {
        /**
         *
         */
        private static final long serialVersionUID = 5794543109646743416L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textField.getSelectionEnd();
                StringBuffer sb = new StringBuffer(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                textField.replaceSelection("\t");
            }
        }
    }

    private class CompletionTask implements Runnable {
        private String completion;
        private int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            StringBuffer sb = new StringBuffer(textField.getText());
            sb.insert(position, completion);
            textField.setText(sb.toString());
            textField.setCaretPosition(position + completion.length());
            textField.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }

}