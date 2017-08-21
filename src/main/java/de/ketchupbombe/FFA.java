package de.ketchupbombe;

import de.ketchupbombe.MySQL.MySQL;
import de.ketchupbombe.commands.*;
import de.ketchupbombe.enums.LocationType;
import de.ketchupbombe.enums.MySQLTable;
import de.ketchupbombe.listeners.InventoryClickListener;
import de.ketchupbombe.listeners.PlayerJoinListener;
import de.ketchupbombe.manager.*;
import de.ketchupbombe.utils.ItemSerialization;
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
    private KitManager kitManager;

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
        kitManager = new KitManager();
        kitManager.updateKitCache();
        createStandardKit();
        kitManager.updateKitCache();
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
     * To manage kits
     *
     * @return new KitManager
     * @see KitManager
     */
    public KitManager getKitManager() {
        return kitManager;
    }

    /**
     * Create standart MySQL-tables in onEnable
     */
    private void createMySQLTables() {
        MySQL.updateAsync("CREATE TABLE IF NOT EXISTS " + MySQLTable.LOCATION.getTablename() + " (type VARCHAR(64), worldname VARCHAR(64), location VARCHAR(255))");
        MySQL.updateAsync("CREATE TABLE IF NOT EXISTS " + MySQLTable.MAPS.getTablename() + " (mapname VARCHAR(64), author VARCHAR(64), online VARCHAR(7))");
        MySQL.updateAsync("CREATE TABLE IF NOT EXISTS " + MySQLTable.KIT.getTablename() + " (kitname VARCHAR(64), contents TEXT(1500), armorContents TEXT(1500))");
    }

    /**
     * Register all commands and listeners
     */
    private void init() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryClickListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);

        this.getCommand("ImportWorld").setExecutor(new ImportWorldCommand());
        this.getCommand("maps").setExecutor(new MapsCommand());
        this.getCommand("ffareload").setExecutor(new FFAReloadCommand());
        this.getCommand("forcemap").setExecutor(new ForceMapCommand());
        this.getCommand("forcekit").setExecutor(new ForceKitCommand());
    }

    private void createStandardKit() {
        if (!getKitManager().isKitExist("Standard")) {
            getKitManager().createNewKit("Standard", ItemSerialization.itemStackArrayFromBase64(
                    "rO0ABXcEAAAAJHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw\n" +
                            "dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi\n" +
                            "bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl\n" +
                            "Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA\n" +
                            "AAJ0AAI9PXQABHR5cGV1cQB+AAYAAAACdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNr\n" +
                            "dAALU1RPTkVfU1dPUkRzcQB+AABzcQB+AAN1cQB+AAYAAAACcQB+AAhxAH4ACXVxAH4ABgAAAAJx\n" +
                            "AH4AC3QAC0ZJU0hJTkdfUk9Ec3EAfgAAc3EAfgADdXEAfgAGAAAAAnEAfgAIcQB+AAl1cQB+AAYA\n" +
                            "AAACcQB+AAt0AANCT1dwcHBwcHNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJdAAGYW1v\n" +
                            "dW50dXEAfgAGAAAAA3EAfgALdAAFQVJST1dzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIA\n" +
                            "AUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAFHBwcHBwcHBwcHBw\n" +
                            "cHBwcHBwcHBwcHBwcHBwcA=="), ItemSerialization.itemStackArrayFromBase64(
                    "rO0ABXcEAAAABHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw\n" +
                            "dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi\n" +
                            "bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmpl\n" +
                            "Y3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAA\n" +
                            "AAJ0AAI9PXQABHR5cGV1cQB+AAYAAAACdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNr\n" +
                            "dAAKSVJPTl9CT09UU3NxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAGAAAAAnEA\n" +
                            "fgALdAANSVJPTl9MRUdHSU5HU3NxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAG\n" +
                            "AAAAAnEAfgALdAASRElBTU9ORF9DSEVTVFBMQVRFc3EAfgAAc3EAfgADdXEAfgAGAAAAAnEAfgAI\n" +
                            "cQB+AAl1cQB+AAYAAAACcQB+AAt0AAtJUk9OX0hFTE1FVA=="));
        }
    }
}
