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
package org.bee.fxgallery.ui.controller;

/**
 *
 * @author Sleiman Rabah
 */
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.bee.fxgallery.db.controller.BirdsController;
import org.bee.fxgallery.db.model.Bird;

public class FXMLGalleryController implements Initializable {

    @FXML
    private TilePane imagesPane;
    @FXML
    private ScrollPane imgScrollPane;
    
    @FXML
    private VBox vBox;
    @FXML
    private MenuItem menuClose;
    @FXML
    private MenuItem menuAbout;

    @FXML
    private Button btnAddImage;

    private Stage mainStage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set events on corresponding controls.
        btnAddImage.setOnAction(this::handleButtonAction);
        menuClose.setOnAction(this::handleMenuBarMenuAction);
        menuAbout.setOnAction(this::handleMenuBarMenuAction);

        loadImages();

    }

    private void loadImages() {
        BirdsController birdsController = new BirdsController();
        String path = "images/";
        
        imgScrollPane.setPrefSize(1000, 900);
        
        imgScrollPane.setStyle("-fx-background-color: DAE6F3;");
        imagesPane.setPadding(new Insets(15, 15, 15, 15));
        imagesPane.setHgap(15);
        try {
            //-- Read the list of birds along with their images stored in the DB.
            List<Bird> birds = birdsController.getBirds();
            for (Bird bird : birds) {
                File file = null;
                file = new File(path + bird.getScientificName());
                try {
                    FileUtils.copyInputStreamToFile(bird.getImageInStream(), file);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ImageView imageView;
                imageView = createImageView(file);
                imagesPane.getChildren().addAll(imageView);
            }
            imgScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
            imgScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
            imgScrollPane.setFitToWidth(true);
//            root.setContent(imagesPane);
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleButtonAction(Event e) {
        if (e.getSource() == btnAddImage) {
            System.out.println("Adding a new image.");
            FontAwesomeIconView fontAwesomeIconView
                    = new FontAwesomeIconView(FontAwesomeIcon.ANDROID);
            vBox.getChildren().add(fontAwesomeIconView);

        }
    }

    @FXML
    private void handleMenuBarMenuAction(Event e) {
        if (e.getSource() == menuClose) {
            if (mainStage != null) {
                mainStage.close();
            }
        } else if (e.getSource() == menuAbout) {
            // Open about dialog
        }
    }

    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);

            imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                        if (mouseEvent.getClickCount() == 2) {
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(new FileInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(mainStage.getHeight() - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(mainStage.getWidth());
                                newStage.setHeight(mainStage.getHeight());
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane, Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return imageView;
    }

    /**
     * Sets the main stage of the application.
     *
     * @param mainStage the main stage of the application
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

}
