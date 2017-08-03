package de.ketchupbombe.utils;

import de.ketchupbombe.FFA;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class variables {

    private static String PREFIX = FFA.getInstance().getMessagesManager().getMessage("prefix");
    private static String NOPERMISSION = FFA.getInstance().getMessagesManager().getMessage("noPermission");
    private static String NOCONSOLE = FFA.getInstance().getMessagesManager().getMessage("noConsole");

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
