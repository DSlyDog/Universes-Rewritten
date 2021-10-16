package net.whispwriting.universes.commands;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.ConfigFile;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.WorldLoader;
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
                sender.sendMessage(ChatColor.GOLD + "/universecreate " + ChatColor.YELLOW + "<name> <environment> <world type> <difficulty> <group> <seed> <generator>");
                return true;
            }
            if (plugin.universes.containsKey(args[0])){
                sender.sendMessage(ChatColor.RED + "A world with that name already exists");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Starting creation of world " + ChatColor.DARK_GREEN + args[0]);
            UniversesGenerator universesGenerator = new UniversesGenerator(plugin, args[0]);
            World world = makeWorld(args[1], universesGenerator, sender, args);
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
                        makeWorld("nether", universesGeneratorNether, sender, args);
                    }
                }
            }
            if (hasEnds){
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    if (Universes.plugin.endPerOverworld) {
                        sender.sendMessage(ChatColor.GREEN + "Starting creation of corresponding end");
                        UniversesGenerator universesGeneratorEnd = new UniversesGenerator(plugin, args[0] + "_the_end");
                        makeWorld("end", universesGeneratorEnd, sender, args);
                    }
                }
            }
        }else{
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }

        return true;
    }

    private World makeWorld(String environment, UniversesGenerator universesGenerator, CommandSender sender, String[] args){
        if (args.length == 5)
            return WorldLoader.constructWorld(universesGenerator, sender, environment, args[2], args[3], args[4], null, null);
        else if (args.length == 6)
            return WorldLoader.constructWorld(universesGenerator, sender, environment, args[2], args[3], args[4], args[5], null);
        else if (args.length == 7)
            return WorldLoader.constructWorld(universesGenerator, sender, environment, args[2], args[3], args[4], args[5], args[6]);
        else
            return WorldLoader.constructWorld(universesGenerator, sender, environment, args[2], args[3], null, null, null);
    }
}
