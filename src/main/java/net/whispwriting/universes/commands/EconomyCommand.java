package net.whispwriting.universes.commands;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.economy.EconomyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class EconomyCommand implements CommandExecutor {

    private Player executor;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Universes.plugin.useEconomy){
            sender.sendMessage(ChatColor.RED + "The Universes economy system is currently disabled");
            return true;
        }

        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Only players may execute that command");
            return true;
        }

        Player player = (Player) sender;
        executor = player;

        if (!player.hasPermission("Universes.economy.admin")){
            player.sendMessage(ChatColor.DARK_RED + "You do not have access to that command");
            return true;
        }

        if (args.length == 3) {

            double balance;

            try{
                balance = Double.parseDouble(args[2]);
            }catch(NumberFormatException e){
                player.sendMessage(ChatColor.RED + "Values given must be numbers");
                return true;
            }

            switch (args[0]) {
                case "give":
                    giveMoney(args[1], balance);
                    break;
                case "take":
                    takeMoney(args[1], balance);
                    break;
                case "set":
                    setMoney(args[1], balance);
                    break;
            }
        }else if (args.length == 2 && args[0].equals("view")){
            viewBalance(args[1]);
        }else {
            executor.sendMessage(ChatColor.RED + "Missing arguments. /ueconomy <give:take:set:view> <player name> <amount:null>");
        }
        return true;
    }

    private void giveMoney(String name, double balance){
        Player player = Bukkit.getPlayer(name);
        String world = player.getWorld().getName();
        if (hasAccount(player, world)){
            EconomyResponse response = Universes.econ.depositPlayer(player, world, balance);
            if (response.transactionSuccess()){
                executor.sendMessage(ChatColor.GREEN + "Successfully gave " +
                        ChatColor.DARK_GREEN + Universes.econ.format(balance) + ChatColor.GREEN + " to " + name);
            }
        }
    }

    private void takeMoney(String name, double balance){
        Player player = Bukkit.getPlayer(name);
        String world = player.getWorld().getName();
        if (hasAccount(player, world)){
            EconomyResponse response = Universes.econ.withdrawPlayer(player, world, balance);
            if (response.transactionSuccess()){
                executor.sendMessage(ChatColor.GREEN + "Successfully took " +
                        ChatColor.DARK_GREEN + Universes.econ.format(balance) + ChatColor.GREEN + " from " + name);
            }else{
                executor.sendMessage(ChatColor.RED + name + " does not have sufficient funds");
            }
        }
    }

    private void setMoney(String name, double amount){
        double balance = viewBalance(name);
        takeMoney(name, balance);
        giveMoney(name, amount);
        executor.sendMessage(ChatColor.GREEN + name + " now has " + ChatColor.DARK_GREEN + Universes.econ.format(amount));
    }

    private double viewBalance(String name){
        Player player = Bukkit.getPlayer(name);
        String world = player.getWorld().getName();
        if (hasAccount(player, world)){
            double balance = Universes.econ.getBalance(player, world);
            executor.sendMessage(ChatColor.GREEN + "Balance: " + ChatColor.DARK_GREEN + Universes.econ.format(balance));
            return balance;
        }
        return 0;
    }

    private boolean hasAccount(Player player, String world){
        if (!Universes.econ.hasAccount(player, world)){
            Universes.econ.createPlayerAccount(player, world);
        }
        return true;
    }
}
