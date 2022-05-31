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
package org.bee.fxgallery.db.helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * A singleton class that enables connection to an SQLite database.
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
