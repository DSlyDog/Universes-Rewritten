package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorldSpawnItem extends GUIItem {

    public WorldSpawnItem(){
        Material[] materials = {Material.SPAWNER};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to change the spawn point of the world."));

        super.create(Utils.chat("&bWorld Spawn"), 1, false, 0, materials, lore1);
    }

    @Override
    public void onClick(Player player, Universe universe) {
        universe.setSpawn(player.getLocation());
        player.sendMessage(Utils.chat("&2Spawn point for world &a" + universe.name() + " &2has been set to where you stand."));
    }
}
