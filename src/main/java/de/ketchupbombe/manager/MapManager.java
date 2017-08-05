package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import de.ketchupbombe.MySQL.MySQL;
import de.ketchupbombe.enums.MySQLTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class MapManager {

    private String currentMap = null;

    private ArrayList<String> onlineMapCache = new ArrayList<>();
    private ArrayList<String> allMapCache = new ArrayList<>();

    /**
     * Set the current mapname
     *
     * @param currentMap which map is the current map
     */
    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    /**
     * Get current online map
     *
     * @return current online map
     */
    public String getCurrentMap() {

        return currentMap;
    }

    /**
     * Insert a new map in database
     *
     * @param mapname name of the map
     * @param author  author of the map
     * @param online  true/false - should the map be in mapchange
     */
    public void createNewMap(String mapname, String author, boolean online) {
        if (!isMapExist(mapname)) {
            MySQL.updateAsync("INSERT INTO " + MySQLTable.MAPS.getTablename() + "(mapname,author,online) VALUES ('" + mapname + "','" + author + "','" + String.valueOf(online) + "')");

        }
    }

    /**
     * Check if specified mapname is in map cache
     *
     * @param mapname mapname to check
     * @return true if map exist
     */
    public boolean isMapExist(String mapname) {
        return getAllMapCache().contains(mapname);
    }

    /**
     * All maps withe 'true' in column 'online'
     *
     * @return List of online maps
     */
    private ArrayList<String> getOnlineMaps() {
        ArrayList<String> onlineMaps = new ArrayList<>();
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.MAPS.getTablename());
        try {
            while (rs.next()) {
                if (rs.getBoolean("online")) {
                    onlineMaps.add(rs.getString("mapname"));
                }
            }
        } catch (SQLException e) {
        }

        return onlineMaps;
    }

    /**
     * Get author of a map
     *
     * @param mapname From which map to get the author
     * @return name of the creator
     */
    public String getAuthor(String mapname) {
        String[] author = null;
        if (isMapExist(mapname)) {
            FFA.getInstance().getExecutor().execute(() -> {
                ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.MAPS.getTablename() + " WHERE mapname='" + mapname + "'");
                try {
                    if (rs.next())
                        author[0] = rs.getString("author");
                } catch (SQLException e) {
                }

            });
        } else {
            return "This map does not exist!";
        }
        return author[0];
    }

    /**
     * Get all mapnames which are registered in database
     *
     * @return List of all mapnames
     */
    private ArrayList<String> getAllMaps() {
        ArrayList<String> onlineMaps = new ArrayList<>();
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.MAPS.getTablename());
        try {
            while (rs.next()) {
                onlineMaps.add(rs.getString("mapname"));
            }
        } catch (SQLException e) {
        }

        return onlineMaps;
    }

    /**
     * Update the cache in which the maps are stored
     */
    public void updateMapCache() {
        onlineMapCache.clear();
        allMapCache.clear();
        for (String s : getOnlineMaps()) {
            onlineMapCache.add(s);
        }
        for (String s : getAllMaps()) {
            allMapCache.add(s);
        }
    }

    /**
     * Get all online maps which are stored in cache
     *
     * @return list of online maps
     */
    public ArrayList<String> getOnlineMapCache() {
        return onlineMapCache;
    }

    /**
     * Get all maps which are stored in cache
     *
     * @return list of all maps which are registered in database
     */
    public ArrayList<String> getAllMapCache() {
        return allMapCache;
    }

    /**
     * Get a random map of all online maps which are stored in cache
     *
     * @return random map of all online maps
     */
    public String getRandomMap() {

        return getOnlineMapCache().get(new Random().nextInt(getOnlineMapCache().size()));
    }

    /**
     * Delete a map from database
     *
     * @param mapname name of mapname which should be deleted
     */
    public void deleteMap(String mapname) {
        if (isMapExist(mapname)) {
            MySQL.updateAsync("DELETE FROM " + MySQLTable.MAPS.getTablename() + " WHERE mapname='" + mapname + "'");
        }
    }

    /**
     * Switch between online and offline mode from a map
     *
     * @param mapname which map should be online/offline
     * @param online  boolean to switch online-mode
     */
    public void updateOnline(String mapname, boolean online) {
        MySQL.updateAsync("UPDATE " + MySQLTable.MAPS.getTablename() + " SET online='" + String.valueOf(online) + "' WHERE mapname='" + mapname + "'");
    }

    /**
     * Check if map is in online map cache
     *
     * @param mapname which map should be checked
     * @return boolean true if map is onkine
     */
    public boolean isMapOnline(String mapname) {
        return getOnlineMapCache().contains(mapname);
    }

}
