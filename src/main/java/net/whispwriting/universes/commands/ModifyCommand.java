package net.whispwriting.universes.commands;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.ModifyInventoryClick;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.guis.WorldSettingsUI;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.sql.SQL;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;

public class ModifyCommand implements CommandExecutor {

    private Universes plugin;

    public ModifyCommand(Universes plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("Universes.modify")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.openInventory(WorldSettingsUI.GUI(player, plugin.universes.get(player.getWorld().getName()), plugin));
                Bukkit.getPluginManager().registerEvents(new ModifyInventoryClick(plugin, player.getUniqueId().toString()), plugin);
            } else {
                sender.sendMessage(ChatColor.RED + "Only players may execute that command");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
        }
        return true;
    }
}
