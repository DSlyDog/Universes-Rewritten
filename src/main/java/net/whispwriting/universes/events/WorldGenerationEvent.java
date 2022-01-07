package net.whispwriting.universes.events;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.WorldLoadEventHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WorldGenerationEvent implements Listener {

    private Universes plugin;

    public WorldGenerationEvent(Universes plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldGen(WorldLoadEvent event){
        if (WorldLoadEventHelper.getInstance().isCreateCommandExecuted()){
            WorldLoadEventHelper.getInstance().setCreateCommandExecuted(false);
            return;
        }
        World world = event.getWorld();
        if (!plugin.universes.containsKey(world.getName()) && plugin.startupComplete) {
            String name = world.getName();
            List<String> group = new ArrayList<>();
            group.add(name);
            Universes.plugin.groupsFile.get().set(name, group);
            Universes.plugin.groupsFile.save();
            WorldSettingsFile worldSettings = new WorldSettingsFile(plugin, world.getName());
            try {
                String gen = event.getWorld().getGenerator().getClass().getName();
                String[] potentials = gen.split("[.]");
                for (int i=0; i< potentials.length; i++){
                    if (Bukkit.getPluginManager().getPlugin(potentials[i]) != null){
                        worldSettings.setDefaults(world, world.getEnvironment().name().toLowerCase(), potentials[i]);
                        break;
                    }
                }
            }catch(NullPointerException e){
                worldSettings.setDefaults(world, world.getEnvironment().name().toLowerCase(), null);
            }
            worldSettings.save();
            List<String> blockedCommands = new ArrayList<>();
            Universe universe = new Universe(name, world, true, true, true,
                    GameMode.SURVIVAL, -1, world.getSpawnLocation(), name, true, false,
                    worldSettings, blockedCommands);
            universe.save();
            plugin.universes.put(name, universe);
        }
    }
}
