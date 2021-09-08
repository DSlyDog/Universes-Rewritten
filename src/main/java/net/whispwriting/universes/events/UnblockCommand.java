package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class UnblockCommand implements Listener {

    private String uuid;
    private Universe universeToModfy;
    private Universes plugin;

    public UnblockCommand(String uuid, Universes plugin, Universe universeToModify){
        this.uuid = uuid;
        this.universeToModfy = universeToModify;
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if (event.getPlayer().getUniqueId().toString().equalsIgnoreCase(uuid)) {
            event.setCancelled(true);
            String message = event.getMessage();
            if(universeToModfy.unblockCommand("/" + message)) {
                event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Command unblocked.");
            }else {
                event.getPlayer().sendMessage(ChatColor.RED + "That command is not blocked.");
            }
            HandlerList.unregisterAll(UnblockCommand.this);
        }
    }
}
