package de.ketchupbombe;

import de.ketchupbombe.MySQL.MySQL;
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

    @Override
    public void onEnable() {
        MySQL.connect();
    }

    @Override
    public void onDisable() {
        MySQL.close();
    }

    public static FFA getInstance() {
        return INSTANCE;
    }

    public Executor getExecutor() {
        return executor;
    }
}
