package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.BlockCommand;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BlockCommandItem extends GUIItem {

    public BlockCommandItem(){
        Material[] materials = {Material.BARRIER};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to block a command."));

        super.create(Utils.chat("&bBlock Command"), 1, false, 0, materials, lore1);
    }

    @Override
    public void onClick(Player player, Universe universe, Universes plugin) {
        player.closeInventory();
        Bukkit.getPluginManager().registerEvents(new BlockCommand(player.getUniqueId().toString(), plugin, universe), plugin);
        player.sendMessage(Utils.chat("&2Please enter a command to block. Do not include the slash."));
    }
}
