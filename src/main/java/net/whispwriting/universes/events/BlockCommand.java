package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BlockCommand implements Listener {

    private String uuid;
    private Universe universeToModfy;
    private Universes plugin;

    public BlockCommand(String uuid, Universes plugin, Universe universeToModify){
        this.uuid = uuid;
        this.universeToModfy = universeToModify;
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if (event.getPlayer().getUniqueId().toString().equals(uuid)) {
            String message = event.getMessage();
            event.setCancelled(true);
            if (universeToModfy.blockCommand("/" + message)) {
                event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Command blocked.");
            }else {
                event.getPlayer().sendMessage(ChatColor.RED + "That command is already blocked.");
            }
            HandlerList.unregisterAll(BlockCommand.this);
        }
    }
}
