package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.SpawnFile;
import net.whispwriting.universes.utils.InventoryManagement;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.UniversePlayer;
import net.whispwriting.universes.utils.sql.SQLResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerChangeOnlineState implements Listener {

    private Universes plugin;

    public PlayerChangeOnlineState(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        UUID uuid = player.getUniqueId();
        UniversePlayer uPlayer = new UniversePlayer(uuid, player, plugin);
        plugin.onlinePlayers.put(name, uPlayer);
        plugin.universes.get(player.getWorld().getName()).incrementPlayerCount();
        uPlayer.buildStats();
        if (plugin.useEconomy)
            if (Universes.econ != null)
                uPlayer.buildBalances();
        uPlayer.buildPreviousLocations();
        InventoryManagement.loadInventory(uPlayer, plugin.universes.get(player.getWorld().getName()));
    }

    @EventHandler
    public void login(PlayerJoinEvent event){
        if (!event.getPlayer().hasPlayedBefore()){
            if (plugin.useFirstJoinSpawn){
                SpawnFile spawnFile = new SpawnFile(plugin);
                double x = spawnFile.get().getDouble("x");
                double y = spawnFile.get().getDouble("y");
                double z = spawnFile.get().getDouble("z");
                float pitch = (float) spawnFile.get().getDouble("pitch");
                float yaw = (float) spawnFile.get().getDouble("yaw");
                String worldStr = spawnFile.get().getString("world");
                Universe universe = plugin.universes.get(worldStr);
                World world = universe.serverWorld();
                Location loc = new Location(world, x, y, z, yaw, pitch);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        event.getPlayer().teleport(loc);
                    }
                };
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UniversePlayer uPlayer = plugin.onlinePlayers.remove(player.getName());
        plugin.universes.get(player.getWorld().getName()).decrementPlayerCount();
        uPlayer.saveInventory(plugin.universes.get(player.getWorld().getName()));
        uPlayer.storeInventory(plugin.universes.get(player.getWorld().getName()));
        uPlayer.saveStats(plugin.universes.get(player.getWorld().getName()));
        uPlayer.storeStats();
    }
}
