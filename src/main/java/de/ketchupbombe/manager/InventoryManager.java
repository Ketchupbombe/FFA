package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class InventoryManager {

    private FFA ffa = FFA.getInstance();

    public Inventory mapsInv = Bukkit.createInventory(null, 6 * 9, "§eMaps");

    /**
     *
     */
    public void setUpMapsInv() {
        this.mapsInv.clear();
        for (String mapname : ffa.getMapManager().getAllMapCache()) {
            if (ffa.getMapManager().isMapOnline(mapname)) {
                this.mapsInv.addItem(new ItemBuilder(Material.APPLE)
                        .withName("§e" + mapname + " §8(§a§lONLINE§r§8)")
                        .withLore("§7Click for options")
                        .withGlow()
                        .block()
                        .build());
            } else {
                this.mapsInv.addItem(new ItemBuilder(Material.APPLE)
                        .withName("§e" + mapname + " §8(§4§lOFFLINE§r§8)")
                        .withLore("§7Click for options")
                        .block()
                        .build());

            }
        }
    }

    /**
     * Create a inventory to a map
     *
     * @param mapname name of the Map to create
     * @return created Inventory
     */
    public Inventory createMapInv(String mapname) {
        try {
            Inventory inv = Bukkit.createInventory(null, 9, "§e" + mapname);
            List<String> infoLore = new ArrayList<>();
            infoLore.add(" ");
            infoLore.add("§6Mapname: §e" + mapname);
            infoLore.add(" ");
            infoLore.add("§6Author: §e" + ffa.getMapManager().getAuthor(mapname));
            inv.setItem(0, new ItemBuilder(Material.STONE_BUTTON).withName("§3Set Spawwnpoint").block().build());
            inv.setItem(2, new ItemBuilder(Material.REDSTONE).withName("§3Teleport to Map").block().build());
            if (ffa.getMapManager().isMapOnline(mapname)) {
                inv.setItem(4, new ItemBuilder(Material.WOOL)
                        .withName("§cDisable Map")
                        .withDamage((short) 14)
                        .withLore("§6Removes the Map from online-Maps")
                        .block()
                        .build());
            } else {
                inv.setItem(4, new ItemBuilder(Material.WOOL)
                        .withName("§aEnable Map")
                        .withDamage((short) 5)
                        .withLore("§6Add the Map to online-Maps")
                        .block()
                        .build());
            }
            inv.setItem(6, new ItemBuilder(Material.ARROW).withName("§3Forcemap").block().build());
            inv.setItem(8, new ItemBuilder(Material.SIGN).withName("§3Information:").block().withLore(infoLore).build());
            return inv;

        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("The map: " + mapname + " does not exist!");
        }
        return null;
    }

    /**
     * Get the name of a map by Displayname from a Item
     * This works only with Items which are created in method 'createMapInv'
     *
     * @param is ItemStack to get the mapname
     * @return translated mapname
     */
    public String getMapnameByItem(ItemStack is) {
        String displayname = is.getItemMeta().getDisplayName();
        int length;
        if (displayname.contains("ONLINE")) {
            length = displayname.length() - 19;
        } else {
            length = displayname.length() - 20;
        }
        String mapname = displayname.substring(2, length);
        return mapname;
    }

}
