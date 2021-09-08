package net.whispwriting.universes.commands;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.generation.UniversesGenerator;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportCommand implements CommandExecutor {

    private Universes plugin;

    public ImportCommand(Universes pl){
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("Universes.importworld")) {
            if (args.length != 4){
                sender.sendMessage(ChatColor.GOLD + "/universeimport " + ChatColor.YELLOW + "<name> <environment> <world type> <difficulty>");
                return true;
            }
            File worldDirectory = new File(Bukkit.getWorldContainer()+"/"+args[0]);
            if (worldDirectory.isDirectory()){
                File datFile = new File(worldDirectory + "/level.dat");
                File uidFile = new File(worldDirectory + "/uid.dat");
                if (datFile.exists() || uidFile.exists()){
                    sender.sendMessage(ChatColor.GREEN + "Starting import of world " + ChatColor.DARK_GREEN + args[0]);
                    UniversesGenerator universesGenerator = new UniversesGenerator(plugin, args[0]);
                    constructWorld(universesGenerator, sender, args[1], args[2], args[3]);
                    return true;
                }else{
                    sender.sendMessage(ChatColor.RED + "That is not a valid Minecraft Java Edition world.");
                    return true;
                }
            }else{
                sender.sendMessage(ChatColor.RED + "No world could be found by that name.");
                return true;
            }
        }else{
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
    }

    private World constructWorld(UniversesGenerator universesGenerator, CommandSender sender, String environment, String type, String difficulty) {
        universesGenerator.setEnvironment(getEnvironment(environment, sender));
        universesGenerator.generateStructures(true);
        universesGenerator.setType(getWorldType(type, sender));
        universesGenerator.createWorld();
        World world = universesGenerator.getWorld();
        if (world != null) {
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
            sender.sendMessage(ChatColor.GREEN + "World successfully imported.");
            return world;
        }else {
            sender.sendMessage(ChatColor.RED + "Importing world failed.");
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
