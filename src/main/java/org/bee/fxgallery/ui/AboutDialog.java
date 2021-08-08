/*
 * Copyright (C) 2021 Sleiman Rabah
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
package org.bee.fxgallery.ui;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bee.fxgallery.utils.AppUtils;

/**
 *
 * @author Sleiman Rabah
 */
public class AboutDialog extends Stage {

    public AboutDialog(Stage owner) {
        this.initOwner(owner);
        this.initModality(Modality.APPLICATION_MODAL);
        makeDialogContent();
    }

    private void makeDialogContent() {
        Pane root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppUtils.APP_FXML_PATH + "about_dialog.fxml"));
            root = loader.load();
            //Scene dialogScene = new Scene(root);
            JFXDecorator decorator = new JFXDecorator(this, root);
            decorator.setCustomMaximize(true);
            decorator.setGraphic( new ImageView(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON))));

            Scene dialogScene = new Scene(decorator, 500, 400);
            Button btnClose = (Button) root.lookup("#btnClose");
            btnClose.setOnAction(e -> {
                this.close();
            });
            //-- Disable textareas
            TextArea taLibraries = (TextArea) root.lookup("#txtLibraries");
            taLibraries.setDisable(true);
            taLibraries.setStyle("-fx-opacity: 1.0;-fx-text-fill: #1d2633; -fx-font-size:12px;");            
            TextArea taAbout = (TextArea) root.lookup("#taAbout");
            taAbout.setDisable(true);
            taAbout.setStyle("-fx-opacity: 1.0;-fx-text-fill: #1d2633; -fx-font-size:12px;");            
            

            final ObservableList<String> stylesheets = dialogScene.getStylesheets();
            stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                    JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                    getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm(),
                    getClass().getResource(AppUtils.APP_STYLE_SHEETS).toExternalForm()
            );
            getIcons().add(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON)));
            this.setWidth(500);
            this.setHeight(500);
            this.setTitle(AppUtils.APP_TITLE + " - About");
            this.setScene(dialogScene);
        } catch (IOException ex) {
            Logger.getLogger(ManageSpeciesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
