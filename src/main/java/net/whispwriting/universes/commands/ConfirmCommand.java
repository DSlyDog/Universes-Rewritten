package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.utils.PlayersWhoCanConfirm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class ConfirmCommand implements CommandExecutor {

    private Universes plugin;

    public ConfirmCommand(Universes plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Universes.delete")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
        for (PlayersWhoCanConfirm p : plugin.players){
            if (p.getSender() == sender){
                sender.sendMessage(ChatColor.GREEN + "Deleting world.");
                String worldName = p.getWorld();
                World world = Bukkit.getWorld(worldName);
                for (Player player : world.getPlayers()){
                    player.teleport(plugin.universes.get(plugin.defaultWorld).spawn());
                }
                world.getEntities().clear();
                removeFromGroups(world);
                Bukkit.getServer().unloadWorld(world, true);
                File file = new File(Bukkit.getWorldContainer() + "/"+worldName);
                deleteFolderContents(file);
                file.delete();
                file = new File(Universes.plugin.getDataFolder() + "/worlds/"+worldName);
                deleteFolderContents(file);
                file.delete();
                sender.sendMessage(ChatColor.GREEN + "World deleted.");
                plugin.universes.remove(worldName);
                plugin.players.remove(p);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "You have nothing to confirm.");
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
