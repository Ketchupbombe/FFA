package de.ketchupbombe.commands;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.variables;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class KitCommand implements CommandExecutor {

    private FFA ffa = FFA.getInstance();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("ffa.command.kit")) {
                if (args.length == 1) {
                    // create new Kit
                    if (args[0].equalsIgnoreCase("create")) {
                        ffa.getKitManager().setCreate(p);

                        // accept kit creating or editing
                    } else if (args[0].equalsIgnoreCase("accept")) {
                        if (ffa.getKitManager().creatingKit.contains(p)) {
                            ffa.getKitManager().creatingKit.remove(p);
                            ffa.getKitManager().contents.put(p, p.getInventory().getContents());
                            ffa.getKitManager().armorContents.put(p, p.getInventory().getArmorContents());
                            ffa.getKitManager().waitingForKitname.add(p);


                            p.setGameMode(GameMode.SURVIVAL);
                            p.getInventory().clear();
                            p.getInventory().setContents(ffa.getKitManager().getKitContents(ffa.getKitManager().getCurrentKit()));
                            p.getInventory().setArmorContents(ffa.getKitManager().getKitArmorKontents(ffa.getKitManager().getCurrentKit()));

                            p.sendMessage(variables.getPrefix() + "§lPlease enter now the name of the Kit in chat!");
                        }
                        if (ffa.getKitManager().editingKit.containsKey(p)) {
                            ffa.getKitManager().updatedKit(ffa.getKitManager().editingKit.get(p), p.getInventory().getContents(), p.getInventory().getArmorContents());
                            ffa.getKitManager().editingKit.remove(p);

                            p.setGameMode(GameMode.SURVIVAL);
                            p.getInventory().setContents(ffa.getKitManager().getKitContents(ffa.getKitManager().getCurrentKit()));
                            p.getInventory().setArmorContents(ffa.getKitManager().getKitArmorKontents(ffa.getKitManager().getCurrentKit()));

                            p.sendMessage(variables.getPrefix() + "You edited the kit!");
                        }

                        // deny kit creating or editing
                    } else if (args[0].equalsIgnoreCase("abord")) {
                        if (ffa.getKitManager().creatingKit.contains(p)) {
                            ffa.getKitManager().creatingKit.remove(p);

                            p.setGameMode(GameMode.SURVIVAL);
                            p.getInventory().clear();
                            p.getInventory().setContents(ffa.getKitManager().getKitContents(ffa.getKitManager().getCurrentKit()));
                            p.getInventory().setArmorContents(ffa.getKitManager().getKitArmorKontents(ffa.getKitManager().getCurrentKit()));
                        }
                        if (ffa.getKitManager().editingKit.containsKey(p)) {
                            ffa.getKitManager().editingKit.remove(p);

                            p.setGameMode(GameMode.SURVIVAL);
                            p.getInventory().clear();
                            p.getInventory().setContents(ffa.getKitManager().getKitContents(ffa.getKitManager().getCurrentKit()));
                            p.getInventory().setArmorContents(ffa.getKitManager().getKitArmorKontents(ffa.getKitManager().getCurrentKit()));
                        }
                    }
                    // edit kit with command
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("edit")) {
                        String kitname = args[1];
                        if (ffa.getKitManager().isKitExist(kitname)) {
                            ffa.getKitManager().setEditKit(p, kitname);
                        } else p.sendMessage(variables.getPrefix() + "§cThis kit does not exist!");
                    }
                }
            } else p.sendMessage(variables.getNoPermission());
        } else sender.sendMessage(variables.getNoConsole());
        return false;
    }


}
