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
public class ForceMapCommand implements CommandExecutor {

    private FFA ffa = FFA.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("ffa.command.forcemap")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (ffa.getKitManager().editingKit.containsKey(p)
                        || ffa.getKitManager().creatingKit.contains(p)
                        || ffa.getKitManager().waitingForKitname.contains(p)) {
                    p.sendMessage(variables.getPrefix() + "You must finish your creating/editing kit process!");
                    return true;
                }

            }
            if (args.length == 1) {
                String mapname = args[0];
                if (ffa.getMapManager().isMapExist(mapname) && ffa.getMapManager().isMapOnline(mapname)) {
                    if (ffa.getMapChangeManager().isCanChangeMap()) {
                        ffa.getMapChangeManager().setFollowingMap(mapname);
                        ffa.getMapChangeManager().setMapChangeSchedulerTime(ffa.getConfig().getInt("MapChange.cancleForcemapTime"));
                        sender.sendMessage(variables.getPrefix() + "Changed the map to §6" + mapname);
                    } else sender.sendMessage(variables.getPrefix() + "§cYou can't change the map!");
                } else sendHelp(sender);
            } else sendHelp(sender);
        } else sender.sendMessage(variables.getNoPermission());

        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(variables.getPrefix() + "§cUsage: /forcemap <mapname>");
        for (String maps : ffa.getMapManager().getOnlineMapCache()) {
            sender.sendMessage(variables.getPrefix() + "§a§l" + maps);
        }
    }

}
