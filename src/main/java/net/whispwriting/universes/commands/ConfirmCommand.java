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
                GroupsFile groupsFile = new GroupsFile(plugin);
                groupsFile.get().set(world.getName(), null);
                groupsFile.save();
                Bukkit.getServer().unloadWorld(world, true);
                File file = new File(Bukkit.getWorldContainer() + "/"+worldName);
                deleteFolderContents(file);
                file.delete();
                plugin.sql.query("delete from universe where name='" + worldName + "'", "update");
                sender.sendMessage(ChatColor.GREEN + "World deleted.");
                plugin.universes.remove(worldName);
                plugin.players.remove(p);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "You have nothing to confirm.");
        return true;
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
