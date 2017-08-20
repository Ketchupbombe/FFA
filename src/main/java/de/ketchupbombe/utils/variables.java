package de.ketchupbombe.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class variables {

    private static final String PREFIX = "§9FFA §8| §7";
    private static String NOPERMISSION = PREFIX + "§cNo Permission!";
    private static String NOCONSOLE = PREFIX + "§cThis is not available for consoles!";

    public static List<ItemStack> blockedClickedItems = new ArrayList<>();

    /**
     * In front of each message
     *
     * @return prefix
     */
    public static String getPrefix() {
        return PREFIX;
    }

    /**
     * If command is run by console an it must be a player
     *
     * @return noConsole
     */
    public static String getNoConsole() {
        return NOCONSOLE;
    }

    /**
     * If someone has not a permission
     *
     * @return noPermission
     */
    public static String getNoPermission() {
        return NOPERMISSION;
    }
}
