package de.ketchupbombe.MySQL;

import de.ketchupbombe.FFA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class MySQL {

    private static String HOST, DATABASE, USERNAME, PASSWORD;
    private static int PORT;
    private static Connection CON;

    /**
     * Connect to database
     */
    public static void connect() {
        try {
            if (!isConnected()) {
                CON = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoreconnect=true", USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
        }
    }

    /**
     * Close connection
     */
    public static void close() {
        try {
            if (isConnected()) CON.close();
        } catch (SQLException e) {
        }
    }

    /**
     * Execute command synchronous
     *
     * @param qry SQL-command
     */
    public static void updateSync(String qry) {
        try {
            if (isConnected()) {
                getConnection().prepareStatement(qry).executeUpdate();
            }
        } catch (SQLException e) {
        }
    }

    /**
     * Execute command asynchronous
     *
     * @param qry SQL-command
     * @see MySQL#updateSync(String)
     */
    public static void updateAsync(String qry) {
        FFA.getInstance().getExecutor().execute(() -> {
            updateSync(qry);
        });
    }

    /**
     * Get a result.
     * Return null if there is no result
     *
     * @param qry SQL-command
     * @return Result of SQL-command
     */
    public static ResultSet getResult(String qry) {
        try {
            return CON.createStatement().executeQuery(qry);
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     * Setup the MySQL data
     */
    public static void setUp() {
        HOST = "localhost";
        DATABASE = "FFA";
        USERNAME = "admin";
        PASSWORD = "lukas";
        PORT = 3306;

    }

    /**
     * @return boolean is connected
     */
    public static boolean isConnected() {
        return CON != null;
    }

    /**
     * @return MySQL Connection
     */
    public static Connection getConnection() {
        return CON;
    }
}
