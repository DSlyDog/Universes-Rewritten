package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnloadCommand implements CommandExecutor {

    private Universes plugin;

    public UnloadCommand(Universes plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Universes.unload")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
        if (args.length != 1){
            sender.sendMessage(ChatColor.GOLD + "/universeunload " + ChatColor.YELLOW + "<world name>");
            return true;
        }
        Universe universe = plugin.universes.get(args[0]);
        if (universe == null){
            sender.sendMessage(ChatColor.RED + "No world could be found by that name.");
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "Started unloading world.");
        Bukkit.unloadWorld(universe.serverWorld(), true);
        plugin.sql.query("delete from universe where name='" + universe.serverWorld().getName() + "'", "update");
        sender.sendMessage(ChatColor.GREEN + "Unload complete.");
        return true;
    }
}
