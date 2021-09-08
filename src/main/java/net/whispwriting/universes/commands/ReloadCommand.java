package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.ChatPrefixFile;
import net.whispwriting.universes.files.ConfigFile;
import net.whispwriting.universes.files.KitsFile;
import net.whispwriting.universes.files.SpawnFile;
import net.whispwriting.universes.utils.WorldLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private Universes plugin;

    public ReloadCommand(Universes plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Universes.reload")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }

        plugin.config = new ConfigFile(plugin);
        plugin.spawnFile = new SpawnFile(plugin);
        plugin.kitsFile = new KitsFile(plugin);
        plugin.prefixFile = new ChatPrefixFile(plugin);
        plugin.perWorldInventories = plugin.config.get().getBoolean("per-world-inventories");
        plugin.inventoryGrouping = plugin.config.get().getBoolean("per-world-inventory-grouping");
        plugin.perWorldStats = plugin.config.get().getBoolean("per-world-stats");
        plugin.useRespawnWorld = plugin.config.get().getBoolean("use-respawnWorld");
        plugin.saveLastLocOnDeath = plugin.config.get().getBoolean("save-location-on-death");
        plugin.trackLastLocation = plugin.config.get().getBoolean("track-previous-locations");
        plugin.prefixChat = plugin.config.get().getBoolean("prefix-chat");
        plugin.useFirstJoinSpawn = plugin.config.get().getBoolean("use-first-join-spawn");
        plugin.worldEntryPermissions = plugin.config.get().getBoolean("world-entry-permissions");
        plugin.hubOnJoin = plugin.config.get().getBoolean("rejoin-at-hub");
        plugin.toGroupOnRespawn = plugin.config.get().getBoolean("respawn-at-group-spawn");
        plugin.toEntryPortal = plugin.config.get().getBoolean("return-to-entry-portal");
        plugin.usePerWorldTeleportPermissions = plugin.config.get().getBoolean("use-per-world-teleport-permissions");
        WorldLoader.loadWorlds(plugin);
        sender.sendMessage(ChatColor.GREEN + "reloaded config.");
        return true;
    }
}
