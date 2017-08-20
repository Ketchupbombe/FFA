package de.ketchupbombe.manager;

import de.ketchupbombe.FFA;
import de.ketchupbombe.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

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
                        .build());
            } else {
                this.mapsInv.addItem(new ItemBuilder(Material.APPLE)
                        .withName("§e" + mapname + " §8(§4§lOFFLINE§r§8)")
                        .withLore("§7Click for options")
                        .build());

            }
        }
    }

}
