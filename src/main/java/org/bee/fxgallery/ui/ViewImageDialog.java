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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bee.fxgallery.db.model.Species;
import org.bee.fxgallery.ui.controller.FXMLImageViewerController;
import org.bee.fxgallery.utils.AppUtils;

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
        getIcons().add(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON)));

        VBox root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppUtils.APP_FXML_PATH + "image_viewer_dialog.fxml"));
            root = loader.load();
            FXMLImageViewerController controller = (FXMLImageViewerController) loader.getController();
            controller.setMainStage(this);
            controller.setSpecies(mSpecies);
            controller.loadImage();
            root.setFillWidth(true);
            Scene dialogScene = new Scene(root);
            this.setWidth(this.mParentWindow.getWidth() + 100);
            this.setHeight(this.mParentWindow.getHeight() + 100);
            this.setTitle(this.mSpecies.getCommonName());
            this.setScene(dialogScene);
        } catch (IOException ex) {
            Logger.getLogger(ManageSpeciesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
