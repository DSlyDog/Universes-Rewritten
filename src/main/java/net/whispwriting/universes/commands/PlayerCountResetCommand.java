package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCountResetCommand implements TabExecutor {

    private Universes plugin;

    public PlayerCountResetCommand(Universes plugin){
        this.plugin = plugin;
    }

    /**
     * Command to reset the player count of a world when it bugs
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Universes.resetplayercount")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
        if (args.length < 1){
            sender.sendMessage(ChatColor.RED + "Please specify a world");
            return true;
        }

        if (plugin.universes.containsKey(args[0])){
            Universe universe = plugin.universes.get(args[0]);
            for (Player player : Bukkit.getWorld(args[0]).getPlayers()){
                player.kickPlayer(ChatColor.GREEN + "" + ChatColor.BOLD + "The player count for the world you are in " +
                        "is currently being reset.\nTo avoid incorrect readings, you have been forced to rejoin. Thank you.");
            }
            universe.resetTotalPlayers();
        }else{
            sender.sendMessage(ChatColor.RED + "Please specify a world");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1){
            List<String> worlds = new ArrayList<>();
            for (World world : Bukkit.getWorlds()){
                worlds.add(world.getName());
            }
            return worlds;
        }
        return null;
    }
}
