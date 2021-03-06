package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.KitsInventoryClick;
import net.whispwriting.universes.files.ConfigFile;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.guis.KitUI;
import net.whispwriting.universes.guis.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class KitCommand implements CommandExecutor {

    private Universes plugin;
    public KitCommand(Universes pl){
        plugin = pl;
    }

    /**
     * Command to open the kits UI so a kit can be selected
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(Utils.chat("&cOnly players may execute that command."));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("Universes.kits")){
            player.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
        String worldName;
        boolean openUI = false;
        if (!plugin.perWorldKitGrouping) {
            worldName = player.getLocation().getWorld().getName();
            openUI = KitUI.init(worldName);
        }else{
            String world = player.getLocation().getWorld().getName();
            worldName = plugin.universes.get(world).name();
            openUI = KitUI.init(worldName);
        }
        if (openUI){
            Inventory kitUI = KitUI.GUI(player);
            if (kitUI != null) {
                Bukkit.getPluginManager().registerEvents(new KitsInventoryClick(plugin, player.getUniqueId().toString()), plugin);
                player.openInventory(kitUI);
            }else{
                player.sendMessage(Utils.chat("&cThere was an error opening the kits UI. Please report this to a staff member and tell them to look over the logs and console."));
                System.out.println(Utils.chat("[Universes] &cAttempt to open Kits UI in world or group, "+worldName+", failed. There is a kit listed that has not yet been created."));
            }
        }else{
            player.sendMessage(Utils.chat("&cThere was an error opening the kits UI. Please report this to a staff member and tell them to look over the logs and console."));
            System.out.println(Utils.chat("[Universes] &cAttempt to open Kits UI in world or group, "+worldName+", failed. You have exceeded the kits capacity. You may not create more than 54 kits per world."));
        }
        return true;
    }
}
