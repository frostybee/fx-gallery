/*
 * Copyright (C) 2020 frostybee.
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bee.fxgallery.db.models.Species;
import org.bee.fxgallery.utils.AppUtils;

/**
 *
 *
 */
public class ManageSpeciesDialog extends Stage {

    public enum SpeciesDlgViewMode {
        ADD, UPDATE
    }
    private TextField txtScientificName;
    private TextField txtCommonName;
    private TextField txtFamily;
    private final Stage parentStage;
    private byte[] bytes;
    private Species mSpecies;
    private File mSelectedFile;
    private Label lblErrorMsg;
    private ImageView imgSelected;
    final private SpeciesDlgViewMode mViewMode;
    private boolean isCancled;

    public ManageSpeciesDialog(Stage owner, Species inSpecies, SpeciesDlgViewMode iViewMode) {
        this.parentStage = owner;
        mSelectedFile = null;
        this.mViewMode = iViewMode;
        isCancled = false;
        if (inSpecies != null) {
            mSpecies = inSpecies;
        }
        initComponents();
    }

    private void initComponents() {
        this.initOwner(this.parentStage);
        this.initModality(Modality.APPLICATION_MODAL);
        //getIcons().add(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON)));
        // Make the form.
        Pane root = makeInputForm();
        // If updating a species, load its info into the form controls.
        if (mViewMode == SpeciesDlgViewMode.UPDATE) {
            txtCommonName.setText(mSpecies.getCommonName());
            txtFamily.setText(mSpecies.getFamily());
            txtScientificName.setText(mSpecies.getScientificName());
            setSelectedImage(mSpecies.getImageBytes());
            this.bytes = mSpecies.getImageBytes();
        }
        //--
        //Scene dialogScene = new Scene(root);
        JFXDecorator decorator = new JFXDecorator(this, root);
        decorator.setCustomMaximize(true);
        decorator.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(AppUtils.APP_ICON))));
        Scene dialogScene = new Scene(decorator, 700, 500);
        //Scene dialogScene = new Scene(root, 700, 550);
        dialogScene.getStylesheets().add(getClass().getResource(AppUtils.APP_STYLE_SHEETS).toExternalForm());
        final ObservableList<String> stylesheets = dialogScene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                getClass().getResource("/css/jfoenix-main-demo.css").toExternalForm(),
                getClass().getResource(AppUtils.APP_STYLE_SHEETS).toExternalForm()
        );
        this.setScene(dialogScene);
        if (mViewMode == SpeciesDlgViewMode.ADD) {
            this.setTitle(AppUtils.APP_TITLE + " - Add New Species");
        } else {
            this.setTitle(AppUtils.APP_TITLE + " - Update Existing Species");
        }
        this.setScene(dialogScene);
    }

    public String getFirstname() {
        return txtScientificName.getText();
    }

    private Pane makeInputForm() {
        Pane root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(AppUtils.APP_FXML_PATH + "edit_species_dialog.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ManageSpeciesDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Lookup nodes in the hierarchy.
        Label lblDlgTitle = (Label) root.lookup("#lblDlgTitle");
        lblErrorMsg = (Label) root.lookup("#lblErrorMsg");
        lblErrorMsg.setStyle("-fx-text-fill: red;");
        lblErrorMsg.setVisible(false);
        txtScientificName = (TextField) root.lookup("#txtScientificName");
        txtCommonName = (TextField) root.lookup("#txtCommonName");
        txtFamily = (TextField) root.lookup("#txtFamily");
        imgSelected = (ImageView) root.lookup("#imgSelected");
        Button btnBrowse = (Button) root.lookup("#btnBrowseImage");
        Button btnCancel = (Button) root.lookup("#btnCancel");
        Button btnSubmit = (Button) root.lookup("#btnSave");
        // Set event handlers.
        btnBrowse.setOnAction(this::openFileChooser);
        btnSubmit.setOnAction(this::submitForm);
        btnCancel.setOnAction(e -> {
            isCancled = true;
            this.close();
        });
        // Change the dialog title.
        if (mViewMode == SpeciesDlgViewMode.UPDATE) {
            lblDlgTitle.setText("Update information of the selected species");
        }

        return root;
    }

    private void openFileChooser(Event e) {
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Select an Image to Add");
        imageChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));
        mSelectedFile = imageChooser.showOpenDialog(this.parentStage);
        if (mSelectedFile != null) {
            try ( InputStream imageInStream = (InputStream) new FileInputStream(mSelectedFile)) {
                this.bytes = imageInStream.readAllBytes();
                System.out.println("You selected: " + mSelectedFile.getAbsolutePath());
                // Show the selected image.
                setSelectedImage(bytes);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManageSpeciesDialog.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ManageSpeciesDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void setSelectedImage(byte[] bytes) {
        final Image fullImage = new Image(new ByteArrayInputStream(bytes), 250, 0, true, true);
        imgSelected.setImage(fullImage);
        imgSelected.setFitWidth(100);
        imgSelected.setFitHeight(100);
    }

    private void submitForm(Event e) {        
        String commonName = txtCommonName.getText().trim();
        String scientificName = txtScientificName.getText().trim();
        String family = txtFamily.getText().trim();
        if (!commonName.isEmpty() && !scientificName.isEmpty() && !family.isEmpty()
                && this.bytes.length >= 0) {
            // If valid, create a new species object.                        
            if (mViewMode == SpeciesDlgViewMode.ADD) {
                mSpecies = new Species(scientificName, commonName, bytes, family);
            } else if (mViewMode == SpeciesDlgViewMode.UPDATE) {
                mSpecies.setCommonName(commonName);
                mSpecies.setFamily(family);
                mSpecies.setScientificName(scientificName);
                mSpecies.setImageBytes(bytes);
            }
            // Finally, submit the form.
            this.close();
        } else {
            //-- Invalid form! Display an error message.            
            String errorMsg = "Error: you must fill out all the fields and select an image!";
            lblErrorMsg.setText(errorMsg);
            lblErrorMsg.setVisible(true);
        }
    }

    public Species getSpecies() {
        return mSpecies;
    }

    public void setSpecies(Species species) {
        this.mSpecies = species;
    }

    public boolean isCancled() {
        return isCancled;
    }

}
