package net.whispwriting.universes.utils;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.generation.Generator;
import org.bukkit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static org.bukkit.Difficulty.*;

public class WorldLoader {

    public static void loadWorlds(Universes plugin){
        File worldDir = new File(plugin.getDataFolder() + "/worlds");
        if (!worldDir.exists()){
            Bukkit.getLogger().log(Level.INFO, "[Universes] Running first time setup...");
            firstTimeStartup(plugin, worldDir);
            Bukkit.getLogger().log(Level.INFO, "[Universes] First time setup complete");
        }else{
            for (String group : plugin.groupsFile.get().getConfigurationSection("").getKeys(false)){
                for (String world : plugin. groupsFile.get().getStringList(group)){
                    plugin.groups.put(world, group);
                }
            }
            File[] worlds = worldDir.listFiles();
            for (File world : worlds){
                standardStartup(world, plugin);
            }
        }
    }

    private static void firstTimeStartup(Universes plugin, File worldDir){
        boolean isFolderCreated = worldDir.mkdir();
        if (!isFolderCreated) {
            Bukkit.getLogger().log(Level.SEVERE, "[Universes] Could not create worlds directory");
            return;
        }
        for (World world : Bukkit.getWorlds()){
            WorldSettingsFile worldSettings = new WorldSettingsFile(plugin, world.getName());
            worldSettings.setDefaults(world, world.getEnvironment().name().toLowerCase());
            worldSettings.save();
            world.setDifficulty(EASY);
            Universe universe = new Universe(world.getName(), world, true, true, true,
                    GameMode.SURVIVAL, -1, world.getSpawnLocation(), world.getName(), true,
                    false, worldSettings, new ArrayList<>());
            plugin.universes.put(world.getName(), universe);
        }
        GroupsFile groups = new GroupsFile(plugin);
        groups.setDefaults();
        groups.save();
    }

    private static void standardStartup(File file, Universes plugin){
        WorldSettingsFile worldSettings = new WorldSettingsFile(plugin, file.getName());
        WorldCreator creator = new WorldCreator(file.getName());
        World world = creator.createWorld();
        double x = worldSettings.get().getDouble("spawn.x");
        double y = worldSettings.get().getDouble("spawn.y");
        double z = worldSettings.get().getDouble("spawn.z");
        float yaw = (float) worldSettings.get().getDouble("spawn.yaw");
        float pitch = (float) worldSettings.get().getDouble("spawn.pitch");
        Location spawn = new Location(world, x, y, z, yaw, pitch);
        String respawnWorld = worldSettings.get().getString("respawnWorld");
        String environment = worldSettings.get().getString("environment");
        GameMode gameMode = getGameModeFromString(worldSettings.get().getString("gameMode"));
        int playerLimit = worldSettings.get().getInt("playerLimit");
        boolean playerLimitEnabled = false;
        if (playerLimit >= 0)
            playerLimitEnabled = true;
        Difficulty difficulty = getDifficulty(worldSettings.get().getString("difficulty"));
        world.setDifficulty(difficulty);
        boolean allowPvP = worldSettings.get().getBoolean("allowPvP", true);
        boolean allowAnimals = worldSettings.get().getBoolean("allowAnimals", true);
        boolean allowMonsters = worldSettings.get().getBoolean("allowMonsters", true);
        boolean allowFlight = worldSettings.get().getBoolean("allowFlight", true);
        List<String> blockedCommands = worldSettings.get().getStringList("blockedCommands");

        Generator generator = new Generator(plugin, file.getName());
        generator.setEnvironment(getEnvironment(environment));
        generator.createWorld();

        Universe universe;
        if (plugin.inventoryGrouping){
            String groupName = plugin.groups.get(world.getName());
            universe = new Universe(groupName, world, allowAnimals, allowMonsters, allowFlight, gameMode,
                    playerLimit, spawn, respawnWorld, allowPvP, playerLimitEnabled, worldSettings, blockedCommands);
        }else{
            universe = new Universe(file.getName(), world, allowAnimals, allowMonsters, allowFlight, gameMode,
                    playerLimit, spawn, respawnWorld, allowPvP, playerLimitEnabled, worldSettings, blockedCommands);
        }

        plugin.universes.put(file.getName(), universe);
    }

    private static World.Environment getEnvironment(String environment){
        switch (environment){
            case "normal":
                return World.Environment.NORMAL;
            case "nether":
                return World.Environment.NETHER;
            case "end":
                return World.Environment.THE_END;
            default:
                return World.Environment.NORMAL;
        }
    }

    private static Difficulty getDifficulty(String arg) {
        arg = arg.toLowerCase();
        switch (arg){
            case "peaceful":
                return PEACEFUL;
            case "easy":
                return EASY;
            case "normal":
                return NORMAL;
            case "hard":
                return HARD;
            default:
                return EASY;
        }
    }

    private static GameMode getGameModeFromString(String gameModeString) {
        switch (gameModeString){
            case "adventure":
                return GameMode.ADVENTURE;
            case "creative":
                return GameMode.CREATIVE;
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return GameMode.SURVIVAL;
        }
    }
}
