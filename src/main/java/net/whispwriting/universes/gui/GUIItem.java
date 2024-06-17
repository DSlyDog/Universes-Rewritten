package net.whispwriting.universes.gui;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class GUIItem {

    private String displayName;
    private int amount;
    private Material[] materials;
    private Map<Integer, List<String>> lores = new HashMap<>();
    private int matIndex = 0;
    private int loreIndex = 0;
    private boolean useEnchantment;
    private boolean isEnchanted = false;
    private ItemStack item = new ItemStack(Material.DIRT, amount);

    public GUIItem(){}

    public void create(String displayName, int amount, boolean useEnchantment, boolean currentValue, Material[] materials, List<String>... lore) {
        create(displayName, amount, useEnchantment, materials, lore);
        isEnchanted = !currentValue;

        if (currentValue)
            incrementIndex();
    }

    public void create(String displayName, int amount, boolean useEnchantment, int currentValue, Material[] materials, List<String>... lore) {
        create(displayName, amount, useEnchantment, materials, lore);

        for (int i=0; i<currentValue; i++){
            incrementIndex();
        }
    }

    private void create(String displayName, int amount, boolean useEnchantment, Material[] materials, List<String>[] lore) {
        this.displayName = displayName;
        this.amount = amount;
        this.materials = materials;
        this.useEnchantment = useEnchantment;

        for (int i=0; i<lore.length; i++){
            lores.put(i, lore[i]);
        }
    }

    public String getName() {
        return displayName;
    }

    public ItemStack getAsItemStack(){
        item.setType(materials[matIndex]);
        item.setAmount(amount);
        if (useEnchantment){
            if (isEnchanted) {
                item.removeEnchantments();
                isEnchanted = false;
            }else{
                item.addUnsafeEnchantment(Enchantment.MENDING, 2);
                isEnchanted = true;
            }

        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lores.get(loreIndex));
        item.setItemMeta(meta);

        return item;
    }

    public void incrementIndex(){
        matIndex++;
        if (matIndex >= materials.length)
            matIndex = 0;

        loreIndex++;
        if (loreIndex >= lores.size())
            loreIndex = 0;
    }

    public void onClick(Player player, Universes plugin){}

    public void onClick(Player player, Universe universe){}

    public void onClick(Player player, Universe universe, Universes plugin){}
}
