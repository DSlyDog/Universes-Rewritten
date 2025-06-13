package net.whispwriting.universes.gui;

import org.bukkit.inventory.ItemStack;

public class UIItemData {

    private ItemStack item;
    private String id;

    public UIItemData(ItemStack i, String d){
        item = i;
        id = d;
    }

    public ItemStack getItem(){
        return item;
    }

    public String getID(){
        return id;
    }

}
