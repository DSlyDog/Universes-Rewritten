package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfoCommand implements CommandExecutor {

    private Universes plugin;

    public InfoCommand(Universes plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Universes.info")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command");
            return true;
        }

        if (args.length < 1){
            sender.sendMessage(ChatColor.RED + "Please specify a world");
            return true;
        }

        if (plugin.universes.containsKey(args[0])){
            sender.sendMessage(plugin.universes.get(args[0]) + "");
        }else{
            sender.sendMessage(ChatColor.RED + "Could not find a world with that name");
        }
        return true;
    }
}
