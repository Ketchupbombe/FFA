package de.ketchupbombe.utils;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class variables {

    private static final String PREFIX = "§9FFA §8| §7";
    private static String NOPERMISSION = PREFIX + "§cNo Permission!";
    private static String NOCONSOLE = PREFIX + "§cThis is not available for consoles!";

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
