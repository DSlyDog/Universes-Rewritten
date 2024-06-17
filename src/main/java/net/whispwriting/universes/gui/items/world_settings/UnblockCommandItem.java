package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.BlockCommand;
import net.whispwriting.universes.events.UnblockCommand;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UnblockCommandItem extends GUIItem {
    public UnblockCommandItem(){
        Material[] materials = {Material.SLIME_BALL};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to unblock a command."));

        super.create(Utils.chat("&bUnblock Command"), 1, false, 0, materials, lore1);
    }

    @Override
    public void onClick(Player player, Universe universe, Universes plugin) {
        player.closeInventory();
        if (universe.blockedCommands().size() == 0){
            player.sendMessage(ChatColor.RED + "There are no blocked commands in this world.");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<universe.blockedCommands().size(); i++){
            if (i == universe.blockedCommands().size() - 1)
                builder.append(universe.blockedCommands().get(i));
            else
                builder.append(universe.blockedCommands().get(i)).append("\n");
        }
        player.sendMessage(ChatColor.DARK_GREEN + "Blocked commands:\n" + ChatColor.GOLD + builder);
        Bukkit.getPluginManager().registerEvents(new UnblockCommand(player.getUniqueId().toString(), plugin, universe), plugin);
        player.sendMessage(Utils.chat("&2Please enter a command to unblock. Do not include the slash."));
    }
}
