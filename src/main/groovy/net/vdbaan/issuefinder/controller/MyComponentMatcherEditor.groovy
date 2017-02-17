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

import ca.odell.glazedlists.matchers.AbstractMatcherEditor
import ca.odell.glazedlists.matchers.Matcher
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.model.Finding.Severity

import javax.swing.JTextField
import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener

class IssueSelector extends AbstractMatcherEditor implements  CaretListener {


    String filter
    JTextField editor
    String text

    IssueSelector(JTextField editor, String filter) {
        this(editor,filter,"")
    }
    IssueSelector(JTextField editor, String filter, String text) {
        this.editor = editor
        editor.addCaretListener(this)
        this.filter = filter
        this.text = text
    }

    private void refilter(CaretEvent event) {
        if (editor.text == null || editor.text.length() == 0) {
                fireMatchAll()
        } else {
            if (editor.text != text) {
                Matcher newMatcher = new FindingMatcher(filter,editor.text)
                fireChanged(newMatcher)
            }
        }
    }

    @Override
    void caretUpdate(CaretEvent e) {
        refilter(e)
    }
}

class FindingMatcher implements Matcher<Finding> {
    String filter
    String text
    FindingMatcher(String filter, String text) {
        this.filter = filter
        this.text = text
    }
    @Override
    boolean matches(Finding item) {

        switch (filter) {
            case 'scanner': return test(item.scanner, text)
            case 'ip': return test(item.ip, text)
            case 'port': return test(item.port, text)
            case 'service': return test(item.service, text)
            case 'plugin': return test(item.plugin, text)
            case 'risk': return test(item.severity.toString(), text)
            case 'description': return test(item.summary, text)
            default: return true
        }
    }

    private boolean test(String item, String test) {
        if (test.contains(",")) {
            boolean result = false
            test.split(",").each { val ->
                result |= item.contains(val)
            }
            // test for negative searches
            test.split(",").each { val ->
                if (val.startsWith("-") && val.length() > 1) {
                    result &= (!item.contains(val.substring(1)))
                }
            }
            return result
        } else
            return item.contains(test)
    }
}