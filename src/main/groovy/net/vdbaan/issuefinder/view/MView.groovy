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

import ca.odell.glazedlists.SortedList
import ca.odell.glazedlists.swing.AdvancedTableModel
import ca.odell.glazedlists.swing.TableComparatorChooser
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.component.Colour
import groovy.swing.SwingBuilder
import net.vdbaan.issuefinder.model.Finding
import net.vdbaan.issuefinder.view.component.Colour

import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableCellRenderer
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.GridBagLayout


class MView {
    SwingBuilder swing

    MView(SwingBuilder swing, SortedList<Finding> sortedFindings, AdvancedTableModel<Finding> findingTableModel) {
        this.swing = swing
        swing.lookAndFeel('nimbus')
        swing.edt {
            build(Description)
            mainTable.setModel(findingTableModel)
            TableComparatorChooser.install(mainTable, sortedFindings, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE)
            mainTable.setDefaultRenderer(Object.class, new TableCellRenderer() {
                private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer()

                Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
                    if(isSelected) return c
                    Object o = table.getModel().getValueAt(row, column)
                    c.setForeground(Color.BLACK)
                    if (row % 2 == 0) c.setBackground(Colour.INFO)
                    else c.setBackground(Colour.INFO_ODD)
                    if (!(o instanceof Finding.Severity)) return c
                    Finding.Severity sev = (Finding.Severity) o
                    switch (sev) {
                        case Finding.Severity.CRITICAL:
                            if (row % 2 == 0) c.setBackground(Colour.CRITICAL)
                            else c.setBackground(Colour.CRITICAL_ODD)
                            c.setForeground(Color.yellow)
                            break
                        case Finding.Severity.HIGH:
                            if (row % 2 == 0) c.setBackground(Colour.HIGH)
                            else c.setBackground(Colour.HIGH_ODD)
                            break
                        case Finding.Severity.MEDIUM:
                            if (row % 2 == 0) c.setBackground(Colour.MEDIUM)
                            else c.setBackground(Colour.MEDIUM_ODD)
                            break
                        case Finding.Severity.LOW:
                            if (row % 2 == 0) c.setBackground(Colour.LOW)
                            else c.setBackground(Colour.LOW_ODD)
                            break
                        case Finding.Severity.INFO:
                            if (row % 2 == 0) c.setBackground(Colour.INFO)
                            else c.setBackground(Colour.INFO_ODD)
                            break
                    }
                    return c
                }
            })
        }
    }

    void show() {
        swing.edt {
            mainFrame.show()
            mainSplit.dividerLocation = 0.999
        }
    }

    private JPanel glass
    void showLoading() {
        swing.edt {
            if(glass == null) {
                glass = mainFrame.getGlassPane()
                JPanel p2 = new JPanel()
                p2.setBackground(Color.WHITE)
                p2.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(10,10,10,10), BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),BorderFactory.createEmptyBorder(10,10,10,10))))

                ImageIcon loading = null
                URL imgURL = getClass().getResource("/ajax-loader.gif")
                if (imgURL != null)
                    loading = new ImageIcon(imgURL)
                glass.setLayout(new GridBagLayout())
                JLabel label = new JLabel("loading ... ", loading, JLabel.CENTER)
                p2.add(label)
                glass.add(p2)
                glass.setSize(mainFrame.getSize())

            }
            glass.setVisible(true)
            mainFrame.enable(false)
        }
    }

    void hideLoading() {
        swing.edt {
            mainFrame.enable(true)
            glass.setVisible(false)
        }
    }

    JFileChooser chooser
    JFileChooser createOpenFileChooser() {
        if (chooser == null) {
            chooser = new JFileChooser() {
                void showChooser(Closure okClosure) {
                    if (showOpenDialog(swing.mainFrame) == APPROVE_OPTION) {
                        okClosure()
                    }
                }
            }
            chooser.setMultiSelectionEnabled(true)
        }
        return chooser
    }

    JPopupMenu createPopup() {
        return swing.popupMenu() {
            menuItem(action:swing.filterOnIpAction)
            menuItem(action:swing.filterOnPortAction)
            menuItem(action:swing.filterOnServiceAction)
            menuItem(action:swing.filterOnPluginAction)
        }
    }
    JPopupMenu createStatusPopup() {
        return swing.popupMenu() {
            menuItem(action:swing.copyIps)
            menuItem(action:swing.copyIpPorts)
        }
    }
}

