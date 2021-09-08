package net.whispwriting.universes.events;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PVPEvent implements Listener {

    private WorldSettingsFile worldSettings;
    private Universes plugin;

    public PVPEvent(Universes pl){
        plugin = pl;
    }


    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player){
            Player damager = (Player) event.getDamager();
            String worldName = damager.getLocation().getWorld().getName();
            if (event.getEntity() instanceof Player) {
                Universe universe = plugin.universes.get(worldName);
                if (!universe.isAllowPvP()) {
                    event.setCancelled(true);
                    damager.sendMessage(ChatColor.RED + "PVP is not allowed in this world.");
                }
            }
        }
    }

}
