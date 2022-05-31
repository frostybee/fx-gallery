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
 * A class that implements CRUD operations performed on species.
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
    private final String COL_ID = "id";

    //--
    public SpeciesController() {
    }

    /**
     * Inserts a new species into the attached database. 
     * @param newSpecies the species to be added. 
     */
    public void addNewSpecies(Species newSpecies) {
        String insertQuery = String.format("INSERT INTO %s (%s,%s,%s,%s) VALUES(?,?,?,?) ", SPECIES_TABLE, COL_SCIENTIFIC_NAME, COL_COMMON_NAME, COL_IMAGE, COL_FAMILY);
        try ( Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);  PreparedStatement pstmt = dbConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            //-- 1) Set parameters and insert the new item.
            pstmt.setString(1, newSpecies.getScientificName());
            pstmt.setString(2, newSpecies.getCommonName());
            pstmt.setBytes(3, newSpecies.getImageBytes());
            pstmt.setString(4, newSpecies.getFamily());
            pstmt.executeUpdate();
            System.out.println("Stored the file in the BLOB column.");
            //-- 2) Retrieve the lastly generate ID.
            ResultSet resultSet = pstmt.getGeneratedKeys();
            int generatedKey = 0;
            if (resultSet.next()) {
                generatedKey = resultSet.getInt(1);
                newSpecies.setId(generatedKey);
            }
            System.out.println("Inserted record's ID: " + generatedKey);
        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + insertQuery);
            System.err.println("Error message: " + ex);
        }

    }

    /**
     * Retrieves the list of species from the attached database.
     * @return a list containing all the species.
     * @throws SQLException
     * @throws IOException 
     */
    public List<Species> getSpecies() throws SQLException, IOException {
        List<Species> species = new ArrayList<>();
        String query = "";
        //-- Step 1 & 2: Open a connection to the specified database 
        //--         and create a prepared statement for executing SQL queries..  
        try ( Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);  Statement stmt = dbConnection.createStatement()) {
            query = String.format("SELECT *  FROM %s ", SPECIES_TABLE);
            ResultSet rSet = stmt.executeQuery(query);
            while (rSet.next()) {
                String scientificName = rSet.getString(COL_SCIENTIFIC_NAME);
                int speciesId = rSet.getInt(COL_ID);
                String commonName = rSet.getString(COL_COMMON_NAME);
                String family = rSet.getString(COL_FAMILY);
                InputStream imageStream = rSet.getBinaryStream(COL_IMAGE);
                byte[] bytes = imageStream.readAllBytes();
                Species newSpecies = new Species(speciesId, scientificName, commonName, bytes, family);
                species.add(newSpecies);
            }

        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + query);
            System.err.println("Error message: " + ex);
        }
        return species;
    }

    /**
     * Removes the specified species from the database. 
     * @param inSpecies 
     */
    public void deleteSpecies(Species inSpecies) {
        String insertQuery = String.format("DELETE FROM %s WHERE id = ? ", SPECIES_TABLE);
        try ( Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);  PreparedStatement pstmt = dbConnection.prepareStatement(insertQuery)) {
            // set parameters
            pstmt.setInt(1, inSpecies.getId());
            pstmt.executeUpdate();
            System.out.println("The selected item has been removed from there DB...");
        } catch (SQLException ex) {
            System.err.println("An error has occured while trying to execute query: " + insertQuery);
            System.err.println("Error message: " + ex);
        }
    }

    /**
     * Update information about the specified species.
     * @param inSpecies the species containing the updated information.
     */
    public void updateSpecies(Species inSpecies) {
        String insertQuery = String.format("UPDATE %s SET %s =?,%s=?,%s=?,%s=? WHERE %s= ? ", SPECIES_TABLE, COL_SCIENTIFIC_NAME, COL_COMMON_NAME, COL_IMAGE, COL_FAMILY, COL_ID);
        try ( Connection dbConnection = ConnectionProvider.getInstance().getConnection(databaseName);  PreparedStatement pstmt = dbConnection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            //-- 1) Set parameters and insert the new item.
            pstmt.setString(1, inSpecies.getScientificName());
            pstmt.setString(2, inSpecies.getCommonName());
            pstmt.setBytes(3, inSpecies.getImageBytes());
            pstmt.setString(4, inSpecies.getFamily());
            pstmt.setInt(5, inSpecies.getId());
            pstmt.executeUpdate();
            System.out.println("Updated entry's ID: " + inSpecies.getId());
        } catch (SQLException ex) {
            
            java.lang.System.err.println("An error has occured while trying to execute query: " + insertQuery);
            
            System.err.println("Error message: " + ex);
        }
    }
}
