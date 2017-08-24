package de.ketchupbombe.listeners;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.variables;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class AsynchPlayerChatListener implements Listener {

    private FFA ffa = FFA.getInstance();

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (ffa.getKitManager().waitingForKitname.contains(p)) {
            endKitCreation(e, p);

        }
    }

    /**
     * To create a new Kit and set the name of this
     *
     * @param e event to get the name of the kit
     * @param p creator of the new kit
     */
    private void endKitCreation(AsyncPlayerChatEvent e, Player p) {
        if (!ffa.getKitManager().isKitExist(e.getMessage())) {
            e.setCancelled(true);
            ffa.getKitManager().waitingForKitname.remove(p);
            ItemStack[] contents = ffa.getKitManager().contents.get(p);
            ItemStack[] armorContents = ffa.getKitManager().armorContents.get(p);
            ffa.getKitManager().contents.remove(p);
            ffa.getKitManager().armorContents.remove(p);

            ffa.getKitManager().createNewKit(e.getMessage(), contents, armorContents);
            Bukkit.getScheduler().runTaskLater(ffa, () -> {

                p.setGameMode(GameMode.SURVIVAL);
                p.getInventory().setContents(ffa.getKitManager().getKitContents(ffa.getKitManager().getCurrentKit()));
                p.getInventory().setArmorContents(ffa.getKitManager().getKitArmorKontents(ffa.getKitManager().getCurrentKit()));
                p.teleport(ffa.getLocationManager().getSpawnLocationByWorld(ffa.getMapManager().getCurrentMap()));
            }, 1);
            p.sendMessage(variables.getPrefix() + "You created a new kit with the name: §6 " + e.getMessage());
        } else {
            e.setCancelled(true);
            p.sendMessage(variables.getPrefix() + "§lThis kit does already exist! Please enter another name!");

        }
    }

}
