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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.bee.fxgallery.db.controller.BirdsController;
import org.bee.fxgallery.db.model.Bird;
import org.bee.fxgallery.ui.CustomDialog;

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

    private BirdsController birdsController;
    private File file = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        birdsController = new BirdsController();
        // Set events on corresponding controls.
        btnAddImage.setOnAction(this::handleButtonActions);
        menuClose.setOnAction(this::handleMenuBarMenuAction);
        menuAbout.setOnAction(this::handleMenuBarMenuAction);

        loadImages();

    }

    private void loadImages() {
        //TODO: fix the scrolling issue.
        imgScrollPane.setPrefSize(1000, 900);
        imgScrollPane.setStyle("-fx-background-color: DAE6F3;");
        imagesPane.setPadding(new Insets(15, 15, 15, 15));
        imagesPane.setHgap(15);
        try {
            //-- Read the list of birds along with their images stored in the DB.
            List<Bird> birds = birdsController.getBirds();
            for (Bird bird : birds) {
                VBox cardBox = createImageCard(bird);
                //-- Add the new card to the main panel.
                imagesPane.getChildren().addAll(cardBox);
            }
            imgScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
            imgScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
            imgScrollPane.setFitToWidth(true);
//            root.setContent(imagesPane);

        } catch (SQLException ex) {
            Logger.getLogger(FXMLGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleButtonActions(Event e) {
        if (e.getSource() == btnAddImage) {
            System.out.println("Adding a new image.");
            //-- Select a new image to add to the library.
            addNewImage();
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

    private void addNewImage() {
        CustomDialog dialog = new CustomDialog(this.mainStage);
        dialog.showAndWait();
        //-- Retreive what the user entered in the dialog box.        
        Bird newBird = dialog.getSpecies();
        if (newBird != null) {
            birdsController.addNewBird(newBird);
            VBox imageCard = createImageCard(newBird);
            imagesPane.getChildren().add(imageCard);
            System.out.println("You entered: " + dialog.getFirstname());
        }
    }

    private void deleteSelectedImage(VBox cardBox, Bird inBird) {
        // TODO: implement delete from the database.
        // And add comfirmation message.        
        System.out.println("Delete bird: " + inBird.getBirdName());

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Comfirm Delete Image");
        alert.setHeaderText(String.format("Are you sure you want to delete: %s?", inBird.getBirdName()));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Go ahead and delete!");
            imagesPane.getChildren().remove(cardBox);
            birdsController.deleteItem(inBird);
        } else {
            System.out.println("User clicked on No!");
        }

    }

    private VBox createImageCard(final Bird inBird) {
        VBox cardBox = new VBox();

        try {
            InputStream myInputStream = new ByteArrayInputStream(inBird.getImageInStream());
            final Image fullImage = new Image(myInputStream, 150, 0, true, true);
            HBox controlButtons = new HBox();
            Button btnViewImage = new Button("View");
            Button btnViewDelete = new Button("Delete");
            btnViewImage.setOnAction(e -> viewImage(inBird));
            ImageView imageView = new ImageView(fullImage);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            btnViewDelete.setOnAction(e -> deleteSelectedImage(cardBox, inBird));
            //-- Setup the card/
            controlButtons.getChildren().addAll(btnViewImage, btnViewDelete);
            cardBox.getChildren().addAll(imageView, controlButtons);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (mouseEvent.getClickCount() == 2) {
                            viewImage(inBird);
                        }
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cardBox;
    }

    private void viewImage(final Bird inBird) {
        try {
            InputStream myInputStream = new ByteArrayInputStream(inBird.getImageInStream());
            final Image fullImage = new Image(myInputStream);
            BorderPane borderPane = new BorderPane();
            ImageView imageView = new ImageView();
            imageView.setImage(fullImage);
            imageView.setStyle("-fx-background-color: BLACK");
            imageView.setFitHeight(mainStage.getHeight() - 10);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            borderPane.setCenter(imageView);
            borderPane.setStyle("-fx-background-color: BLACK");
            Stage newStage = new Stage();
            newStage.setWidth(mainStage.getWidth() + 100);
            newStage.setHeight(mainStage.getHeight() + 100);
            newStage.setTitle(inBird.getBirdName());
            Scene scene = new Scene(borderPane, Color.BLACK);
            newStage.getIcons().add(mainStage.getIcons().get(0));
            newStage.setScene(scene);
            newStage.show();
        } catch (Exception ex) {
            Logger.getLogger(FXMLGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
