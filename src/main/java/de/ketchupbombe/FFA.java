package de.ketchupbombe;

import de.ketchupbombe.MySQL.MySQL;
import de.ketchupbombe.manager.ConfigManager;
import de.ketchupbombe.manager.MessagesManager;
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
    private MessagesManager messagesManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        //set instance
        INSTANCE = this;

        //connect to MySQL
        MySQL.setUp();
        MySQL.connect();

        //create configs
        messagesManager = new MessagesManager();
        messagesManager.createMessagesConfig();
        configManager = new ConfigManager();
        configManager.setUpConfig();
    }

    @Override
    public void onDisable() {
        //disconnect from MySQL
        MySQL.close();
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
     * To manage all messages
     *
     * @return new MessagesManager
     * @see MessagesManager
     */
    public MessagesManager getMessagesManager() {
        return messagesManager;
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
}
