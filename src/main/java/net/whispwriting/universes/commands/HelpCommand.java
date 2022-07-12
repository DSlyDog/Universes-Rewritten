package net.whispwriting.universes.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {
    /**
     * Command that lists all the commands with their descriptions
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "---------- " + ChatColor.YELLOW + "Universes Help " + ChatColor.GOLD + "----------");
        sender.sendMessage(ChatColor.GOLD + "/universehelp " +
                ChatColor.YELLOW + "\nshows the Universes help");
        if (sender.hasPermission("Universes.listworlds")){
            sender.sendMessage(ChatColor.GOLD + "/universelist " +
                    ChatColor.YELLOW + "\nlist available worlds");
        }
        if (sender.hasPermission("Universes.teleport")){
            sender.sendMessage(ChatColor.GOLD + "/universeteleport <world_name>" +
                    ChatColor.YELLOW + "\nteleport to other worlds, returning to previous location");
        }
        if (sender.hasPermission("Universes.spawn")){
            sender.sendMessage(ChatColor.GOLD + "/universespawn <world_name>" +
                    ChatColor.YELLOW + "\nteleport to a world's spawn");
        }
        if (sender.hasPermission("Universes.createworld")) {
            sender.sendMessage(ChatColor.GOLD + "/universecreate <world_name> <environment> <type> <difficulty> [seed] [generator]" +
                    ChatColor.YELLOW + "\ncreate a new world");
        }
        if (sender.hasPermission("Universes.importworld")) {
            sender.sendMessage(ChatColor.GOLD + "/universeimport <world_name> <environment> <type> <difficulty>" +
                    ChatColor.YELLOW + "\nimport an existing world");
        }
        if (sender.hasPermission("Universes.unload")){
            sender.sendMessage(ChatColor.GOLD + "/universeunload <world_name>" +
                    ChatColor.YELLOW + "\nunload an existing world");
        }
        if (sender.hasPermission("Universes.delete")) {
            sender.sendMessage(ChatColor.GOLD + "/universedelete <world_name>" +
                    ChatColor.YELLOW + "\ndelete an existing world");
        }
        if (sender.hasPermission("Universes.modify")){
            sender.sendMessage(ChatColor.GOLD + "/universemodify " +
                    ChatColor.YELLOW + "\nopens the Universe Modify GUI");
        }
        if (sender.hasPermission("Universes.override.gamemode") || sender.hasPermission("Universes.override.fullworld") || sender.hasPermission("Universes.override.flight")){
            sender.sendMessage(ChatColor.GOLD + "/universeoverride " +
                    ChatColor.YELLOW + "\nopens the overrides GUI");
        }
        if (sender.hasPermission("Universes.usetspawn")){
            sender.sendMessage(ChatColor.GOLD + "/usetspawn " +
                    ChatColor.YELLOW + "\nset the spawn point for first time players");
        }
        if (sender.hasPermission("Universes.economy.admin")){
            sender.sendMessage(ChatColor.GOLD + "/ueconomy <give:take:set> <playername> <amount>" +
                    ChatColor.YELLOW + "\nAdmin command for manipulation of player balances");
        }
        if (sender.hasPermission("Universes.economy.use")){
            sender.sendMessage(ChatColor.GOLD + "/ubalance" +
                    ChatColor.YELLOW + "\nCheck your current balance");
        }
        if (sender.hasPermission("Universes.convert")){
            sender.sendMessage(ChatColor.GOLD + "/universeconvert " +
                    ChatColor.YELLOW + "\nConvert worlds and player inventories to the new storage system");
        }
        if (sender.hasPermission("Universes.info")){
            sender.sendMessage(ChatColor.GOLD + "/universeinfo <world_name>" +
                    ChatColor.YELLOW + "\nDisplay information about a given world");
        }
        if (sender.hasPermission("Universes.resetplayercount")){
            sender.sendMessage(ChatColor.GOLD + "/universeresetplayercount <world_name>" +
                    ChatColor.YELLOW + "\nResets the total players to 0 and max players to the default of -1." +
                    "\nUsing this will kick all players in the world so that the counter displays the right numbers after reset.");
        }
        if (sender.hasPermission("Universes.reload")){
            sender.sendMessage(ChatColor.GOLD + "/universesreload " +
                    ChatColor.YELLOW + "\nreload the plugin config.");
        }
        return true;
    }
}
