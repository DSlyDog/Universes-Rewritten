package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.UniversePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private Universes plugin;

    public SpawnCommand(Universes plugin){
        this.plugin = plugin;
    }

    /**
     * Command to teleport to a world's spawn point
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
                }
            }

            if (args.length >= 1) {
                Universe universe = plugin.universes.get(args[0]);
                if (universe != null) {
                    if (plugin.worldEntryPermissions){
                        if (!player.hasPermission("Universes.universe." + universe.serverWorld().getName())){
                            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to enter that world");
                            return true;
                        }
                    }
                    player.sendMessage(ChatColor.GREEN + "Teleporting...");
                    if (plugin.trackLastLocation) {
                        UniversePlayer uPlayer = plugin.onlinePlayers.get(player.getName());
                        Universe fromUniverse = plugin.universes.get(player.getWorld().getName());
                        uPlayer.savePreviousLocation(fromUniverse, player.getLocation());
                    }
                    player.teleport(universe.spawn());
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Could not find a world by that name.");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + "/universespawn " + ChatColor.YELLOW + "<world>");
                return true;
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Only players may use that command.");
            return true;
        }
    }
}
