package net.whispwriting.universes.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "---------- " + ChatColor.YELLOW + "Universes Help " + ChatColor.GOLD + "----------");
        sender.sendMessage(ChatColor.GOLD + "/universehelp " + ChatColor.YELLOW + "shows the Universes help");
        if (sender.hasPermission("Universes.listworlds")){
            sender.sendMessage(ChatColor.GOLD + "/universelist " + ChatColor.YELLOW + "list available worlds");
        }
        if (sender.hasPermission("Universes.teleport")){
            sender.sendMessage(ChatColor.GOLD + "/universeteleport " + ChatColor.YELLOW + "teleport to other worlds");
        }
        if (sender.hasPermission("Universes.spawn")){
            sender.sendMessage(ChatColor.GOLD + "/universespawn " + ChatColor.YELLOW + "teleport to a world's spawn");
        }
        if (sender.hasPermission("Universes.createworld")) {
            sender.sendMessage(ChatColor.GOLD + "/universecreate " + ChatColor.YELLOW + "create a new world");
        }
        if (sender.hasPermission("Universes.importworld")) {
            sender.sendMessage(ChatColor.GOLD + "/universeimport " + ChatColor.YELLOW + "import an existing world");
        }
        if (sender.hasPermission("Universes.unload")){
            sender.sendMessage(ChatColor.GOLD + "/universeunload " + ChatColor.YELLOW + "unload an existing world");
        }
        if (sender.hasPermission("Universes.delete")) {
            sender.sendMessage(ChatColor.GOLD + "/universedelete " + ChatColor.YELLOW + "delete an existing world");
        }
        if (sender.hasPermission("Universes.modify")){
            sender.sendMessage(ChatColor.GOLD + "/universemodify " + ChatColor.YELLOW + "modify settings in an existing world");
        }
        if (sender.hasPermission("Universes.override.gamemode") || sender.hasPermission("Universes.override.fullworld") || sender.hasPermission("Universes.override.flight")){
            sender.sendMessage(ChatColor.GOLD + "/universeoverride " + ChatColor.YELLOW + "enable or disable overrides");
        }
        if (sender.hasPermission("Universes.usetspawn")){
            sender.sendMessage(ChatColor.GOLD + "/usetspawn " + ChatColor.YELLOW + "set the spawn point for first time players");
        }
        if (sender.hasPermission("Universes.convert")){
            sender.sendMessage(ChatColor.GOLD + "/universeconvert " + ChatColor.YELLOW + "Convert worlds and player inventories to the new storage system");
        }
        if (sender.hasPermission("Universes.info")){
            sender.sendMessage(ChatColor.GOLD + "/universeinfo " + ChatColor.YELLOW + "Display information about a given world");
        }
        if (sender.hasPermission("Universes.resetplayercount")){
            sender.sendMessage(ChatColor.GOLD + "/universeresetplayercount " + ChatColor.YELLOW + "Resets the total players to 0 and max players to the default of -1." +
                    "\nUsing this will kick all players in the world so that the counter displays the right numbers after reset.");
        }
        if (sender.hasPermission("Universes.reload")){
            sender.sendMessage(ChatColor.GOLD + "/universesreload " + ChatColor.YELLOW + "reload the plugin config.");
        }
        return true;
    }
}
