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

import javafx.event.ActionEvent
import javafx.event.EventHandler


interface SettingsView extends BaseView {

    EventHandler<ActionEvent> getAddFilterAction()

    void setAddFilterAction(EventHandler<ActionEvent> addFilterAction)

    EventHandler<ActionEvent> getCancelAllAction()

    void setCancelAllAction(EventHandler<ActionEvent> cancelAllAction)

    EventHandler<ActionEvent> getDeleteFilterAction()

    void setDeleteFilterAction(EventHandler<ActionEvent> deleteFilterAction)

    EventHandler<ActionEvent> getEditFilterAction()

    void setEditFilterAction(EventHandler<ActionEvent> editFilterAction)

    EventHandler<ActionEvent> getSaveAllAction()

    void setSaveAllAction(EventHandler<ActionEvent> saveAllAction)

    void removeListItems()

    List getAllItems()

    void addToListItems(List items)

    void addToListItems(String items)

    String getSelectedListItem()

    void removeItem(String item)

    boolean getIsCritical()

    void setIsCritical(boolean isCritical)

    boolean getIsHigh()

    void setIsHigh(boolean isHigh)

    boolean getIsMedium()

    void setIsMedium(boolean isMedium)

    boolean getIsLow()

    void setIsLow(boolean isLow)

    boolean getIsInfo()

    void setIsInfo(boolean isInfo)

    String getFilterText()

    void setFilterText(String filterText)

    String getBatchSize()

    void setBatchSize(String batchSize)

    String getMaxRows()

    void setMaxRows(String maxRows)

    String getCopyString()

    void setCopyString(String copyString)
}