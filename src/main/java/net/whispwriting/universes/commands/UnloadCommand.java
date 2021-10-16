package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

public class UnloadCommand implements CommandExecutor {

    private Universes plugin;

    public UnloadCommand(Universes plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Universes.unload")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
        if (args.length != 1){
            sender.sendMessage(ChatColor.GOLD + "/universeunload " + ChatColor.YELLOW + "<world name>");
            return true;
        }
        Universe universe = plugin.universes.get(args[0]);
        if (universe == null){
            sender.sendMessage(ChatColor.RED + "No world could be found by that name.");
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "Started unloading world.");
        File file = new File(Universes.plugin.getDataFolder() + "/worlds/"+universe.serverWorld().getName());
        deleteFolderContents(file);
        file.delete();
        removeFromGroups(universe.serverWorld());
        Bukkit.unloadWorld(universe.serverWorld(), true);
        sender.sendMessage(ChatColor.GREEN + "Unload complete.");
        return true;
    }

    private void removeFromGroups(World world){
        String groupName = Universes.plugin.groups.get(world.getName());
        List<String> group = Universes.plugin.groupsFile.get().getStringList(groupName);
        group.remove(world.getName());
        if (group.size() == 0)
            Universes.plugin.groupsFile.get().set(groupName, null);
        else
            Universes.plugin.groupsFile.get().set(groupName, group);
        Universes.plugin.groupsFile.save();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ur");
    }

    private void deleteFolderContents(File directory){
        String[] files = directory.list();
        for (String f : files){
            File current = new File(directory.getPath(), f);
            if (current.isDirectory()){
                deleteFolderContents(current);
                current.delete();
            }else{
                current.delete();
            }
        }
    }
}
