package net.whispwriting.universes.utils;

import net.whispwriting.universes.Universes;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class InventoryManagement {

    public static void inventoryManagement(World fromWorld, World toWorld, Player spigotPlayer, Universes plugin){
        Universe fromUniverse = plugin.universes.get(fromWorld.getName());
        UniversePlayer player = plugin.onlinePlayers.get(spigotPlayer.getName());
        player.saveInventory(fromUniverse);
        player.storeInventory(fromUniverse);

        Universe toUniverse = plugin.universes.get(toWorld.getName());
        if (plugin.inventoryGrouping){
            if (!fromUniverse.name().equals(toUniverse.name())){
                clearEffects(player, plugin);
                loadInventory(player, toUniverse);
            }
        }else{
            clearEffects(player, plugin);
            loadInventory(player, toUniverse);
        }
    }

    public static void loadInventory(UniversePlayer player, Universe toUniverse){
        boolean success = player.loadInventory(toUniverse);
        if (!success){
            player.buildInventory(toUniverse);
            success = player.loadInventory(toUniverse);
            if (!success)
                player.clearInventory(toUniverse);
        }
    }

    public static void statManagement(World fromWorld, World toWorld, Player spigotPlayer, Universes plugin){
        String fromWorldName = fromWorld.getName();
        UniversePlayer player = plugin.onlinePlayers.get(spigotPlayer.getName());
        player.saveStats(plugin.universes.get(fromWorldName));
        String toWorldName = toWorld.getName();
        player.loadStats(plugin.universes.get(toWorldName));
        player.storeStats();
    }

    public static void clearEffects(UniversePlayer player, Universes plugin){
        if (plugin.removeEffectsOnWorldChange) {
            if (!player.spigotPlayer().getActivePotionEffects().isEmpty()){
                for (PotionEffect effect : player.spigotPlayer().getActivePotionEffects())
                    player.spigotPlayer().removePotionEffect(effect.getType());
            }
        }
    }
}
