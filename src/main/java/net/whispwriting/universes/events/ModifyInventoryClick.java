package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.gui.UIItemData;
import net.whispwriting.universes.gui.WorldSettingsUI_Old;
import net.whispwriting.universes.gui.guis.OverridesGUI;
import net.whispwriting.universes.gui.guis.WorldSettingsUI;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class ModifyInventoryClick implements Listener {

    private Universe universe;
    private String uuid;
    private Universes plugin;
    public ModifyInventoryClick(Universe universe, String uid, Universes plugin){
        this.universe = universe;
        this.uuid = uid;
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (!uuid.equals(e.getWhoClicked().getUniqueId().toString()))
            return;

        if (e.getClick().isKeyboardClick())
            return;

        e.setCancelled(true);
        try {
            WorldSettingsUI.getFor(universe).getItem(e.getSlot()).onClick((Player) e.getWhoClicked(), universe);
            WorldSettingsUI.getFor(universe).getItem(e.getSlot()).onClick((Player) e.getWhoClicked(), universe, plugin);
            WorldSettingsUI.getFor(universe).updateItem(e.getSlot());
        }catch(IndexOutOfBoundsException er){
            er.printStackTrace();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if (!uuid.equals(e.getPlayer().getUniqueId().toString())){
            return;
        }
        HandlerList.unregisterAll(ModifyInventoryClick.this);
    }

}
