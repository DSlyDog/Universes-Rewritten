package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.PlayersWhoCanConfirm;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CancelCommand implements CommandExecutor {

    private Universes plugin;

    public CancelCommand(Universes pl){
        plugin = pl;
    }

    /**
     * Command to cancel an initiated world deletion
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Universes.delete")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
        for (Object o : plugin.players){
            PlayersWhoCanConfirm p = (PlayersWhoCanConfirm) o;
            if (p.getSender() == sender){
                sender.sendMessage(ChatColor.RED + "World deletion canceled.");
                plugin.players.remove(p);
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "You have nothing to cancel.");
        return true;
    }
}
