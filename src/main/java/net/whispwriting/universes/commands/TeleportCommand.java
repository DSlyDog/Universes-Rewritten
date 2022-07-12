package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.*;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.UniversePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TeleportCommand implements CommandExecutor {

    private Universes plugin;

    public TeleportCommand(Universes plugin){
        this.plugin = plugin;
    }

    /**
     * Command to teleport to a given world, returning to a previous location if one exists
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
            if (!plugin.usePerWorldTeleportPermissions) {
                if (!player.hasPermission("Universes.teleport")) {
                    player.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
                    return true;
                }
            }else{
                if (args.length == 1) {
                    if (!player.hasPermission("Universes.teleport." + args[0]) && !player.hasPermission("Universes.teleport.*")){
                        player.sendMessage(ChatColor.DARK_RED + "You do not have permission to teleport to that world");
                        return true;
                    }
                }else if (args.length >= 2){
                    if (!player.hasPermission("Universes.teleport." + args[0]) && !player.hasPermission("Universes.teleport.*")){
                        player.sendMessage(ChatColor.DARK_RED + "You may not teleport other players to worlds you do not have permission to access");
                        return true;
                    }
                }
            }

            if (args.length >= 2 && !player.hasPermission("Universes.teleport.others") && !player.getName().equals(args[1])){
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to teleport other players");
                return true;
            }

            if (args.length == 1) {
                Universe universe = plugin.universes.get(args[0]);
                if (plugin.worldEntryPermissions){
                    if (!player.hasPermission("Universes.universe." + universe.serverWorld().getName())){
                        player.sendMessage(ChatColor.DARK_RED + "You do not have permission to enter that world");
                        return true;
                    }
                }
                if (universe != null) {
                    teleport(player, universe);
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Could not find a world by that name.");
                    return true;
                }
            } if (args.length == 2){
                Player player2 = Bukkit.getPlayerExact(args[1]);
                if (player2 == null){
                    player.sendMessage(ChatColor.RED + "Could not find a player with that name.");
                    return true;
                }
                Universe universe = plugin.universes.get(args[0]);
                if (universe != null) {
                    teleport(player2, universe);
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Could not find a world by that name.");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + "/universeteleport " + ChatColor.YELLOW + "<world>");
                return true;
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Only players may use that command.");
            return true;
        }
    }

    public void teleport(Player player, Universe universe){
        if (plugin.trackLastLocation) {
            Universe fromUniverse = plugin.universes.get(player.getWorld().getName());
            UniversePlayer uPlayer = plugin.onlinePlayers.get(player.getName());
            uPlayer.savePreviousLocation(fromUniverse, player.getLocation());
            Location previous = uPlayer.loadPreviousLocation(universe);
            if (previous != null)
                player.teleport(previous);
            else
                player.teleport(universe.spawn());
            uPlayer.storePreviousLocations();
        }else{
            player.teleport(universe.spawn());
        }
    }
}
