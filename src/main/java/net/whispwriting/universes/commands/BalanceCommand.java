package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    /**
     * Command to check available balance
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Universes.plugin.useEconomy){
            sender.sendMessage(ChatColor.RED + "The Universes economy system is currently disabled");
            return true;
        }

        if (!sender.hasPermission("Universes.economy.use")){
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command");
            return true;
        }

        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players may execute that command");
            return true;
        }

        Player player = (Player) sender;
        String world = player.getWorld().getName();

        if (!Universes.econ.hasAccount(player, world))
            Universes.econ.createPlayerAccount(player, world);

        double balance = Universes.econ.getBalance(player, world);
        player.sendMessage(ChatColor.GREEN + "Balance: " + ChatColor.DARK_GREEN + Universes.econ.format(balance));
        return true;
    }
}
