package org.bee.fxgallery.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bee.fxgallery.db.model.Species;
import org.bee.fxgallery.utils.AppUtils;

/**
 *
 * @author Sleiman Rabah
 */
public class ManageSpeciesDialog extends Stage implements EventHandler<ActionEvent> {

    public enum SpeciesDlgViewMode {
        ADD, UPDATE
    }
    private Button btnSubmit;
    private TextField txtScientificName;
    private TextField txtCommonName;
    private TextField txtFamily;
    private final Stage parentStage;
    private byte[] bytes;
    private Species mSpecies;

    private File mSelectedFile;
    private Label lblErrorMsg;
    private SpeciesDlgViewMode mViewMode;

    public ManageSpeciesDialog(Stage owner,Species inSpecies, SpeciesDlgViewMode iViewMode) {
        this.parentStage = owner;
        mSelectedFile = null;
        this.mViewMode = iViewMode;
        if (inSpecies != null) {
            mSpecies = inSpecies;
        }
        initComponents();
    }

    private void initComponents() {
        this.initOwner(this.parentStage);
        this.initModality(Modality.APPLICATION_MODAL);
        //-- Create the input form.        
        GridPane root = makeInputForm();
        // If updating a species, load its info into the form controls.
        if (mViewMode == SpeciesDlgViewMode.UPDATE){
            txtCommonName.setText(mSpecies.getCommonName());
            txtFamily.setText(mSpecies.getFamily());
            txtScientificName.setText(mSpecies.getScientificName());
        }
        //--
        Scene dialogScene = new Scene(root, 500, 400);
        dialogScene.getStylesheets().add(getClass().getResource(AppUtils.APP_STYLE_SHEETS).toExternalForm());
        this.setScene(dialogScene);
        this.setTitle("Bee's Gallery - Add New Image");
        this.setScene(dialogScene);
    }

    @Override
    public void handle(ActionEvent event) {
        //-- Handle the submitt event.
        if (event.getSource() == btnSubmit) {
            this.close();
        }
    }

    public String getFirstname() {
        return txtScientificName.getText();
    }

    private GridPane makeInputForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Font labelFont = Font.font("Tahoma", FontWeight.SEMI_BOLD, 15);
        //-- Label for error messages.
        lblErrorMsg = AppUtils.makeLabel("ee", labelFont);
        lblErrorMsg.setStyle("-fx-text-fill: red;");
        lblErrorMsg.setVisible(false);
        grid.add(lblErrorMsg, 0, 0, 2, 1);
        //-- Form title.
        Text scenetitle = new Text("Enter information about the new species:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 1, 3, 1);
        //-- Scientific name.
        grid.add(AppUtils.makeLabel("Scientific Name:", labelFont), 0, 2);
        txtScientificName = new TextField();
        grid.add(txtScientificName, 1, 2);
        //-- Common name.      
        grid.add(AppUtils.makeLabel("Common name:", labelFont), 0, 3);
        txtCommonName = new TextField();
        grid.add(txtCommonName, 1, 3);
        //-- Species's family.        
        grid.add(AppUtils.makeLabel("Family:", labelFont), 0, 4);
        txtFamily = new TextField();
        grid.add(txtFamily, 1, 4);
        //-- Species's image.
        grid.add(AppUtils.makeLabel("Select an image:", labelFont), 0, 5);
        Button btnBrowse = new Button("browse...");
        btnBrowse.setOnAction(this::browseImage);
        grid.add(btnBrowse, 1, 5);
        //Horizontal separator
        Separator hSeparator = new Separator();
        hSeparator.setMaxWidth(this.getMaxWidth());
        grid.add(hSeparator, 0, 6, 3, 1);
        //--Save button.
        Button btnSave = new Button("Save");
        btnSave.setOnAction(this::submitForm);
        //-- Cancel button.
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> this.close());
        //-- Wrap the control buttons.
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnSave, btnCancel);
        grid.add(hbBtn, 1, 8);
        return grid;
    }

    private void browseImage(Event e) {
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
                //TODO: update placehold with the selected path.
                // You selected:
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ManageSpeciesDialog.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ManageSpeciesDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void submitForm(Event e) {
        //TODO: !! Validate the dialog.
        String commonName = txtCommonName.getText().trim();
        String scientificName = txtScientificName.getText().trim();
        String family = txtFamily.getText().trim();
        if (!commonName.isEmpty() && !scientificName.isEmpty() && !family.isEmpty()
                && mSelectedFile != null) {
            // If valid, create a new species object.            
            mSpecies = new Species(scientificName, commonName, bytes, family);
            // Finally, submit the form.
            close();
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
}
