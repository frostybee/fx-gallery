/*
 * Copyright (C) 2020 Sleiman R.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bee.fxgallery.utils;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 *
 *  
 */
public class AppUtils {

    public static String APP_ICON = "/icons/bee.png";
    public static String APP_TITLE = "Bee's JFX Image Gallery" ;
    public static String APP_STYLE_SHEETS = "/css/styles.css";
    public static String APP_FXML_PATH = "/fxml/" ;
    

    public static Label makeLabel(String labelText, Font labelFont) {
        Label customLabel = new Label(labelText);
        customLabel.setFont(labelFont);
        return customLabel;
    }

}
