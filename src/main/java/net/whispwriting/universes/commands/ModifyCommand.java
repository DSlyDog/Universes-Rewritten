package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.ModifyInventoryClick;
import net.whispwriting.universes.gui.WorldSettingsUI_Old;
import net.whispwriting.universes.gui.guis.WorldSettingsUI;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public class ModifyCommand implements CommandExecutor {

    private Universes plugin;

    public ModifyCommand(Universes plugin){
        this.plugin = plugin;
    }

    /**
     * Command to open the modify menu and change world settings
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Bukkit.getPluginManager().isPluginEnabled("Universe-Spawnify"))
            return true;

        sender.sendMessage("Continuing to base cmd");
        if (sender.hasPermission("Universes.modify")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Universe universe = plugin.universes.get(player.getWorld().getName());
                WorldSettingsUI.getFor(universe).build();
                player.openInventory(WorldSettingsUI.getFor(universe).get());
                Bukkit.getPluginManager().registerEvents(new ModifyInventoryClick(universe, player.getUniqueId().toString(), plugin), plugin);
            } else {
                sender.sendMessage(ChatColor.RED + "Only players may execute that command");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
        }
        return true;
    }
}
