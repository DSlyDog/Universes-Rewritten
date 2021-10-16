package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.WorldBuilderHelper;
import net.whispwriting.universes.utils.generation.UniversesGenerator;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class ImportCommand implements CommandExecutor {

    private Universes plugin;
    private WorldBuilderHelper helper;

    public ImportCommand(Universes pl){
        plugin = pl;
        helper = new WorldBuilderHelper();
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
                    helper.makeWorld(args[1], universesGenerator, sender, args);
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
