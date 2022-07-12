package net.whispwriting.universes.utils;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.generation.Generator;
import net.whispwriting.universes.utils.generation.UniversesGenerator;
import org.bukkit.*;
import org.bukkit.command.CommandSender;

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
            worldSettings.setDefaults(world, world.getEnvironment().name().toLowerCase(), null);
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

        Universes.plugin.config.writeCommentsFistTime();
        Universes.plugin.config.writeComments();
    }

    private static void standardStartup(File file, Universes plugin){
        WorldSettingsFile worldSettings = new WorldSettingsFile(plugin, file.getName());
        double x = worldSettings.get().getDouble("spawn.x");
        double y = worldSettings.get().getDouble("spawn.y");
        double z = worldSettings.get().getDouble("spawn.z");
        float yaw = (float) worldSettings.get().getDouble("spawn.yaw");
        float pitch = (float) worldSettings.get().getDouble("spawn.pitch");
        String respawnWorld = worldSettings.get().getString("respawnWorld");
        String environment = worldSettings.get().getString("environment");
        long seed = worldSettings.get().getLong("seed");
        String generatorName = worldSettings.get().getString("generator");
        GameMode gameMode = getGameModeFromString(worldSettings.get().getString("gameMode"));
        int playerLimit = worldSettings.get().getInt("playerLimit");
        boolean playerLimitEnabled = false;
        if (playerLimit >= 0)
            playerLimitEnabled = true;
        Difficulty difficulty = getDifficulty(worldSettings.get().getString("difficulty"), null);
        boolean allowPvP = worldSettings.get().getBoolean("allowPvP", true);
        boolean allowAnimals = worldSettings.get().getBoolean("allowAnimals", true);
        boolean allowMonsters = worldSettings.get().getBoolean("allowMonsters", true);
        boolean allowFlight = worldSettings.get().getBoolean("allowFlight", true);
        List<String> blockedCommands = worldSettings.get().getStringList("blockedCommands");

        Generator generator = new Generator(plugin, file.getName());
        generator.setEnvironment(getEnvironment(environment, null));
        if (generatorName == null || !generatorName.equals(""))
            generator.setGenerator(generatorName);
        generator.setSeed(seed);
        generator.createWorld();

        World world = generator.getWorld();
        world.setDifficulty(difficulty);
        Location spawn = new Location(world, x, y, z, yaw, pitch);

        Universe universe;
        if (plugin.inventoryGrouping){
            String groupName = plugin.groups.get(world.getName());
            if (groupName == null) {
                groupName = world.getName();
                Bukkit.getLogger().log(Level.WARNING, "Grouping is enabled but " + world.getName() + " does not " +
                        "appear to have an associated group in the groups.yml. To prevent errors, the world name has " +
                        "been assigned as its group. You should consider adding this world to the groups.yml");
            }
            universe = new Universe(groupName, world, allowAnimals, allowMonsters, allowFlight, gameMode,
                    playerLimit, spawn, respawnWorld, allowPvP, playerLimitEnabled, worldSettings, blockedCommands);
        }else{
            universe = new Universe(file.getName(), world, allowAnimals, allowMonsters, allowFlight, gameMode,
                    playerLimit, spawn, respawnWorld, allowPvP, playerLimitEnabled, worldSettings, blockedCommands);
        }

        plugin.universes.put(file.getName(), universe);
    }

    public static World constructWorld(UniversesGenerator universesGenerator, CommandSender sender, String environment, String type,
                                 String difficulty, String group, String seedString, String generator) {
        universesGenerator.setEnvironment(getEnvironment(environment, sender));
        universesGenerator.generateStructures(true);
        universesGenerator.setType(getWorldType(type, sender));
        if (!seedString.equals("")) {
            universesGenerator.setSeed(Long.parseLong(seedString));
        }
        if (!generator.equals(""))
            universesGenerator.setGenerator(generator);
        universesGenerator.createWorld();
        World world = universesGenerator.getWorld();
        if (universesGenerator.getWorld() != null) {
            WorldSettingsFile worldSettings = new WorldSettingsFile(Universes.plugin, world.getName());
            worldSettings.setDefaults(world, environment, generator);
            worldSettings.save();
            world.setDifficulty(getDifficulty(difficulty, sender));
            if (group.equals(""))
                group = world.getName();
            List<String> groupList = Universes.plugin.groupsFile.get().getStringList(group);
            groupList.add(world.getName());
            Universes.plugin.groupsFile.get().set(group, groupList);
            Universes.plugin.groupsFile.save();
            Universes.plugin.groups.put(world.getName(), group);
            String name = world.getName();
            List<String> blockedCommands = new ArrayList<>();
            Universe universe;
            if (Universes.plugin.inventoryGrouping) {
                universe = new Universe(group, world, true, true, true,
                        GameMode.SURVIVAL, -1, world.getSpawnLocation(), name, true, false,
                        worldSettings, blockedCommands);
                universe.save();
            }else{
                universe = new Universe(name, world, true, true, true,
                        GameMode.SURVIVAL, -1, world.getSpawnLocation(), name, true, false,
                        worldSettings, blockedCommands);
                universe.save();
            }
            Universes.plugin.universes.put(name, universe);
            sender.sendMessage(ChatColor.GREEN + "World successfully created.");
            return world;
        }else {
            sender.sendMessage(ChatColor.RED + "World creation failed.");
            return null;
        }
    }

    private static World.Environment getEnvironment(String arg, CommandSender sender) {
        arg = arg.toLowerCase();
        switch (arg){
            case "normal":
                return World.Environment.NORMAL;
            case "nether":
                return World.Environment.NETHER;
            case "end":
                return World.Environment.THE_END;
            default:
                if (sender != null)
                    sender.sendMessage(ChatColor.RED + "Invalid environment, defaulting to normal world.");
                return World.Environment.NORMAL;
        }
    }

    private static Difficulty getDifficulty(String arg, CommandSender sender) {
        arg = arg.toLowerCase();
        switch (arg){
            case "peaceful":
                return Difficulty.PEACEFUL;
            case "easy":
                return Difficulty.EASY;
            case "normal":
                return Difficulty.NORMAL;
            case "hard":
                return Difficulty.HARD;
            default:
                if (sender != null)
                    sender.sendMessage(ChatColor.RED + "Invalid difficulty, defaulting to normal difficulty.");
                return Difficulty.NORMAL;
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

    private static WorldType getWorldType(String arg, CommandSender sender) {
        arg = arg.toLowerCase();
        switch (arg){
            case "amplified":
                return WorldType.AMPLIFIED;
            case "flat":
                return WorldType.FLAT;
            case "large_biomes":
                return WorldType.LARGE_BIOMES;
            case "normal":
                return WorldType.NORMAL;
            default:
                if (sender != null)
                    sender.sendMessage(ChatColor.RED + "Invalid world type, defaulting to normal world.");
                return WorldType.NORMAL;
        }
    }
}
