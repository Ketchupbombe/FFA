package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class MessagesManager {

    private File file = new File(FFA.getInstance().getDataFolder(), "Messages.yml");
    private FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    /**
     * Create new File called "Messages.yml" in datafolder
     */
    public void createMessagesConfig() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        addMessage("noPermission", "%prefix%&cNo Permission!");
        addMessage("enable", "%prefix%&aenabled!");
        addMessage("disable", "%prefix%&cdisabled!");
        saveFile();
    }

    /**
     * Save file
     */
    private void saveFile() {
        try {
            cfg.save(file);
        } catch (IOException e) {
        }
    }

    /**
     * Add a message to Messages.yml
     *
     * @param path  which path
     * @param value Message
     */
    public void addMessage(String path, String value) {
        cfg.set(path, value);
    }

    /**
     * Get a Message from Messages.yml
     *
     * @param path place to find value
     * @return value of path
     */
    public String getMessage(String path) {
        return cfg.getString(path)
                .replaceAll("%prefix%", FFA.getInstance().getConfig().getString("prefix"))
                .replaceAll("&", "§");
    }

    /**
     * Find out if path exist
     *
     * @param path place to find value
     * @return if path exists
     */
    public boolean isPathExist(String path) {
        return cfg.get(path) != null;
    }
}
