package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.ArrayList;
import java.util.List;

public class EnablePlayerLimitItem extends GUIItem {

    public EnablePlayerLimitItem(boolean currentValue){
        Material[] materials = {Material.IRON_HELMET};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&cPlayer limit is currently disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2Player limit is currently enabled."));

        super.create(Utils.chat("&bEnable Player Limit"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universe universe) {
        if (universe.isPlayerLimitEnabled()){
            universe.setPlayerLimitEnabled(false);
            player.sendMessage(Utils.chat("&cPlayer limit has been disabled in &4" + universe.name()));
        }else{
            universe.setPlayerLimitEnabled(true);
            player.sendMessage(Utils.chat("&2Player limit has been enabled in &a" + universe.name()));
        }
        incrementIndex();
    }
}
