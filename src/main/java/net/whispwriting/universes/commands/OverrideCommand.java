package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.OverridesInventoryClick;
import net.whispwriting.universes.gui.OverrideUI;
import net.whispwriting.universes.gui.guis.OverridesGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OverrideCommand implements CommandExecutor {

    private Universes plugin;

    public OverrideCommand(Universes plugin){
        this.plugin = plugin;
    }

    /**
     * Administrative command to toggle world setting overrides
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            OverridesGUI.getFor(player, plugin).build();
            player.sendMessage();
            player.openInventory(OverridesGUI.getFor(player, plugin).get());
            Bukkit.getPluginManager().registerEvents(new OverridesInventoryClick(plugin, player.getUniqueId().toString()), plugin);
            if (OverridesGUI.getFor(player, plugin).size() == 0){
                player.closeInventory();
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to use any overrides.");
            }
            return true;
        }else{
            sender.sendMessage(ChatColor.RED + "Only players may execute that command.");
            return true;
        }
    }
}
