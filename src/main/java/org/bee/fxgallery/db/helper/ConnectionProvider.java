package org.bee.fxgallery.db.helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Sleiman Rabah
 */
public class ConnectionProvider {

    private static final String DB_FOLDER = "data";
    private Connection connection;
    private static ConnectionProvider instance;

    private ConnectionProvider() {

    }

    /**
     * Opens a connection to an SQLite database.
     *
     * @param databaseName the name of the SQLite database file (it can also be
     * a relative path.
     * @return a connection to an SQLite database.
     */
    public Connection getConnection(String databaseName) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:" + DB_FOLDER + "/" + databaseName);
            return dbConnection;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static ConnectionProvider getInstance() {
        if (instance == null) {
            instance = new ConnectionProvider();
        }
        return instance;
    }
}
