package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import de.ketchupbombe.MySQL.MySQL;
import de.ketchupbombe.enums.LocationType;
import de.ketchupbombe.enums.MySQLTable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class LocationManager {
    /**
     * Save a new location in databaes
     *
     * @param type      LocationType (SPAWN/SPECIALBLOCK)
     * @param loc       location to save
     * @param worldname worldname of location to save
     */
    public void saveLocation(LocationType type, Location loc, String worldname) {
        if (type == LocationType.SPAWN) {
            String locString = getLocationStringByLocation(loc);
            if (isSpawnLocation(worldname)) {
                updateLocation(type, loc, worldname);
                return;
            }
            MySQL.updateAsync("INSERT INTO " + MySQLTable.LOCATION.getTablename() + "(type, worldname, location) VALUES ('" + LocationType.SPAWN.toString() + "','" + worldname + "','" + locString + "')");

        } else if (type == LocationType.SPECIALBLOCK) {
            int x = loc.getBlockX();
            int y = loc.getBlockY() - 1;
            int z = loc.getBlockZ();
            Location blockLoc = new Location(Bukkit.getWorld(worldname), x, y, z);
            if (!isAlreadySpecialBlock(blockLoc)) {
                String locString = getSpecialBlockStringByLocation(loc);
                MySQL.updateAsync("INSERT INTO " + MySQLTable.LOCATION.getTablename() + "(type, worldname, location) VALUES ('" + LocationType.SPECIALBLOCK.toString() + "', '" + worldname + "', '" + locString + "')");
            }
        } else {
            return;
        }
    }

    /**
     * Update a already existing Location
     *
     * @param type      LocationType (SPAWN)
     * @param loc       location to save
     * @param worldname worldname of location to save
     */
    public void updateLocation(LocationType type, Location loc, String worldname) {
        if (type == LocationType.SPAWN) {
            String locString = getLocationStringByLocation(loc);

            MySQL.updateAsync("UPDATE " + MySQLTable.LOCATION.getTablename() + " SET location='" + locString + "' WHERE worldname='" + worldname + "' AND type='" + LocationType.SPAWN.toString() + "'");

        } else {
            return;
        }

    }

    /*
    public ArrayList<String> getWordNames() {
        ArrayList<String> worlds = new ArrayList<>();

        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.LOCATION.getTablename());
        try {
            while (rs.next()) {
                worlds.add(rs.getString("worldname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return worlds;
    }
    */

    /**
     * Check if block is already a special block
     *
     * @param loc on which location it should check
     * @return boolean true if exist
     */
    public boolean isAlreadySpecialBlock(Location loc) {
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.LOCATION.getTablename());
        try {
            while (rs.next()) {
                if (rs.getString("type").equals(LocationType.SPECIALBLOCK.toString())) {
                    String locs = rs.getString("location");
                    String world = rs.getString("worldname");
                    Location blockLocation = getSpecialBlockLocationByString(locs, world);
                    if (blockLocation.equals(loc)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
        }
        return false;
    }

    /**
     * Check if a location is a spawnlocation of a world
     *
     * @param worldname world which shold be check
     * @return boolean true if exists
     */
    public boolean isSpawnLocation(String worldname) {
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.LOCATION.getTablename() + " WHERE worldname='" + worldname + "'");
        try {
            if (rs.next()) {
                if (rs.getString("type").equals(LocationType.SPAWN.toString())) return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    /**
     * Get a list of all Location in a world which are a special block
     *
     * @param world World to check
     * @return list of special block locations
     */
    public ArrayList<Location> getSpecialBlockLocations(World world) {
        ArrayList<Location> specialBlockLocations = new ArrayList<>();
        FFA.getInstance().getExecutor().execute(() -> {
            ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.LOCATION.getTablename() + " WHERE type='SPECIALBLOCK'");
            try {
                while (rs.next()) {
                    String[] locationStrings = rs.getString("location").split(":");
                    if (world.getName().equals(rs.getString("worldname"))) {
                        specialBlockLocations.add(new Location(world, Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1]), Integer.parseInt(locationStrings[2])));

                    }
                }
            } catch (SQLException e) {
            }

        });
        return specialBlockLocations;

    }

    /**
     * Get the spawn location which is stored in databes from a specified world
     *
     * @param worldname world to get the location
     * @return stored location in database from specified world
     */
    public Location getSpawnLocationByWorld(String worldname) {
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.LOCATION.getTablename() + " WHERE worldname='" + worldname + "' AND type='" + LocationType.SPAWN + "'");
        try {
            if (rs.next()) {
                return getLocationByString(rs.getString("location"), worldname);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    /**
     * Remove a spawn location from database by worldname
     *
     * @param worldname word to remove location
     */
    public void deleteSpawnLocation(String worldname) {
        MySQL.updateAsync("DELETE FROM " + MySQLTable.LOCATION.getTablename() + " WHERE type='" + LocationType.SPAWN.toString() + "' AND worldname='" + worldname + "'");
    }

    /**
     * Import a world to Bukkit
     * NOTE: this does not import the world in database!
     *
     * @param worldname world to import
     * @return boolean false if folder of world does not exist
     */
    public boolean importWorld(String worldname) {
        if (isFolderExist(worldname)) {
            Bukkit.createWorld(new WorldCreator(worldname));
            return true;
        } else
            return false;
    }

    /**
     * Unload word from Bukkit
     * NOTE: This removes the world from database!
     *
     * @param worldname world to unload
     */
    public void unloadWorld(String worldname) {
        Bukkit.unloadWorld(worldname, true);
        FFA.getInstance().getMapManager().deleteMap(worldname);
        deleteSpawnLocation(worldname);
    }

    /**
     * Check if list of all world which are loaded contains the specified world
     *
     * @param worldname world to check
     * @return boolean true if worldlist contains specified world
     */
    public boolean isWorldLoaded(String worldname) {
        return Bukkit.getWorlds().contains(Bukkit.getWorld(worldname));
    }

    /**
     * Check if specified folder exist
     *
     * @param foldername folder to check
     * @return boolean true if folder exist
     */
    public boolean isFolderExist(String foldername) {
        File file = new File(foldername);
        return file.exists();
    }

    /**
     * Transform block location to String
     *
     * @param loc location to transfrom into String
     * @return String from block location
     */
    private String getSpecialBlockStringByLocation(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY() - 1;
        int z = loc.getBlockZ();

        return x + ":" + y + ":" + z;
    }

    /**
     * Transform block-location-String into location
     *
     * @param blockString String to transform
     * @param worldname   worldname of location
     * @return Location from block-location-String
     */
    private Location getSpecialBlockLocationByString(String blockString, String worldname) {
        String[] locs = blockString.split(":");
        int x = Integer.parseInt(locs[0]);
        int y = Integer.parseInt(locs[1]);
        int z = Integer.parseInt(locs[2]);
        return new Location(Bukkit.getWorld(worldname), x, y, z);
    }

    /**
     * Transform location to String
     *
     * @param loc location to transfrom into String
     * @return String from location
     */
    private String getLocationStringByLocation(Location loc) {
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        return x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }

    /**
     * Transform location-String into location
     *
     * @param locString String to transform
     * @param worldname worldname of location
     * @return Location from location-String
     */
    private Location getLocationByString(String locString, String worldname) {
        String[] locs = locString.split(":");
        double x = Double.parseDouble(locs[0]);
        double y = Double.parseDouble(locs[1]);
        double z = Double.parseDouble(locs[2]);
        float yaw = Float.parseFloat(locs[3]);
        float pitch = Float.parseFloat(locs[4]);

        return new Location(Bukkit.getWorld(worldname), x, y, z, yaw, pitch);
    }

}
