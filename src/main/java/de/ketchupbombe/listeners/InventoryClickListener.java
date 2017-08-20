package de.ketchupbombe.listeners;

import de.ketchupbombe.FFA;
import de.ketchupbombe.enums.LocationType;
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
                // define current map
                String currentOpenMap = e.getInventory().getTitle().substring(2);
                if (variables.blockedClickedItems.contains(e.getCurrentItem())) {
                    e.setCancelled(true);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(" §8(§a§lONLINE§r§8)")
                        || e.getCurrentItem().getItemMeta().getDisplayName().contains(" §8(§4§lOFFLINE§r§8)")) {
                    String currentMap = ffa.getInventoryManager().getMapnameByItem(e.getCurrentItem());

                    p.openInventory(ffa.getInventoryManager().createMapInv(currentMap));
                }

                // Set-Spawnpoint Item
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Set Spawwnpoint")) {
                    p.closeInventory();
                    if (p.getWorld().getName().equalsIgnoreCase(currentOpenMap)) {
                        ffa.getLocationManager().updateLocation(LocationType.SPAWN, p.getLocation(), currentOpenMap);
                        p.sendMessage(variables.getPrefix() + "You updated the location of: §6" + currentOpenMap);
                    } else p.sendMessage(variables.getPrefix() + "§cYou are not in the right world!");
                }

                // Teleport Item
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Teleport to Map")) {
                    p.closeInventory();
                    if (!p.getWorld().getName().equalsIgnoreCase(currentOpenMap)) {
                        p.teleport(ffa.getLocationManager().getSpawnLocationByWorld(currentOpenMap));
                    } else p.sendMessage(variables.getPrefix() + "§cYou are already in this world!");
                }

                // Make map online Item
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aEnable Map")) {
                    p.closeInventory();
                    if (!ffa.getMapManager().isMapOnline(currentOpenMap)) {
                        ffa.getMapManager().updateOnline(currentOpenMap, true);
                        p.sendMessage(variables.getPrefix() + "The map is now online!");
                        p.sendMessage(variables.getPrefix() + "Use §6/ffareload §7to updated the maps!");
                    } else {
                        p.sendMessage(variables.getPrefix() + "§cThis map is already online!");
                        p.sendMessage(variables.getPrefix() + "§cUse §6/ffareload §cto update the maps!");
                    }
                }

                // Make map offline Item
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cDisable Map")) {
                    p.closeInventory();
                    if (ffa.getMapManager().isMapOnline(currentOpenMap)) {
                        ffa.getMapManager().updateOnline(currentOpenMap, false);
                        p.sendMessage(variables.getPrefix() + "The map is now offline!");
                        p.sendMessage(variables.getPrefix() + "Use §6/ffareload §7to updated the maps!");
                    } else {
                        p.sendMessage(variables.getPrefix() + "§cThis map is already offline!");
                        p.sendMessage(variables.getPrefix() + "§cUse §6/ffareload §cto update the maps!");
                    }
                }

                // Forcemap Item
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Forcemap")) {
                    p.closeInventory();
                    if (ffa.getMapManager().isMapOnline(currentOpenMap)) {
                        if (ffa.getMapChangeManager().isCanChangeMap()) {
                            ffa.getMapChangeManager().setFollowingMap(currentOpenMap);
                            ffa.getMapChangeManager().setMapChangeSchedulerTime(ffa.getConfig().getInt("MapChange.cancleForcemapTime"));
                            p.sendMessage(variables.getPrefix() + "Changed the map to §6" + currentOpenMap);
                        } else p.sendMessage(variables.getPrefix() + "§cYou can't change the map!");
                    } else p.sendMessage(variables.getPrefix() + "§cThis map is not online!");
                }

            } catch (NullPointerException ex) {

            }
        }
    }
}
