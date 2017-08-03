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

    public void createMessagesConfig() {
        this.file = new File(FFA.getInstance().getDataFolder(), "Messages.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);

        saveFile();
    }

    public void saveFile() {
        try {
            cfg.save(file);
        } catch (IOException e) {
        }
    }

    public void addMessage(String path, String value) {
        cfg.addDefault(path, value);
        saveFile();
    }

    public String getMessage(String path) {
        return cfg.getString(path);
    }

    public boolean isPathExist(String path) {
        return cfg.get(path) != null;
    }
}
