package net.whispwriting.universes.commands.tab_completers;

import net.whispwriting.universes.Universes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TeleportTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> worlds = new ArrayList<>();
        if (!Universes.plugin.usePerWorldTeleportPermissions){
            if (!commandSender.hasPermission("Universes.teleport")){
                return worlds;
            }else{
                for (World world : Bukkit.getWorlds()){
                    worlds.add(world.getName());
                }
                return worlds;
            }
        }else{
            for (World world : Bukkit.getWorlds()){
                if (commandSender.hasPermission("Universes.teleport." + world.getName()) || commandSender.hasPermission("Universes.teleport.*"))
                    worlds.add(world.getName());
            }
            return worlds;
        }
    }
}
