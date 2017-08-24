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
public class MapsCommand implements CommandExecutor {

    private FFA ffa = FFA.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender.hasPermission("ffa.command.maps.admin") && sender instanceof Player) {
            Player p = (Player) sender;
            if (ffa.getKitManager().editingKit.containsKey(p)
                    || ffa.getKitManager().creatingKit.contains(p)
                    || ffa.getKitManager().waitingForKitname.contains(p)) {
                p.sendMessage(variables.getPrefix() + "You must finish your creating/editing kit process!");
                return true;
            }
            p.openInventory(ffa.getInventoryManager().mapsInv);
        } else {
            for (String mapname : ffa.getMapManager().getOnlineMapCache()) {
                sender.sendMessage("§a§l" + mapname);
            }
        }

        return false;
    }
}
