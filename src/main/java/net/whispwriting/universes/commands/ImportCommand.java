package net.whispwriting.universes.commands;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.WorldLoader;
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
            if (args.length < 4){
                sender.sendMessage(ChatColor.GOLD + "/universeimport " + ChatColor.YELLOW + "<name> <environment> <world type> <difficulty> <group> <seed> <generator>");
                return true;
            }
            File worldDirectory = new File(Bukkit.getWorldContainer()+"/"+args[0]);
            if (worldDirectory.isDirectory()){
                File datFile = new File(worldDirectory + "/level.dat");
                File uidFile = new File(worldDirectory + "/uid.dat");
                if (datFile.exists() || uidFile.exists()){
                    sender.sendMessage(ChatColor.GREEN + "Starting import of world " + ChatColor.DARK_GREEN + args[0]);
                    UniversesGenerator universesGenerator = new UniversesGenerator(plugin, args[0]);
                    if (args.length == 5)
                        WorldLoader.constructWorld(universesGenerator, sender, args[1], args[2], args[3], args[4], null, null);
                    else if (args.length == 6)
                        WorldLoader.constructWorld(universesGenerator, sender, args[1], args[2], args[3], args[4], args[5], null);
                    else if (args.length == 7)
                        WorldLoader.constructWorld(universesGenerator, sender, args[1], args[2], args[3], args[4], args[5], args[6]);
                    else
                        WorldLoader.constructWorld(universesGenerator, sender, args[1], args[2], args[3], null, null, null);
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
}
