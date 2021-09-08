package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CommandPreprocessEvent implements Listener {

    private Universes plugin;

    public CommandPreprocessEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void commandPreProcess(PlayerCommandPreprocessEvent event){
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, event.getPlayer().getUniqueId().toString());
        boolean overrideBlockedCommands = playerSettings.get().getBoolean("blockedCommandsOverride");
        if (!overrideBlockedCommands) {
            String[] message = event.getMessage().split(" ");
            Universe universe = plugin.universes.get(event.getPlayer().getWorld().getName());
            if (universe.isCommandBlocked(message[0])) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "That command is not allowed in this world.");
            }
        }
    }
}
