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
import org.bee.fxgallery.db.model.Bird;

/**
 *
 * @author Sleiman Rabah
 */
public class CustomDialog extends Stage implements EventHandler<ActionEvent> {

    private Button btnSubmit;
    private TextField txtScientificName;
    private TextField txtCommonName;
    private final Stage parentStage;
    private byte[] bytes;
    private Bird species;

    public CustomDialog(Stage owner) {
        this.parentStage = owner;
        initComponents();
    }

    private void initComponents() {
        this.initOwner(this.parentStage);
        this.initModality(Modality.APPLICATION_MODAL);
        //-- Create the input form.        
        GridPane root = makeInputForm();
        Scene mainScene = new Scene(root, 500, 400);
        this.setScene(mainScene);
        this.setTitle("Bee's Gallery - Add New Image");
        this.setScene(mainScene);
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
        Text scenetitle = new Text("Enter information about the new species:");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        Font labelFont = Font.font("Tahoma", FontWeight.SEMI_BOLD, 15);
        //-- Scientific name
        Label lblSciName = new Label("Scientific Name:");
        lblSciName.setFont(labelFont);
        grid.add(lblSciName, 0, 1);
        txtScientificName = new TextField();
        grid.add(txtScientificName, 1, 1);
        //-- Common name
        Label lblCommonName = new Label("Common name:");
        lblCommonName.setFont(labelFont);
        grid.add(lblCommonName, 0, 2);
        txtCommonName = new TextField();
        grid.add(txtCommonName, 1, 2);
        //-- Species's image
        Label lblSelectFile = new Label("Select an image:");
        lblSelectFile.setFont(labelFont);
        grid.add(lblSelectFile, 0, 4);
        Button btnBrowse = new Button("browse...");
        btnBrowse.setOnAction(this::browseImage);
        grid.add(btnBrowse, 1, 4);
        //--Save button
        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> {
            closeDialog();
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnSave);
        grid.add(hbBtn, 1, 5);
        return grid;
    }

    private void browseImage(Event e) {
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Select an Image to Add");
        imageChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = imageChooser.showOpenDialog(this.parentStage);
        if (selectedFile != null) {
            InputStream imageInStream = null;
            try {
                imageInStream = (InputStream) new FileInputStream(selectedFile);
                this.bytes = imageInStream.readAllBytes();
                System.out.println("You selected: " + selectedFile.getAbsolutePath());
                //TODO: update placehold with the selected path.
                // You selected:
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CustomDialog.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CustomDialog.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    imageInStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(CustomDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void closeDialog() {
        // Validate the dialog.
        // If valid, create a new species object.
        species = new Bird();
        species.setBirdName(txtCommonName.getText().trim());
        species.setScientificName(txtScientificName.getText().trim());
        species.setImageInStream(bytes);
        // Submit the form.
        close();
    }

    public Bird getSpecies() {
        return species;
    }
}
