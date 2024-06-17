package net.whispwriting.universes.gui.guis;

import net.whispwriting.universes.gui.GUI;
import net.whispwriting.universes.gui.GUIItem;
import net.whispwriting.universes.gui.items.world_settings.*;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class WorldSettingsUI implements GUI {
    private static Map<String, WorldSettingsUI> instances = new HashMap<>();
    private Universe universe;
    private List<GUIItem> items = new ArrayList<>();
    private int lastIndex = 0;
    private String name;
    private Inventory inventory;
    @Override
    public void build() {
        lastIndex = 0;
        GUIItem pvpItem = new PVPItem(universe.isAllowPvP());
        GUIItem spawnItem = new WorldSpawnItem();
        GUIItem animalsItem = new AllowAnimalsItem(universe.isAllowAnimals());
        GUIItem monsterItem = new AllowMonstersItem(universe.isAllowMonsters());
        GUIItem gameModeChange = new GameModeItem(universe.gameMode());
        GUIItem respawnWorldItem = new RespawnWorldItem(universe.respawnWorld());
        GUIItem playerLimit = new PlayerLimitItem(universe.maxPlayers());
        GUIItem playerLimitEnabled = new EnablePlayerLimitItem(universe.isPlayerLimitEnabled());
        GUIItem allowFlightItem = new AllowFlightItem(universe.isAllowFlight());
        GUIItem difficultyItem = new DifficultyItem(universe.getDifficulty());
        GUIItem blockCommandItem = new BlockCommandItem();
        GUIItem unblockCommandItem = new UnblockCommandItem();

        insertItem(pvpItem);
        insertItem(spawnItem);
        insertItem(animalsItem);
        insertItem(monsterItem);
        insertItem(gameModeChange);
        insertItem(respawnWorldItem);
        insertItem(playerLimit);
        insertItem(playerLimitEnabled);
        insertItem(allowFlightItem);
        insertItem(difficultyItem);
        insertItem(blockCommandItem);
        insertItem(unblockCommandItem);
    }

    @Override
    public void updateItem(int index) {
        GUIItem item = items.get(index);
        inventory.setItem(index, item.getAsItemStack());
    }

    @Override
    public Inventory get() {
        return inventory;
    }

    @Override
    public GUIItem getItem(int index) {
        return items.get(index);
    }

    private void insertItem(GUIItem item){
        items.add(item);
        inventory.setItem(lastIndex, item.getAsItemStack());
        lastIndex++;
    }

    public WorldSettingsUI(Universe universe){
        this.universe = universe;
        this.name = "§6§l" + universe.name() + "'s Settings";
        this.inventory = Bukkit.createInventory(null, SIZE.TWO.getValue(), name);
    }

    public static WorldSettingsUI getFor(Universe universe){
        if (!instances.containsKey(universe.name())){
            instances.put(universe.name(), new WorldSettingsUI(universe));
        }
        return instances.get(universe.name());
    }
}
