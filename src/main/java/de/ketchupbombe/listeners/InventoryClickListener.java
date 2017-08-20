package de.ketchupbombe.listeners;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.variables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class InventoryClickListener implements Listener {

    private FFA ffa = FFA.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            try {
                if (variables.blockedClickedItems.contains(e.getCurrentItem())) {
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(" §8(§a§lONLINE§r§8)")
                        || e.getCurrentItem().getItemMeta().getDisplayName().contains(" §8(§4§lOFFLINE§r§8)")) {

                    // define current map
                    String currentMap = ffa.getInventoryManager().getMapnameByItem(e.getCurrentItem());

                    p.openInventory(ffa.getInventoryManager().createMapInv(currentMap));
                }
            } catch (NullPointerException ex) {

            }
        }
    }
}
