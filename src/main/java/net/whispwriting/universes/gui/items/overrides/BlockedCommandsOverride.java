package net.whispwriting.universes.gui.items.overrides;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlockedCommandsOverride extends GUIItem {

    public BlockedCommandsOverride(boolean currentValue){
        Material[] materials = {Material.BARRIER};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&cBlocked Commands Override is disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2Blocked Commands Override is enabled."));

        super.create(Utils.chat("&bBlocked Commands Override"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universes plugin) {
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, player.getUniqueId().toString());;
        boolean overrideBlockedCommands = playerSettings.get().getBoolean("blockedCommandsOverride");
        if (player.hasPermission("Universes.override.blockedCommands")) {
            if (overrideBlockedCommands) {
                playerSettings.get().set("blockedCommandsOverride", false);
                playerSettings.save();
                player.sendMessage(ChatColor.RED + "Blocked Commands Override has been disabled.");
            } else {
                playerSettings.get().set("blockedCommandsOverride", true);
                playerSettings.save();
                player.sendMessage(ChatColor.DARK_GREEN + "Blocked Commands Override has been enabled.");
            }
        }else{
            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to change that setting.");
        }
        super.incrementIndex();
    }
}
