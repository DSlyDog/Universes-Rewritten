package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.ChangePlayerLimit;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerLimitItem extends GUIItem {

    public PlayerLimitItem(int currentLimit){
        Material[] materials = {Material.PLAYER_HEAD};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to change the player limit."));
        lore1.add(Utils.chat("&2Player limit is currently: " + currentLimit + "."));

        super.create(Utils.chat("&bPlayer Limit"), 1, false, 0, materials, lore1);
    }

    @Override
    public void onClick(Player player, Universe universe, Universes plugin) {
        player.closeInventory();
        Bukkit.getPluginManager().registerEvents(new ChangePlayerLimit(player.getUniqueId().toString(), plugin, universe), plugin);
        player.sendMessage(Utils.chat("&2Please enter a number for the new player limit."));
    }
}
