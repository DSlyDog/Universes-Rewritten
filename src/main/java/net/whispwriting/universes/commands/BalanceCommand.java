package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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