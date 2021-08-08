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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TestZooming extends Application {

    private static final String IMAGE_URL = "https://www.nasa.gov/sites/default/files/thumbnails/image/global-mosaic-of-pluto-in-true-color.jpg";
    static double initx;
    static double inity;
    static int height;
    static int width;
    public static String path;
    static Scene initialScene, view;
    static double offSetX, offSetY, zoomlvl;

    @Override
    public void start(Stage s) {
        s.setResizable(false);       
        initView();
        s.setScene(view);
        s.show();
    }

    public static void initView() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Test");
        Image source = null;

        //source = new Image(new FileInputStream(path));
        source = new Image(IMAGE_URL);

        ImageView imageView = new ImageView(source);
        double ratio = source.getWidth() / source.getHeight();

        if (500 / ratio < 500) {
            width = 500;
            height = (int) (500 / ratio);
        } else if (500 * ratio < 500) {
            height = 500;
            width = (int) (500 * ratio);
        } else {
            height = 500;
            width = 500;
        }
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        height = (int) source.getHeight();
        width = (int) source.getWidth();
        System.out.println("height = " + height + "\nwidth = " + width);
        HBox zoom = new HBox(10);
        zoom.setAlignment(Pos.CENTER);

        Slider zoomLvl = new Slider();
        zoomLvl.setMax(4);
        zoomLvl.setMin(1);
        zoomLvl.setMaxWidth(200);
        zoomLvl.setMinWidth(200);
        Label hint = new Label("Zoom Level");
        Label value = new Label("1.0");

        offSetX = width / 2;
        offSetY = height / 2;

        zoom.getChildren().addAll(hint, zoomLvl, value);

        Slider Hscroll = new Slider();
        Hscroll.setMin(0);
        Hscroll.setMax(width);
        Hscroll.setMaxWidth(imageView.getFitWidth());
        Hscroll.setMinWidth(imageView.getFitWidth());
        Hscroll.setTranslateY(-20);
        Slider Vscroll = new Slider();
        Vscroll.setMin(0);
        Vscroll.setMax(height);
        Vscroll.setMaxHeight(imageView.getFitHeight());
        Vscroll.setMinHeight(imageView.getFitHeight());
        Vscroll.setOrientation(Orientation.VERTICAL);
        Vscroll.setTranslateX(-20);

        BorderPane borderPane = new BorderPane();
        BorderPane.setAlignment(Hscroll, Pos.CENTER);
        BorderPane.setAlignment(Vscroll, Pos.CENTER_LEFT);
        Hscroll.valueProperty().addListener(e -> {
            offSetX = Hscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            value.setText(newValue + "");
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }

            imageView.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        Vscroll.valueProperty().addListener(e -> {
            offSetY = height - Vscroll.getValue();
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            value.setText(newValue + "");
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }
            imageView.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        borderPane.setCenter(imageView);
        borderPane.setTop(Hscroll);
        borderPane.setRight(Vscroll);
        zoomLvl.valueProperty().addListener(e -> {
            zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            value.setText(newValue + "");
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }
            Hscroll.setValue(offSetX);
            Vscroll.setValue(height - offSetY);
            imageView.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        borderPane.setCursor(Cursor.OPEN_HAND);
        imageView.setOnMousePressed(e -> {
            initx = e.getSceneX();
            inity = e.getSceneY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });
        imageView.setOnMouseReleased(e -> {
            borderPane.setCursor(Cursor.OPEN_HAND);
        });
        imageView.setOnMouseDragged(e -> {
            Hscroll.setValue(Hscroll.getValue() + (initx - e.getSceneX()));
            Vscroll.setValue(Vscroll.getValue() - (inity - e.getSceneY()));
            initx = e.getSceneX();
            inity = e.getSceneY();
        });
        
        imageView.setOnScroll(e -> {
            System.out.println("Test on scroll done");
            
            //  zoomlvl = zoomLvl.getValue();
            double newValue = (double) ((int) (zoomlvl * 10)) / 10;
            value.setText(newValue + "");
            if (offSetX < (width / newValue) / 2) {
                offSetX = (width / newValue) / 2;
            }
            if (offSetX > width - ((width / newValue) / 2)) {
                offSetX = width - ((width / newValue) / 2);
            }
            if (offSetY < (height / newValue) / 2) {
                offSetY = (height / newValue) / 2;
            }
            if (offSetY > height - ((height / newValue) / 2)) {
                offSetY = height - ((height / newValue) / 2);
            }
            Hscroll.setValue(offSetX);
            Vscroll.setValue(height - offSetY);
            imageView.setViewport(new Rectangle2D(offSetX - ((width / newValue) / 2), offSetY - ((height / newValue) / 2), width / newValue, height / newValue));
        });
        root.getChildren().addAll(title, borderPane, zoom);

        view = new Scene(root, (imageView.getFitWidth()) + 70, (imageView.getFitHeight()) + 150);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
