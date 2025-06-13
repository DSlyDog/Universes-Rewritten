package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.gui.OverrideUI;
import net.whispwriting.universes.gui.UIItemData;
import net.whispwriting.universes.gui.guis.OverridesGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class OverridesInventoryClick implements Listener {

    private Universes plugin;
    private String uuid;
    public OverridesInventoryClick(Universes pl, String uid){
        plugin = pl;
        uuid = uid;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (!uuid.equals(e.getWhoClicked().getUniqueId().toString()))
            return;

        if (e.getClick().isKeyboardClick())
            return;

        e.setCancelled(true);
        try {
            OverridesGUI.getFor((Player) e.getWhoClicked(), plugin).getItem(e.getSlot()).onClick((Player) e.getWhoClicked(), plugin);
            OverridesGUI.getFor((Player) e.getWhoClicked(), plugin).updateItem(e.getSlot());
        }catch(IndexOutOfBoundsException er){
            //do nothing
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (!uuid.equals(e.getPlayer().getUniqueId().toString())){
            return;
        }
        HandlerList.unregisterAll(OverridesInventoryClick.this);
    }

}
