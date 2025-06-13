package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PVPItem extends GUIItem {

    public PVPItem(boolean currentValue){
        Material[] materials = {Material.DIAMOND_SWORD};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&cPVP is currently disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2PVP is currently enabled."));

        super.create(Utils.chat("&bPVP"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universe universe) {
        if (universe.isAllowPvP()){
            universe.allowPvP(false);
            player.sendMessage(Utils.chat("&cPVP is no longer allowed in &4" + universe.serverWorld().getName()));
        }else{
            universe.allowPvP(true);
            player.sendMessage(Utils.chat("&2PVP is now allowed in &a" + universe.serverWorld().getName()));
        }
        incrementIndex();
    }
}
