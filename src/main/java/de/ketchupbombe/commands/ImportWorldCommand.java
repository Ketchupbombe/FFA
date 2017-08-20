package de.ketchupbombe.commands;

import de.ketchupbombe.FFA;
import de.ketchupbombe.enums.LocationType;
import de.ketchupbombe.utils.variables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class ImportWorldCommand implements CommandExecutor {

    private FFA ffa = FFA.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("ffa.command.importworld")) {
            if (args.length == 3) {
                String mapname = args[0];
                sender.sendMessage(variables.getPrefix() + "loading...");
                if (!ffa.getLocationManager().isWorldLoaded(mapname) && !ffa.getMapManager().isMapExist(mapname)) {
                    if (ffa.getLocationManager().isFolderExist(mapname)) {

                        if (args[1].equalsIgnoreCase("true")) {
                            ffa.getLocationManager().importWorld(mapname);
                            ffa.getMapManager().createNewMap(mapname, args[2], true);
                            ffa.getLocationManager().saveLocation(LocationType.SPAWN, Bukkit.getWorld(mapname).getSpawnLocation(), mapname);
                            sender.sendMessage(variables.getPrefix() + "Imported Map \"" + mapname + "\"");
                        } else if (args[1].equalsIgnoreCase("false")) {
                            ffa.getLocationManager().importWorld(mapname);
                            ffa.getMapManager().createNewMap(mapname, args[2], false);
                            ffa.getLocationManager().saveLocation(LocationType.SPAWN, Bukkit.getWorld(mapname).getSpawnLocation(), mapname);
                            sender.sendMessage(variables.getPrefix() + "Imported Map \"" + mapname + "\"");

                        } else sendHelp(sender);
                    } else sender.sendMessage(variables.getPrefix() + "§cThis world does not exist!");
                } else sender.sendMessage(variables.getPrefix() + "§cThis world is already imported!");
            } else this.sendHelp(sender);
        } else sender.sendMessage(variables.getNoPermission());
        return false;
    }

    /**
     * Send a help message with the usage of the command
     *
     * @param sender CommandSender to send the message
     */
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(variables.getPrefix() + "§cUsage: §6/ImportWorld <worldname> <true/false> (<--add world to Maps) <author>");

    }
}
