package net.whispwriting.universes.gui.guis;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.gui.GUI;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.items.overrides.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class OverridesGUI implements GUI {

    private static Map<UUID, OverridesGUI> instances = new HashMap<>();

    private Player player;
    private Universes plugin;
    private List<GUIItem> items = new ArrayList<>();
    private int lastIndex = 0;
    private String name = "§2§lOverrides";
    private Inventory inventory = Bukkit.createInventory(null, SIZE.ONE.getValue(), name);
    @Override
    public void build() {
        lastIndex = 0;
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, player.getUniqueId().toString());;
        boolean gameModeOverride = playerSettings.get().getBoolean("gameModeOverride");
        boolean canJoinFullWorlds = playerSettings.get().getBoolean("canJoinFullWorlds");
        boolean flightOverride = playerSettings.get().getBoolean("flightOverride");
        boolean perWorldInvOverride = playerSettings.get().getBoolean("perWorldInvOverride");
        boolean overrideBlockedCommands = playerSettings.get().getBoolean("blockedCommandsOverride");

        GUIItem gameModeItem = new GameModeOverride(gameModeOverride);
        GUIItem fullWorldItem = new FullWorldOverride(canJoinFullWorlds);
        GUIItem flightItem = new FlightOverride(flightOverride);
        GUIItem perWorldInventoriesItem = new PerWorldInventoriesOverride(perWorldInvOverride);
        GUIItem blockedCommandsItem = new BlockedCommandsOverride(overrideBlockedCommands);

        if (player.hasPermission("Universes.override.gamemode"))
            insertItem(gameModeItem);

        if (player.hasPermission("Universes.override.fullworld"))
            insertItem(fullWorldItem);

        if (player.hasPermission("Universes.override.flight"))
            insertItem(flightItem);

        if (player.hasPermission("Universes.override.perworldinv"))
            insertItem(perWorldInventoriesItem);

        if (player.hasPermission("Universes.override.blockedCommands"))
            insertItem(blockedCommandsItem);
    }

    @Override
    public Inventory get() {
        return inventory;
    }

    @Override
    public GUIItem getItem(int index) {
        return items.get(index);
    }

    @Override
    public void updateItem(int index) {
        GUIItem item = items.get(index);
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
        items.add(item);
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
