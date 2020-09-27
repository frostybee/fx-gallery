/*
 * Copyright (C) 2020 Sleiman Rabah
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bee.fxgallery.db.model.Species;
import org.bee.fxgallery.ui.controller.FXMLGalleryController;

/**
 *
 * @author Sleiman Rabah
 */
public class ViewImageDialog extends Stage {

    private Stage mParentWindow = null;
    private Species mSpecies = null;

    public ViewImageDialog(Stage owner, Species inSpecies) {
        this.mParentWindow = owner;
        this.mSpecies = inSpecies;
        initComponents();
    }

    private void initComponents() {
        this.initOwner(this.mParentWindow);
        this.initModality(Modality.WINDOW_MODAL);
        //-- Create and set an image view.
        try {
            InputStream myInputStream = new ByteArrayInputStream(mSpecies.getImageBytes());
            final Image fullImage = new Image(myInputStream);
            BorderPane borderPane = new BorderPane();
            ImageView imageView = new ImageView();
            imageView.setImage(fullImage);
            imageView.setStyle("-fx-background-color: BLACK");
            imageView.setFitHeight(this.mParentWindow.getHeight() - 10);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            borderPane.setCenter(imageView);
            borderPane.setStyle("-fx-background-color: BLACK");
            //-- Customize this window.            
            this.setWidth(this.mParentWindow.getWidth() + 100);
            this.setHeight(this.mParentWindow.getHeight() + 100);
            this.setTitle(this.mSpecies.getCommonName());
            Scene scene = new Scene(borderPane, Color.BLACK);
            this.getIcons().add(this.mParentWindow.getIcons().get(0));
            this.setScene(scene);            
        } catch (Exception ex) {
            Logger.getLogger(FXMLGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
