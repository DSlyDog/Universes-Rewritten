package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.UniversePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportEvent implements Listener {

    private Universes plugin;

    public TeleportEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL && event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (plugin.trackLastLocation) {
                World world = event.getFrom().getWorld();
                //System.out.println(world.getClass()); --CHECK CLASS
                Universe fromUniverse = plugin.universes.get(event.getFrom().getWorld().getName());
                Universe toUniverse = plugin.universes.get(event.getTo().getWorld().getName());
                if (!toUniverse.serverWorld().getName().equals(fromUniverse.serverWorld().getName())) {
                    Player player = event.getPlayer();
                    UniversePlayer uPlayer = plugin.onlinePlayers.get(player.getName());
                    uPlayer.savePreviousLocation(fromUniverse, player.getLocation());
                }
            }
        }

        Universe universe = plugin.universes.get(event.getTo().getWorld().getName());

        if (plugin.worldEntryPermissions){
            if (!event.getPlayer().hasPermission("Universes.universe." + universe.serverWorld().getName())){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.DARK_RED + "You do not have permission to enter that world");
                return;
            }
        }

        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, event.getPlayer().getUniqueId().toString());
        boolean canJoinFullWorlds = playerSettings.get().getBoolean("canJoinFullWorlds");
        if (!canJoinFullWorlds){
            if (universe.isAtMaxPlayers()){
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Sorry, that world is full.");
            }
        }
    }
}
