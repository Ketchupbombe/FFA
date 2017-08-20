package de.ketchupbombe;

import de.ketchupbombe.MySQL.MySQL;
import de.ketchupbombe.commands.FFAReloadCommand;
import de.ketchupbombe.commands.ImportWorldCommand;
import de.ketchupbombe.commands.MapsCommand;
import de.ketchupbombe.enums.LocationType;
import de.ketchupbombe.enums.MySQLTable;
import de.ketchupbombe.listeners.InventoryClickListener;
import de.ketchupbombe.manager.*;
import de.ketchupbombe.utils.variables;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class FFA extends JavaPlugin {

    private Executor executor = Executors.newCachedThreadPool();
    private static FFA INSTANCE;
    private ConfigManager configManager;
    private MapManager mapManager;
    private LocationManager locationManager;
    private MapChangeManager mapChangeManager;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        //set instance
        INSTANCE = this;

        //MySQL
        MySQL.setUp();
        MySQL.connect();
        createMySQLTables();

        //create configs
        configManager = new ConfigManager();
        configManager.setUpConfig();

        //register commands and events
        init();

        //register managers
        mapManager = new MapManager();
        mapManager.updateMapCache();
        locationManager = new LocationManager();
        mapChangeManager = new MapChangeManager();
        for (World world : Bukkit.getWorlds()) {
            if (!mapManager.isMapExist(world.getName())) {
                mapManager.createNewMap(world.getName(), null, true);
                locationManager.saveLocation(LocationType.SPAWN, world.getSpawnLocation(), world.getName());
            }
        }
        mapManager.updateMapCache();
        inventoryManager = new InventoryManager();
        inventoryManager.setUpMapsInv();
        //setup map
        if (mapChangeManager.isMapChangeEnabled()) {
            mapChangeManager.ChangeMapTo(mapManager.getRandomMap());
            mapChangeManager.startMapChangeScheduler();
        } else {
            mapChangeManager.ChangeMapTo(mapManager.getDefaultMap());
        }
        //send enable-message
        Bukkit.getConsoleSender().sendMessage(variables.getPrefix() + "§aenabled!");
    }

    @Override
    public void onDisable() {
        //disconnect from MySQL
        MySQL.close();

        //send disable-message
        Bukkit.getConsoleSender().sendMessage(variables.getPrefix() + "§cdisabled!");
    }

    /**
     * @return FFA-class
     * @see FFA
     */
    public static FFA getInstance() {
        return INSTANCE;
    }

    /**
     * To make things async.
     *
     * @return new Executor
     * @see Executor
     */
    public Executor getExecutor() {
        return executor;
    }

    /**
     * To manage default config
     *
     * @return new ConfigManager
     * @see ConfigManager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * To manage all Maps
     *
     * @return new MapManager
     * @see MapManager
     */
    public MapManager getMapManager() {
        return mapManager;
    }

    /**
     * To manage all Locations
     *
     * @return new LocationManager
     * @see LocationManager
     */
    public LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * To manage MapChange
     *
     * @return new MapChangeManager
     * @see MapChangeManager
     */
    public MapChangeManager getMapChangeManager() {
        return mapChangeManager;
    }

    /**
     * To manage inventorys
     *
     * @return new InventoryManager
     * @see InventoryManager
     */
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    /**
     * Create standart MySQL-tables in onEnable
     */
    private void createMySQLTables() {
        MySQL.updateAsync("CREATE TABLE IF NOT EXISTS " + MySQLTable.LOCATION.getTablename() + " (type VARCHAR(64), worldname VARCHAR(64), location VARCHAR(255))");
        MySQL.updateAsync("CREATE TABLE IF NOT EXISTS " + MySQLTable.MAPS.getTablename() + " (mapname VARCHAR(64), author VARCHAR(64), online VARCHAR(7))");
    }

    /**
     * Register all commands and listeners
     */
    private void init() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryClickListener(), this);

        this.getCommand("ImportWorld").setExecutor(new ImportWorldCommand());
        this.getCommand("maps").setExecutor(new MapsCommand());
        this.getCommand("ffareload").setExecutor(new FFAReloadCommand());
    }
}
