package net.whispwriting.universes.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class ListWorldsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("Universes.listworlds")){
            Collection<World> worldCollection = Bukkit.getWorlds();
            StringBuilder builder = new StringBuilder();
            int count = 0;
            for (World world : worldCollection){
                String name = world.getName();
                if (count == worldCollection.size()-1){
                    builder.append(name);
                }else{
                    builder.append(name).append(", ");
                }
                count++;
            }

            sender.sendMessage(ChatColor.GREEN + "Worlds:");
            sender.sendMessage(builder.toString());
            return true;
        }else{
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
    }
}
