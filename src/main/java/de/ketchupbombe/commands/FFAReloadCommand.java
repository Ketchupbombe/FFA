package de.ketchupbombe.commands;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class FFAReloadCommand implements CommandExecutor {

    private FFA ffa = FFA.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("ffa.command.reload")) {
            sender.sendMessage(variables.getPrefix() + "reloading FFA...");
            ffa.getMapManager().updateMapCache();
            ffa.getInventoryManager().setUpMapsInv();
            ffa.getKitManager().updateKitCache();
            ffa.getInventoryManager().setUpKitsInv();

            sender.sendMessage(variables.getPrefix() + "§aReload complete!");
        } else sender.sendMessage(variables.getNoPermission());
        return false;
    }
}
