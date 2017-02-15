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


package net.vdbaan.issuefinder.view.component

import java.awt.Color

class Colour {
    /*
Colours:
@brand-primary: darken(#428bca, 6.5%); // #337cbb
@brand-success: #5cb85c; // #4da94d
@brand-info:    #5bc0de; // #4cb1cf
@brand-warning: #f0ad4e; // #e19e3f
@brand-danger:  #d9534f; // #ca4440
none            #ffffff; // #f5f5f5
 */
    public static final Color CRITICAL = Color.decode("#d9534f")
    public static final Color CRITICAL_ODD = Color.decode("#ca4440")
    public static final Color HIGH = Color.decode("#f0ad4e")
    public static final Color HIGH_ODD = Color.decode("#e19e3f")
    public static final Color MEDIUM = Color.decode("#5bc0de")
    public static final Color MEDIUM_ODD = Color.decode("#4cb1cf")
    public static final Color LOW = Color.decode("#5cb85c")
    public static final Color LOW_ODD = Color.decode("#4da94d")
    public static final Color INFO = Color.decode("#ffffff")
    public static final Color INFO_ODD = Color.decode("#f5f5f5")
}
