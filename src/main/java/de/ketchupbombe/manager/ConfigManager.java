package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class ConfigManager {

    FileConfiguration cfg = FFA.getInstance().getConfig();

    /**
     * Create config and enter new things
     */
    public void setUpConfig() {
        addObject("MySQL.host", "localhost");
        addObject("MySQL.port", 3306);
        addObject("MySQL.database", "FFA");
        addObject("MySQL.username", "username");
        addObject("MySQL.password", "password");
        addObject("prefix", "&9FFA &8| &7");
        addObject("MapChange.enable", true);
        addObject("MapChange.defaultMap", "world");
        addObject("MapChange.repatingTime", 600);
        addObject("MapChange.cancleForcemapTime", 30);
        cfg.options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Add a Object to config
     *
     * @param path  which path
     * @param value object
     */
    public void addObject(String path, Object value) {
        cfg.addDefault(path, value);
    }

    /**
     * Save the config
     *
     * @see FFA#saveConfig()
     */
    public void saveConfig() {
        FFA.getInstance().saveConfig();
    }

}
