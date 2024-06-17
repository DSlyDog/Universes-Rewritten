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

public class PerWorldInventoriesOverride extends GUIItem {

    public PerWorldInventoriesOverride(boolean currentValue){
        Material[] materials = {Material.CRAFTING_TABLE};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&cPerWorldInventoryOverride is disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2PerWorldInventoryOverride is enabled."));

        super.create(Utils.chat("&bPer World Inventories Override"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universes plugin) {
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, player.getUniqueId().toString());;
        boolean perWorldInvOverride = playerSettings.get().getBoolean("perWorldInvOverride");
        if (player.hasPermission("Universes.override.perworldinv")){
            if (perWorldInvOverride) {
                playerSettings.get().set("perWorldInvOverride", false);
                playerSettings.save();
                player.sendMessage(ChatColor.RED + "Per World Inventory Override has been disabled.");
            } else {
                playerSettings.get().set("perWorldInvOverride", true);
                playerSettings.save();
                player.sendMessage(ChatColor.DARK_GREEN + "Pe rWorld Inventory Override has been enabled.");
            }
        }else{
            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to change that setting.");
        }
        super.incrementIndex();
    }
}
