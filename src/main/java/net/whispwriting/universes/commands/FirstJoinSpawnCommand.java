package net.whispwriting.universes.commands;

import net.whispwriting.universes.files.SpawnFile;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FirstJoinSpawnCommand implements CommandExecutor {

    private SpawnFile spawnFile;

    public FirstJoinSpawnCommand(SpawnFile spawnFile){
        this.spawnFile = spawnFile;
    }

    /**
     * Command to set a location for players to spawn at the first time they join the server
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only a player may use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("Universes.usetspawn")){
            player.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }

        Location loc = player.getLocation();
        spawnFile.get().set("x", loc.getX());
        spawnFile.get().set("y", loc.getY());
        spawnFile.get().set("z", loc.getZ());
        spawnFile.get().set("pitch", loc.getPitch());
        spawnFile.get().set("yaw", loc.getYaw());
        spawnFile.get().set("world", loc.getWorld().getName());
        spawnFile.save();
        player.sendMessage(ChatColor.GREEN + "First join spawn has been set.");
        return true;
    }
}
