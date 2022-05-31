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
package org.bee.fxgallery.ui;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyphLoader;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.bee.fxgallery.ui.controller.FXMLGalleryController;
import org.bee.fxgallery.utils.AppUtils;

/**
 *
 * Creates and manages the main application window. 
 */
public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(AppUtils.APP_FXML_PATH + "main_scene.fxml"));
        BorderPane root = (BorderPane) loader.load();
        FXMLGalleryController controller = (FXMLGalleryController) loader.getController();
        controller.setMainStage(stage);
        root.setStyle("-fx-background-color: white;");

        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(MainWindow.class.getResourceAsStream("/fonts/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();

        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON))));

        stage.setTitle("JFoenix Demo");

        double width = 800;
        double height = 600;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 2.5;
            height = bounds.getHeight() / 1.35;
        } catch (Exception e) {
        }
        Scene scene = new Scene(decorator, width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm(),
                getClass().getResource(AppUtils.APP_STYLE_SHEETS).toExternalForm()
        );

        stage.setTitle(AppUtils.APP_TITLE);
        root.prefHeightProperty().bind(scene.heightProperty());
        root.prefWidthProperty().bind(scene.widthProperty());
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.getIcons().add(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON)));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
    }

}
