package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
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
    public Inventory kitsInv = Bukkit.createInventory(null, 6 * 9, "§eKits");

    /**
     * Put all maps in a inventory
     */
    public void setUpMapsInv() {
        this.mapsInv.clear();
        this.fillInvWithPlaceholder(this.mapsInv);
        int i = -1;
        for (String mapname : ffa.getMapManager().getAllMapCache()) {
            i++;
            if (ffa.getMapManager().isMapOnline(mapname)) {
                this.mapsInv.setItem(i, new ItemBuilder(Material.APPLE)
                        .withName("§e" + mapname + " §8(§a§lONLINE§r§8)")
                        .withLore("§7Click for options")
                        .withGlow()
                        .block()
                        .build());
            } else {
                this.mapsInv.setItem(i, new ItemBuilder(Material.APPLE)
                        .withName("§e" + mapname + " §8(§4§lOFFLINE§r§8)")
                        .withLore("§7Click for options")
                        .block()
                        .build());

            }
        }
        i = -1;
    }

    /**
     * Put all kits in a Inventory
     */
    public void setUpKitsInv() {
        this.kitsInv.clear();
        this.fillInvWithPlaceholder(this.kitsInv);
        int i = -1;
        for (String kitname : ffa.getKitManager().getKitNamesCache()) {
            i++;
            this.kitsInv.setItem(i, new ItemBuilder(Material.IRON_CHESTPLATE)
                    .withName("§3" + kitname)
                    .block()
                    .build());
        }
        this.kitsInv.setItem(53, new ItemBuilder(Material.BARRIER)
                .withName("§4Create new Kit")
                .withGlow()
                .block()
                .build());
        i = -1;
    }

    /**
     * Create a inventory to a map
     *
     * @param mapname name of the Map to create inventory
     * @return created map Inventory
     */
    public Inventory createMapInv(String mapname) {
        try {
            Inventory inv = Bukkit.createInventory(null, 9, "§e" + mapname);
            this.fillInvWithPlaceholder(inv);
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
     * Create a inventory to a kit
     *
     * @param kitname name of kit to create kit inventory
     * @return created kit inventory
     */
    public Inventory createKitsInv(String kitname) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§eKit: §6" + kitname);
        this.fillInvWithPlaceholder(inv);
        inv.setItem(0, new ItemBuilder(Material.FLINT_AND_STEEL)
                .withName("§3Edit kit")
                .block()
                .build());
        inv.setItem(2, new ItemBuilder(Material.REDSTONE)
                .withName("§4§lDELETE KIT")
                .block()
                .build());
        inv.setItem(4, new ItemBuilder(Material.ARROW)
                .withName("§3Forcekit")
                .block()
                .build());
        return inv;
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

    /**
     * Fill a inventory with placeholders
     * NOTE: use this method on beginning to create a inventory and set items after using this method!
     *
     * @param inv Inventory to fill with placeholders
     */
    public void fillInvWithPlaceholder(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).withName(" ").withDamage((short) 7).block().build());
        }
    }

}
