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

    private File file = null;
    private FileConfiguration cfg = null;

    /**
     * Create new File called "Messages.yml" in datafolder
     */
    public void createMessagesConfig() {
        this.file = new File(FFA.getInstance().getDataFolder(), "Messages.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);

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
        cfg.addDefault(path, value);
        saveFile();
    }

    /**
     * Get a Message from Messages.yml
     *
     * @param path place to find value
     * @return value of path
     */
    public String getMessage(String path) {
        return cfg.getString(path);
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