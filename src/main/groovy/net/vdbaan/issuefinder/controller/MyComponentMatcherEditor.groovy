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

import javax.swing.JCheckBox
import javax.swing.JTextField
import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.text.NumberFormat

class IssueSelector extends AbstractMatcherEditor implements CaretListener, ActionListener {

    def filter
    def editor
    def text

    IssueSelector( editor,  filter) {
        this(editor,filter,"")
    }

    IssueSelector( editor,  filter,  text) {
        this.editor = editor
        if(editor instanceof JTextField)
            editor.addCaretListener(this)
        else
            editor.addActionListener(this)
        this.filter = filter
        this.text = text
    }


    @Override
    void caretUpdate(CaretEvent e) {
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
    void actionPerformed(ActionEvent e) {
        if(e.getSource().isSelected()) {
            Matcher newMatcher = new FindingMatcher(filter, editor.text)
            fireChanged(newMatcher)
        } else {
            fireMatchAll()
        }
    }
}

class FindingMatcher implements Matcher<Finding> {
    String filter
    String text
    FindingMatcher( filter,  text) {
        this.filter = filter
        this.text = text
    }
    @Override
    boolean matches(Finding item) {

        switch (filter) {
            case 'scanner': return test(item.scanner, text)
            case 'ip': return test(String.format('%s (%s)',item.ip,item.hostName), text)
            case 'port': return test(item.port, text)
            case 'service': return test(item.service, text)
            case 'plugin': return test(item.plugin, text)
            case 'risk': return test(item.severity.toString(), text)
            case 'description': return test(item.summary, text)
            case 'cvssbasescore':return atleast(item.baseCVSS,text)
            case 'exploitable': return item.exploitable
            default: return true
        }
    }

    def parser = NumberFormat.getInstance()
    private boolean atleast(item, test) {
        if(test == null || test.length() == 0)
            return true
        if(item == null || item.length() == 0)
            return false

        def val = parser.parse(item.trim()).floatValue()
        def v2 = parser.parse(test.trim()).floatValue()
        return val >= v2
    }

    private boolean test( item,  test) {

        if (test.contains(",")) {
            boolean result = false
            test.split(",").each { val ->
                result |= item.contains(val.trim())
            }
            // test for negative searches
            test.split(",").each { val ->
                if (val.startsWith("-") && val.length() > 1) {
                    result &= (!item.contains(val.substring(1).trim()))
                }
            }
            return result
        } else
            return item.contains(test.trim())
    }
}