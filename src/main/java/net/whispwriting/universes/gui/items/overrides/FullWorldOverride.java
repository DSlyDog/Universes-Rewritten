package net.whispwriting.universes.gui.items.overrides;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FullWorldOverride extends GUIItem {

    public FullWorldOverride(boolean currentValue){
        Material[] materials = {Material.BEDROCK};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&cFullWorldOverride is disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2FullWorldOverride is enabled."));

        super.create(Utils.chat("&bFull World Override"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universes plugin) {
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, player.getUniqueId().toString());;
        boolean canJoinFullWorlds = playerSettings.get().getBoolean("canJoinFullWorlds");
        if (player.hasPermission("Universes.override.fullworld")){
            if (canJoinFullWorlds) {
                playerSettings.get().set("canJoinFullWorlds", false);
                playerSettings.save();
                player.sendMessage(ChatColor.RED + "Full World Override has been disabled.");
            } else {
                playerSettings.get().set("canJoinFullWorlds", true);
                playerSettings.save();
                player.sendMessage(ChatColor.DARK_GREEN + "Full World Override has been enabled.");
            }
        }else{
            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to change that setting.");
        }
        super.incrementIndex();
    }
}
