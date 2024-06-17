package net.whispwriting.universes.gui.items.world_settings;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.Utils;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllowFlightItem extends GUIItem {

    public AllowFlightItem(boolean currentValue){
        Material[] materials = {Material.ELYTRA};
        List<String> lore1 = new ArrayList<>();
        lore1.add(Utils.chat("&dClick to enable or disable."));
        lore1.add(Utils.chat("&caFlight is currently disabled."));
        List<String> lore2 = new ArrayList<>();
        lore2.add(Utils.chat("&dClick to enable or disable."));
        lore2.add(Utils.chat("&2Flight is currently enabled."));

        super.create(Utils.chat("&bAllow Flight"), 1, true, currentValue, materials, lore1, lore2);
    }

    @Override
    public void onClick(Player player, Universe universe, Universes plugin) {
        if (universe.isAllowFlight()) {
            universe.allowFlight(false);
            player.sendMessage(Utils.chat("&cFlight has been disabled in &4" + universe.name()));
            Collection<Player> players = universe.serverWorld().getPlayers();
            for (Player p : players) {
                if (p.isFlying()) {
                    PlayerSettingsFile playerSettingsFile = new PlayerSettingsFile(plugin, p.getUniqueId().toString());
                    ;
                    boolean flightOverride = playerSettingsFile.get().getBoolean("flightOverride");
                    if (!flightOverride) {
                        p.setFlying(false);
                        p.sendMessage(Utils.chat("&cFlying has been disabled in this world."));
                    }
                }
            }
        }else{
            universe.allowFlight(true);
            player.sendMessage(Utils.chat("&2Flight has been enabled in &a" + universe.name()));
        }
    }
}
