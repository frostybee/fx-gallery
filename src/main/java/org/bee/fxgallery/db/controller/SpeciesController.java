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
import org.bee.fxgallery.db.helper.ConnectionProvider;
import org.bee.fxgallery.db.model.Species;

/**
 *
 * @author Sleiman Rabah
 */
public class SpeciesController {

    private final String databaseName = "species.db";
    private final String SPECIES_TABLE = "species";
    //-- Table columns
    private final String COL_SCIENTIFIC_NAME = "scientific_name";
    private final String COL_COMMON_NAME = "common_name";
    private final String COL_IMAGE = "image";
    private final String COL_FAMILY = "family"; 
    private final String COL_ID  = "id"; 

    //--
    public SpeciesController() {
    }

    public void addNewBird(Species newBird) {
        String insertQuery = String.format("INSERT INTO %s (%s,%s,%s,%s) VALUES(?,?,?,?) ", SPECIES_TABLE, COL_SCIENTIFIC_NAME, COL_COMMON_NAME, COL_IMAGE, COL_FAMILY);
        try ( Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);  PreparedStatement pstmt = dbConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            //-- 1) Set parameters and insert the new item.
            pstmt.setString(1, newBird.getScientificName());
            pstmt.setString(2, newBird.getCommonName());
            pstmt.setBytes(3, newBird.getImageBytes());
            pstmt.setString(4, newBird.getFamily());
            pstmt.executeUpdate();
            System.out.println("Stored the file in the BLOB column.");
            //-- 2) Retrieve the lastly generate ID.
            ResultSet resultSet = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (resultSet.next()) {
                generatedKey = resultSet.getInt(1);
                newBird.setId(generatedKey);
            }
            System.out.println("Inserted record's ID: " + generatedKey);
        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + insertQuery);
            System.err.println("Error message: " + ex);
        }

    }

    public List<Species> getBirds() throws SQLException, IOException {
        List<Species> birds = new ArrayList<>();
        String query = "";
        //-- Step 1 & 2: Open a connection to the specified database 
        //--         and create a prepared statement for executing SQL queries..  
        try ( Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);  Statement stmt = dbConnection.createStatement()) {
            query = String.format("SELECT *  FROM %s ", SPECIES_TABLE);
            ResultSet rSet = stmt.executeQuery(query);
            while (rSet.next()) {
                String scientificName = rSet.getString(COL_SCIENTIFIC_NAME);
                int birdId = rSet.getInt(COL_ID);
                String birdName = rSet.getString(COL_COMMON_NAME);
                String category = rSet.getString(COL_FAMILY);
                InputStream imageStream = rSet.getBinaryStream(COL_IMAGE);
                byte[] bytes = imageStream.readAllBytes();
                Species bird = new Species(birdId, scientificName, birdName, bytes, category);
                birds.add(bird);
            }

        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + query);
            System.err.println("Error message: " + ex);
        }
        return birds;
    }

    public void deleteItem(Species inBird) {
        String insertQuery = String.format("DELETE FROM %s WHERE id = ? ", SPECIES_TABLE);
        try ( Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);  PreparedStatement pstmt = dbConnection.prepareStatement(insertQuery)) {
            // set parameters
            pstmt.setInt(1, inBird.getId());
            pstmt.executeUpdate();
            System.out.println("The selected item has been removed from there DB...");
        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + insertQuery);
            System.err.println("Error message: " + ex);
        }
    }
}
