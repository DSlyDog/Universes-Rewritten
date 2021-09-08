package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.guis.Utils;
import net.whispwriting.universes.guis.WorldSettingsUI;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChangeRespawnWorld implements Listener {

    private String uuid;
    private Universe universeToModfy;
    private Universes plugin;

    public ChangeRespawnWorld(String uuid, Universes plugin, Universe universeToModify){
        this.uuid = uuid;
        this.universeToModfy = universeToModify;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event){
        event.setCancelled(true);
        String uid = event.getPlayer().getUniqueId().toString();
        if (!uid.equals(uuid)){
            event.setCancelled(false);
            return;
        }
        String message = event.getMessage();
        String[] messageArray = message.split(" ");
        if (messageArray[0].equals("cancel")){
            event.getPlayer().sendMessage(Utils.chat("&cChanging the respawn world has been canceled."));
            HandlerList.unregisterAll(ChangeRespawnWorld.this);
            return;
        }
        Universe universe = plugin.universes.get(messageArray[0]);
        if (universe == null){
            event.getPlayer().sendMessage(Utils.chat("&cCould not find a world by that name. Please try again, or say \"cancel\" to cancel."));
            return;
        }
        universeToModfy.setRespawnWorld(universe.serverWorld().getName());
        plugin.sql.query("update universe set respawnWorld='" + universe.serverWorld().getName() + "' where name='" +
                universeToModfy.serverWorld().getName() + "'", "update");
        event.getPlayer().sendMessage(Utils.chat("&2respawnWorld has been updated."));
        HandlerList.unregisterAll(ChangeRespawnWorld.this);
    }

}
