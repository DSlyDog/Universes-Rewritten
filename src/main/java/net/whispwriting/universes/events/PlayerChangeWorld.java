package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.utils.InventoryManagement;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.UniversePlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class PlayerChangeWorld implements Listener {

    private Universes plugin;

    public PlayerChangeWorld(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onWorldChange(PlayerChangedWorldEvent event){
        Universe universe = plugin.universes.get(event.getPlayer().getWorld().getName());
        Universe fromUniverse = plugin.universes.get(event.getFrom().getName());
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, event.getPlayer().getUniqueId().toString());
        boolean perWorldInvOverride = playerSettings.get().getBoolean("perWorldInvOverride");
        if (plugin.perWorldInventories && !perWorldInvOverride) {
            InventoryManagement.inventoryManagement(event.getFrom(), event.getPlayer().getWorld(), event.getPlayer(), plugin);
            if (plugin.perWorldStats)
                InventoryManagement.statManagement(event.getFrom(), event.getPlayer().getWorld(), event.getPlayer(), plugin);
        }

        universe.incrementPlayerCount();
        fromUniverse.decrementPlayerCount();

        boolean gameModeOverride = playerSettings.get().getBoolean("gameModeOverride");
        if (!gameModeOverride){
            event.getPlayer().setGameMode(universe.gameMode());
        }

    }
}
