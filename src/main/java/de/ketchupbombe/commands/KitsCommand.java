package de.ketchupbombe.commands;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class KitsCommand implements CommandExecutor {

    private FFA ffa = FFA.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("ffa.command.maps.admin") && sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(ffa.getInventoryManager().kitsInv);
        } else {
            for (String kitnames : ffa.getKitManager().getKitNamesCache()) {
                sender.sendMessage(variables.getPrefix() + "§a§l" + kitnames);
            }
        }
        return false;
    }
}
