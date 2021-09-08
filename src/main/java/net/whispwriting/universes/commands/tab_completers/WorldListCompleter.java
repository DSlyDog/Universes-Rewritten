package net.whispwriting.universes.commands.tab_completers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class WorldListCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> worlds = new ArrayList<>();
            for (World world : Bukkit.getWorlds()){
                worlds.add(world.getName());
            }
            return worlds;
        }
        return null;
    }
}
