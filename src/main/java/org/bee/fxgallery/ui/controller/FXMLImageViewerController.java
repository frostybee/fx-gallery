/*
 * Copyright (C) 2020 Sleiman R.
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
package org.bee.fxgallery.ui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bee.fxgallery.db.model.Species;

/**
 * FXML Controller class
 *
 *
 */
public class FXMLImageViewerController implements Initializable {

    @FXML
    private JFXButton btnReset;
    @FXML
    private JFXButton btnFullView;
    @FXML
    private JFXSlider zoomSlider;
    @FXML
    private ImageView imgGraphic;
    @FXML
    private Pane pnImageContainer;
    @FXML
    private Label lblFamilyName;
    @FXML
    private Label lblCommonName;
    @FXML
    private Label lblScientificName;
    private Stage parentStage;
    private Species mSpecies;
    private static final int MIN_PIXELS = 10;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        pnImageContainer.setPrefSize(800, 600);
        imgGraphic.fitWidthProperty().bind(pnImageContainer.widthProperty());
        imgGraphic.fitHeightProperty().bind(pnImageContainer.heightProperty());
        VBox.setVgrow(pnImageContainer, Priority.ALWAYS);
    }

    public void loadImage() {
        try {
            //-- Load the info about the selected species.
            lblCommonName.setText(mSpecies.getCommonName());
            lblFamilyName.setText(mSpecies.getFamily());
            lblScientificName.setText(mSpecies.getScientificName());            
            //-- Create and set an image view.
            InputStream myInputStream = new ByteArrayInputStream(mSpecies.getImageBytes());
            final Image fullImage = new Image(myInputStream);
            double width = fullImage.getWidth();
            double height = fullImage.getHeight();
            imgGraphic.setImage(fullImage);
            imgGraphic.setStyle("-fx-background-color: BLACK");
            imgGraphic.setPreserveRatio(true);
            imgGraphic.setSmooth(true);
            imgGraphic.setCache(true);
            imgGraphic.setPreserveRatio(true);
            //
            configureMouseEvents(width, height);
            //-- Initiliaze the sliding event.
            zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                        Number oldValue, Number newValue) {
                    System.out.println("Slider value:"+ newValue);
                    zoomImage(newValue);
                }
            });
            reset(imgGraphic, width, height);
        } catch (Exception ex) {
            Logger.getLogger(FXMLImageViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void configureMouseEvents(double width, double height) {
        reset(imgGraphic, width / 2, height / 2);
        btnReset.setOnAction(e -> reset(imgGraphic, width / 2, height / 2));
        btnFullView.setOnAction(e -> reset(imgGraphic, width, height));
        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        imgGraphic.setOnMouseReleased(e -> {
            imgGraphic.setCursor(Cursor.OPEN_HAND);
        });
        imgGraphic.setOnMousePressed(e -> {
            Point2D mousePress = imageViewToImage(imgGraphic, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
            imgGraphic.setCursor(Cursor.CLOSED_HAND);
        });

        imgGraphic.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(imgGraphic, new Point2D(e.getX(), e.getY()));
            shift(imgGraphic, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(imgGraphic, new Point2D(e.getX(), e.getY())));
        });

        imgGraphic.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = imgGraphic.getViewport();

            double scale = clamp(Math.pow(1.01, delta),
                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                    // don't scale so that we're bigger than image dimensions:
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())
            );

            Point2D mouse = imageViewToImage(imgGraphic, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image
            // solving this for newViewportMinX gives
            // newViewportMinX = x - (x - currentViewportMinX) * scale 
            // we then clamp this value so the image never scrolls out
            // of the imageview:
            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0, height - newHeight);

            imgGraphic.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        imgGraphic.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(imgGraphic, width, height);
            }
        });
    }

    private void zoomImage(Number newValue) {
        Rectangle2D viewport = imgGraphic.getViewport();

        double scale = clamp(Math.pow(1.01, Double.parseDouble("" + newValue)),
                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                // don't scale so that we're bigger than image dimensions:
                Math.max(imgGraphic.getImage().getWidth() / viewport.getWidth(), imgGraphic.getImage().getHeight() / viewport.getHeight())
        );

    }

    private double clamp(double value, double min, double max) {

        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    public void setMainStage(Stage inParentStage) {
        this.parentStage = inParentStage;
    }

    public void setSpecies(Species inSpecies) {
        this.mSpecies = inSpecies;
    }

}
