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
package org.bee.fxgallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bee.fxgallery.ui.controller.FXMLGalleryController;
import org.bee.fxgallery.utils.AppConstants;
import org.bee.fxgallery.utils.AppUtils;

/**
 *
 * @see: https://en.wikipedia.org/wiki/Bumblebee
 * @author Sleiman Rabah
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(AppUtils.APP_FXML_PATH + "main.fxml"));
        Parent root = loader.load();
        FXMLGalleryController controller = (FXMLGalleryController) loader.getController();
        controller.setMainStage(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(AppUtils.APP_STYLE_SHEETS).toExternalForm());
        stage.setTitle(AppConstants.APP_TITLE);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.getIcons().add(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON)));

    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void stop() throws Exception {
        Platform.exit();
    }

}
