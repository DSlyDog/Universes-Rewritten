package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.ChangeRespawnWorld;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RespawnWorldItem extends GUIItem {

    public RespawnWorldItem(String currentWorld){
        Material[] materials = {Material.OAK_SAPLING};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to change the respawn world."));
        lore1.add(Utils.chat("&2Respawn world is currently: " + currentWorld + "."));

        super.create(Utils.chat("&bWorld Spawn"), 1, false, 0, materials, lore1);
    }

    @Override
    public void onClick(Player player, Universe universe, Universes plugin) {
        player.closeInventory();
        Bukkit.getPluginManager().registerEvents(new ChangeRespawnWorld(player.getUniqueId().toString(), plugin, universe), plugin);
        player.sendMessage(Utils.chat("&2Please enter the name of the new respawn world."));
    }
}
