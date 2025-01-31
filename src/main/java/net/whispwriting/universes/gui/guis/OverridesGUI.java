package net.whispwriting.universes.gui.guis;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.gui.GUI;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.items.overrides.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class OverridesGUI implements GUI {

    private static Map<UUID, OverridesGUI> instances = new HashMap<>();

    private Player player;
    private Universes plugin;
    private Map<String, GUIItem> items = new HashMap<>();
    private int lastIndex;
    private String name = "§2§lOverrides";
    private Inventory inventory = Bukkit.createInventory(null, SIZE.ONE.getValue(), name);

    @Override
    public void build() {
        items.clear();
        inventory.clear();
        lastIndex = 0;
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, player.getUniqueId().toString());;

        if (player.hasPermission("Universes.override.gamemode")) {
            boolean gameModeOverride = playerSettings.get().getBoolean("gameModeOverride");
            insertItem(new GameModeOverride(gameModeOverride));
        }

        if (player.hasPermission("Universes.override.fullworld")) {
            boolean canJoinFullWorlds = playerSettings.get().getBoolean("canJoinFullWorlds");
            insertItem(new FullWorldOverride(canJoinFullWorlds));
        }

        if (player.hasPermission("Universes.override.flight")) {
            boolean flightOverride = playerSettings.get().getBoolean("flightOverride");
            insertItem(new FlightOverride(flightOverride));
        }

        if (player.hasPermission("Universes.override.perworldinv")) {
            boolean perWorldInvOverride = playerSettings.get().getBoolean("perWorldInvOverride");
            insertItem(new PerWorldInventoriesOverride(perWorldInvOverride));
        }

        if (player.hasPermission("Universes.override.blockedCommands")) {
            boolean overrideBlockedCommands = playerSettings.get().getBoolean("blockedCommandsOverride");
            insertItem(new BlockedCommandsOverride(overrideBlockedCommands));
        }
    }

    @Override
    public Inventory get() {
        return inventory;
    }

    @Override
    public GUIItem getItem(int index) {
        return items.get(inventory.getItem(index).getItemMeta().getDisplayName());
    }

    @Override
    public void updateItem(int index) {
        GUIItem item = items.get(inventory.getItem(index).getItemMeta().getDisplayName());
        inventory.setItem(index, item.getAsItemStack());
    }

    public int size(){
        return items.size();
    }

    private OverridesGUI(Player player, Universes plugin){
        this.player = player;
        this.plugin = plugin;
    }

    private void insertItem(GUIItem item){
        items.put(item.getName(), item);
        inventory.setItem(lastIndex, item.getAsItemStack());
        lastIndex++;
    }

    public static OverridesGUI getFor(Player player, Universes plugin){
        if (!instances.containsKey(player.getUniqueId())){
            instances.put(player.getUniqueId(), new OverridesGUI(player, plugin));
        }
        return instances.get(player.getUniqueId());
    }
}
