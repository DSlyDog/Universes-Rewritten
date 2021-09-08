package net.whispwriting.universes.commands;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.ConfigFile;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.generation.UniversesGenerator;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CreateCommand implements CommandExecutor {

    private Universes plugin;

    public CreateCommand(Universes pl){
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("Universes.createworld")) {
            if (args.length < 4){
                sender.sendMessage(ChatColor.RED + "Too few arguments");
                sender.sendMessage(ChatColor.GOLD + "/universecreate " + ChatColor.YELLOW + "<name> <environment> <world type> <difficulty> <seed> <generator>");
                return true;
            }
            if (plugin.universes.containsKey(args[0])){
                sender.sendMessage(ChatColor.RED + "A world with that name already exists");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Starting creation of world " + ChatColor.DARK_GREEN + args[0]);
            UniversesGenerator universesGenerator = new UniversesGenerator(plugin, args[0]);
            World world;
            if (args.length == 5)
                world = constructWorld(universesGenerator, sender, args[1], args[2], args[3], args[4], null);
            else if (args.length == 6)
                world = constructWorld(universesGenerator, sender, args[1], args[2], args[3], args[4], args[5]);
            else
                world = constructWorld(universesGenerator, sender, args[1], args[2], args[3], null, null);
            Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
            boolean hasUNehters = false;
            boolean hasEnds = false;
            for (Plugin plugin : plugins){
                if (plugin.getName().equals("Universe-Nethers"))
                    hasUNehters = true;
                else if (plugin.getName().equals("Universe-Ends"))
                    hasEnds = true;
            }
            if (hasUNehters){
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    if (Universes.plugin.netherPerOverworld) {
                        sender.sendMessage(ChatColor.GREEN + "Starting creation of corresponding nether");
                        UniversesGenerator universesGeneratorNether = new UniversesGenerator(plugin, args[0] + "_nether");
                        constructWorld(universesGeneratorNether, sender, "nether", args[2], args[3], null, null);
                    }
                }
            }
            if (hasEnds){
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    if (Universes.plugin.endPerOverworld) {
                        sender.sendMessage(ChatColor.GREEN + "Starting creation of corresponding end");
                        UniversesGenerator universesGeneratorEnd = new UniversesGenerator(plugin, args[0] + "_the_end");
                        constructWorld(universesGeneratorEnd, sender, "end", args[2], args[3], null, null);
                    }
                }
            }
        }else{
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }

        return true;
    }

    private World constructWorld(UniversesGenerator universesGenerator, CommandSender sender, String environment, String type,
                                 String difficulty, String seedString, String generator) {
        universesGenerator.setEnvironment(getEnvironment(environment, sender));
        universesGenerator.generateStructures(true);
        universesGenerator.setType(getWorldType(type, sender));
        if (seedString != null) {
            try {
                universesGenerator.setSeed(Long.parseLong(seedString));
            }catch(NumberFormatException e){
                universesGenerator.setGenerator(seedString);
            }
        }
        if (generator != null)
            universesGenerator.setGenerator(generator);
        universesGenerator.createWorld();
        World world = universesGenerator.getWorld();
        if (universesGenerator.getWorld() != null) {
            WorldSettingsFile worldSettings = new WorldSettingsFile(plugin, world.getName());
            worldSettings.setDefaults(world, environment);
            worldSettings.save();
            world.setDifficulty(getDifficulty(difficulty, sender));
            GroupsFile groupsFile = new GroupsFile(plugin);
            groupsFile.get().set(world.getName() + ".group", world.getName());
            groupsFile.save();
            String name = world.getName();
            List<String> blockedCommands = new ArrayList<>();
            Universe universe = new Universe(name, world, true, true, true,
                    GameMode.SURVIVAL, -1, world.getSpawnLocation(), name, true, false,
                    worldSettings, blockedCommands);
            plugin.universes.put(name, universe);
            sender.sendMessage(ChatColor.GREEN + "World successfully created.");
            return world;
        }else {
            sender.sendMessage(ChatColor.RED + "World creation failed.");
            return null;
        }
    }

    private Difficulty getDifficulty(String arg, CommandSender sender) {
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
                sender.sendMessage(ChatColor.RED + "Invalid difficulty, defaulting to normal difficulty.");
                return Difficulty.NORMAL;
        }
    }

    private WorldType getWorldType(String arg, CommandSender sender) {
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
                sender.sendMessage(ChatColor.RED + "Invalid world type, defaulting to normal world.");
                return WorldType.NORMAL;
        }
    }

    private World.Environment getEnvironment(String arg, CommandSender sender) {
        arg = arg.toLowerCase();
        switch (arg){
            case "normal":
                return World.Environment.NORMAL;
            case "nether":
                return World.Environment.NETHER;
            case "end":
                return World.Environment.THE_END;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid environment, defaulting to normal world.");
                return World.Environment.NORMAL;
        }
    }
}
