package de.ketchupbombe.manager;

import de.ketchupbombe.MySQL.MySQL;
import de.ketchupbombe.enums.MySQLTable;
import de.ketchupbombe.utils.ItemSerialization;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class KitManager {

    private ArrayList<String> kitNamesCache = new ArrayList<>();
    private String currentKit;

    /**
     * Save new kit in databes
     *
     * @param kitname       name of new kit
     * @param contents      contents of new kit
     * @param armorContents armor contents of new kit
     */
    public void createNewKit(String kitname, ItemStack[] contents, ItemStack[] armorContents) {
        String dataContents = ItemSerialization.itemStackArrayToBase64(contents);
        String dataArmorContents = ItemSerialization.itemStackArrayToBase64(armorContents);
        MySQL.updateAsync("INSERT INTO " + MySQLTable.KIT.getTablename()
                + "(kitname, contents, armorContents) VALUES ('"
                + kitname + "','" + dataContents + "','" + dataArmorContents + "')");
    }

    /**
     * To edit a kit
     *
     * @param kitname       kitname of kit to edit
     * @param contents      contents of kit to edit
     * @param armorContents armor contents of kit to edit
     */
    public void updatedKit(String kitname, ItemStack[] contents, ItemStack[] armorContents) {
        String dataContents = ItemSerialization.itemStackArrayToBase64(contents);
        String dataArmorContents = ItemSerialization.itemStackArrayToBase64(armorContents);
        MySQL.updateAsync("UPDATE " + MySQLTable.KIT.getTablename()
                + " SET contents='" + dataContents + "' AND armorContents='" + dataArmorContents
                + "' WHERE kitname='" + kitname + "'");
    }

    /**
     * Get contents of a kit which is stored in database
     *
     * @param kitname name of kit to get contents
     * @return contents of specified kit
     */
    public ItemStack[] getKitContents(String kitname) {
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.KIT.getTablename() + " WHERE kitname='" + kitname + "'");
        try {
            if (rs.next()) {
                String data = rs.getString("contents");
                return ItemSerialization.itemStackArrayFromBase64(data);
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public ItemStack[] getKitArmorKontents(String kitname) {
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.KIT.getTablename() + " WHERE kitname='" + kitname + "'");
        try {
            if (rs.next()) {
                String data = rs.getString("armorContents");
                return ItemSerialization.itemStackArrayFromBase64(data);
            }
        } catch (SQLException e) {

        }
        return null;
    }

    /**
     * Get all kitnames which are stored in database
     *
     * @return list of all kitnames
     */
    private ArrayList<String> getKits() {
        ArrayList<String> kits = new ArrayList<>();
        ResultSet rs = MySQL.getResult("SELECT * FROM " + MySQLTable.KIT.getTablename());
        try {
            while (rs.next()) {
                kits.add(rs.getString("kitname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kits;
    }

    /**
     * Update Cache in which are all kits local stored
     */
    public void updateKitCache() {
        kitNamesCache.clear();
        for (String kit : getKits()) {
            ItemStack[] kitContents = getKitContents(kit);
            kitNamesCache.add(kit);
        }
    }

    /**
     * Get all kitnames which are stored in local cache
     *
     * @return list of all kitnames
     */
    public ArrayList<String> getKitNamesCache() {
        return kitNamesCache;
    }

    /**
     * Get a random kitname of all kits which are stored in local cache
     *
     * @return random kitname
     */
    public String getRandomKit() {
        return getKitNamesCache().get(new Random().nextInt(getKitNamesCache().size()));
    }

    /**
     * Check if kit with specified name exists
     *
     * @param kitname kitname to check
     * @return true if exists
     */
    public boolean isKitExist(String kitname) {
        return getKitNamesCache().contains(kitname);
    }

    /**
     * Change to kit at Server to a specified kit
     * Inventory of all players get cleared an set contents of specified kitname
     *
     * @param kitname kit to change
     */
    public void changeKit(String kitname) {
        if (isKitExist(kitname)) {
            currentKit = kitname;
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.getInventory().setContents(getKitContents(kitname));
                all.getInventory().setArmorContents(getKitArmorKontents(kitname));
            }
            System.out.println("Changed kit to: " + kitname);
        }
    }

    /**
     * Get current playing kit
     *
     * @return current kitname
     */
    public String getCurrentKit() {
        return currentKit;
    }

}
