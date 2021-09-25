package net.whispwriting.universes.events;

import me.clip.placeholderapi.PlaceholderAPI;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    private Universes plugin;

    public ChatEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event){
        if (plugin.prefixChat) {
            Universe universe = plugin.universes.get(event.getPlayer().getWorld().getName());
            String prefix = plugin.prefixFile.get().getString(universe.serverWorld().getName());
            if (prefix != null)
                prefix = prefix.replaceAll("&", "§");
            else
                prefix = "[" + universe.serverWorld().getName() + "]";
            String format = event.getFormat();
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null){
                format = prefix + format;
            }else {
                format = "%universes%" + format;
                format = PlaceholderAPI.setPlaceholders(event.getPlayer(), format);
            }
            event.setFormat(format);
        }
    }
}
