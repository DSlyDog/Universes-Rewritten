package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.WorldBuilderHelper;
import net.whispwriting.universes.utils.WorldLoadEventHelper;
import net.whispwriting.universes.utils.generation.UniversesGenerator;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.UUID;

public class ImportCommand implements CommandExecutor {

    private Universes plugin;
    private WorldBuilderHelper helper;

    public ImportCommand(Universes pl){
        plugin = pl;
        helper = new WorldBuilderHelper();
    }

    /**
     * Command to import an existing world to Universes and the server
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("Universes.importworld")) {
            if (args.length < 4){
                sender.sendMessage(ChatColor.GOLD + "/universeimport " + ChatColor.YELLOW + "<name> <environment> <world type> <difficulty> <group> <seed> <generator>");
                return true;
            }
            if (plugin.universes.containsKey(args[0])){
                sender.sendMessage(ChatColor.RED + "A world with that name already exists.");
                return true;
            }
            File worldDirectory = new File(Bukkit.getWorldContainer()+"/"+args[0]);
            if (worldDirectory.isDirectory()){
                File datFile = new File(worldDirectory + "/level.dat");
                File uidFile = new File(worldDirectory + "/uid.dat");
                if (datFile.exists() || uidFile.exists()){
                    UUID uid = getUID(uidFile);
                    if (uid != null) {
                        for (Universe universe : plugin.universes.values()) {
                            if (universe.serverWorld().getUID().equals(uid)) {
                                sender.sendMessage(ChatColor.RED + "A duplicate world with the same 'uid.dat' file is currently imported: '" + universe.name() + "'.");
                                sender.sendMessage(ChatColor.RED + "Delete the 'uid.dat' file from the new world if you wish to import this duplicate world.");
                                return true;
                            }
                        }
                    }

                    sender.sendMessage(ChatColor.GREEN + "Starting import of world " + ChatColor.DARK_GREEN + args[0]);
                    UniversesGenerator universesGenerator = new UniversesGenerator(plugin, args[0]);
                    WorldLoadEventHelper.getInstance().setCreateCommandExecuted(true);
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

    private UUID getUID(File uidFile){
        UUID uid;
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(uidFile.toPath()))) {
            uid = new UUID(dis.readLong(), dis.readLong());
        } catch (Exception e) {
            return null;
        }
        return uid;
    }

}
