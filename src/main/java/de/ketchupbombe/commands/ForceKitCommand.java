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
public class ForceKitCommand implements CommandExecutor {

    private FFA ffa = FFA.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("ffa.command.forcekit")) {
            if (args.length == 1) {
                String kitname = args[0];
                if (ffa.getKitManager().isKitExist(kitname)) {
                    ffa.getKitManager().changeKit(kitname);
                    sender.sendMessage(variables.getPrefix() + "Changed kit to: §6" + kitname);
                } else sendHelp(sender);
            } else sendHelp(sender);
        } else sender.sendMessage(variables.getNoPermission());
        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(variables.getPrefix() + "§cUsage: /forcekit <kitname>");
        for (String kitnames : ffa.getKitManager().getKitNamesCache()) {
            sender.sendMessage(variables.getPrefix() + "§a§l" + kitnames);
        }
    }
}
