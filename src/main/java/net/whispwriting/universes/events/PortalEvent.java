package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PortalEvent implements Listener {

    private Universes plugin;

    public PortalEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event){
        Universe universe = plugin.universes.get(event.getTo().getWorld().getName());
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, event.getPlayer().getUniqueId().toString());
        boolean canJoinFullWorlds = playerSettings.get().getBoolean("canJoinFullWorlds");
        if (plugin.worldEntryPermissions && !plugin.netherPerOverworld){
            if (!event.getPlayer().hasPermission("Universes.universe." + universe.serverWorld().getName())){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.DARK_RED + "You do not have permission to enter that world");
                return;
            }
        }
        if (!canJoinFullWorlds){
            if (universe.isAtMaxPlayers()){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Sorry, that world is full.");
            }
        }

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL){
            //endPortal(event);
        }
    }

    public void endPortal(PlayerPortalEvent event){
        if (event.getFrom().getWorld().getName().contains("_the_end")){
            if (event.getPlayer().getBedSpawnLocation() != null)
                event.setTo(event.getPlayer().getBedSpawnLocation());
            else
                event.setTo(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
    }
}
