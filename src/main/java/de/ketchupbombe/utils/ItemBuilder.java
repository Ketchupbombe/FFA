package de.ketchupbombe.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Create your own ItemStacks
 *
 * @author Ketchupbombe
 * @version 1.0
 */
public class ItemBuilder {

    private ItemStack is;
    private ItemMeta im;
    private List<String> lore = new ArrayList<>();

    /**
     * Constructor to set the material type
     *
     * @param material Material type
     */
    public ItemBuilder(Material material) {
        this.is = new ItemStack(material);
        this.im = this.is.getItemMeta();
    }

    /**
     * Set amount of created item
     *
     * @param amount amount of items
     * @return ItemBuilder class
     */
    public ItemBuilder withAmount(int amount) {
        is.setAmount(amount);
        return this;
    }

    /**
     * Displayname of created item
     *
     * @param name Displayname of item
     * @return ItemBuilder class
     */
    public ItemBuilder withName(String name) {
        im.setDisplayName(name);
        return this;
    }

    /**
     * Add a lore-line to item
     *
     * @param lore new lore-line
     * @return ItemBuilder class
     */
    public ItemBuilder withLore(String lore) {
        this.lore.add(lore);
        return this;
    }

    /**
     * Add a full lore to item
     *
     * @param lores lore as list
     * @return ItemBuilder class
     */
    public ItemBuilder withLore(List<String> lores) {
        for (String s : lores) {
            this.lore.add(s);
        }
        return this;
    }

    /**
     * Add a glow-effect to item
     *
     * @return ItemBuilder class
     */
    public ItemBuilder withGlow() {
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addEnchant(Enchantment.KNOCKBACK, 1, false);
        return this;
    }

    /**
     * Add a enchantment to item
     *
     * @param enchantment Enchantment type
     * @param level       Enchantment level
     * @return ItemBuilder class
     */
    public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
        is.addEnchantment(enchantment, level);
        return this;
    }

    /**
     * Set durabilty of item
     * (for ex. for subID's)
     *
     * @param damage Set durability of item
     * @return ItemBuilder class
     */
    public ItemBuilder withDamage(short damage) {
        is.setDurability(damage);
        return this;
    }

    /**
     * Make the created item to ItemStack
     *
     * @return created item
     */
    public ItemStack build() {
        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

}
