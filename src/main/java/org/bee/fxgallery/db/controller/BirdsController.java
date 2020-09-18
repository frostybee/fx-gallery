package org.bee.fxgallery.db.controller;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.bee.fxgallery.db.helper.ConnectionProvider;
import org.bee.fxgallery.db.model.Bird;

/**
 *
 * @author Sleiman Rabah
 */
public class BirdsController {

    private final String databaseName = "bird_info.db";
    private final String BIRDS_TABLE = "birds";

    public BirdsController() {
    }

    public void addNewBird(Bird newBird) {
        String insertQuery = String.format("INSERT INTO %s (scientific_name, name, image, category) VALUES(?,?,?,?) ", BIRDS_TABLE);
        try (Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);
                PreparedStatement pstmt = dbConnection.prepareStatement(insertQuery)) {
            byte[] bytes = null;
            bytes = IOUtils.toByteArray(newBird.getImageInStream());

            // set parameters
            pstmt.setString(1, newBird.getScientificName());
            pstmt.setString(2, newBird.getBirdName());
            pstmt.setBytes(3, bytes);
            pstmt.setString(4, newBird.getCategory());
            pstmt.executeUpdate();
            System.out.println("Stored the file in the BLOB column.");

        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + insertQuery);
            System.err.println("Error message: " + ex);
        } catch (IOException ex) {
            Logger.getLogger(BirdsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Bird> getBirds() throws SQLException {
        List<Bird> birds = new ArrayList<>();
        String query = "";
        //-- Step 1 & 2: Open a connection to the specified database 
        //--         and create a prepared statement for executing SQL queries..  
        try (Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);
                Statement stmt = dbConnection.createStatement()) {
            query = String.format("SELECT *  FROM %s ", BIRDS_TABLE);
            ResultSet rSet = stmt.executeQuery(query);
            while (rSet.next()) {
                String scientificName = rSet.getString("scientific_name");
                int birdId = rSet.getInt("id");
                String birdName = rSet.getString("scientific_name");
                String category = rSet.getString("category");
                InputStream in = rSet.getBinaryStream("image");
                Bird bird = new Bird(birdId, scientificName, birdName, in, category);
                birds.add(bird);
            }

        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + query);
            System.err.println("Error message: " + ex);
        }
        return birds;
    }
}
