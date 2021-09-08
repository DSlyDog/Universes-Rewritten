package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlyEvent implements Listener {

    private Universes plugin;

    public FlyEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event){
        Universe universe = plugin.universes.get(event.getPlayer().getWorld().getName());
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, event.getPlayer().getUniqueId().toString());
        boolean flightOverride = playerSettings.get().getBoolean("flightOverride");
        if (flightOverride){
            return;
        }

        if (!universe.isAllowFlight()){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Flight is not allowed in that world.");
        }

    }

}
