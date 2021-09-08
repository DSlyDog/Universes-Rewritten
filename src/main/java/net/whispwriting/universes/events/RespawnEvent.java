package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.UniversePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEvent implements Listener {

    private Universes plugin;

    public RespawnEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event){
        if (plugin.useRespawnWorld) {
            Universe universe = plugin.universes.get(event.getPlayer().getWorld().getName());
            Universe respawnUniverse = plugin.universes.get(universe.respawnWorld());
            event.setRespawnLocation(respawnUniverse.spawn());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if (plugin.trackLastLocation)
            if (plugin.saveLastLocOnDeath){
                Location location = event.getEntity().getLocation();
                Universe universe = plugin.universes.get(location.getWorld().getName());
                UniversePlayer player = plugin.onlinePlayers.get(((Player) event.getEntity()).getName());
                player.savePreviousLocation(universe, location);
                player.storePreviousLocations();
            }

    }
}
