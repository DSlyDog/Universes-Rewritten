package net.whispwriting.universes.events;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.ArrayList;
import java.util.List;

public class WorldGenerationEvent implements Listener {

    private Universes plugin;

    public WorldGenerationEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldGen(WorldLoadEvent event){
        World world = event.getWorld();
        if (!plugin.universes.containsKey(world.getName()) && plugin.startupComplete) {
            GroupsFile groupsFile = new GroupsFile(plugin);
            groupsFile.get().set(world.getName() + ".group", world.getName());
            groupsFile.save();
            String name = world.getName();
            WorldSettingsFile worldSettings = new WorldSettingsFile(plugin, world.getName());
            worldSettings.setDefaults(world, world.getEnvironment().name().toLowerCase());
            worldSettings.save();
            List<String> blockedCommands = new ArrayList<>();
            Universe universe = new Universe(name, world, true, true, true,
                    GameMode.SURVIVAL, -1, world.getSpawnLocation(), name, true, false,
                    worldSettings, blockedCommands);
            plugin.universes.put(name, universe);
        }
    }
}
