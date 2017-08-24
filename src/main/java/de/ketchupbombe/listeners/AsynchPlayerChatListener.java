package de.ketchupbombe.listeners;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.variables;
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
        e.setCancelled(true);
        ffa.getKitManager().waitingForKitname.remove(p);
        ItemStack[] contents = ffa.getKitManager().contents.get(p);
        ItemStack[] armorContents = ffa.getKitManager().armorContents.get(p);
        ffa.getKitManager().contents.remove(p);
        ffa.getKitManager().armorContents.remove(p);

        ffa.getKitManager().createNewKit(e.getMessage(), contents, armorContents);
        p.sendMessage(variables.getPrefix() + "You created a new kit with the name: §6 " + e.getMessage());
    }

}
