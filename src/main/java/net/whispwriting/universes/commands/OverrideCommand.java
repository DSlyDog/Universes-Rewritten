package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.OverridesInventoryClick;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.guis.OverrideUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OverrideCommand implements CommandExecutor {

    private Universes plugin;

    public OverrideCommand(Universes plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            OverrideUI.init();
            OverrideUI.i = -1;
            player.openInventory(OverrideUI.GUI(player, plugin));
            Bukkit.getPluginManager().registerEvents(new OverridesInventoryClick(plugin, player.getUniqueId().toString()), plugin);
            if (OverrideUI.i == -1){
                player.closeInventory();
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to use any overrides.");
            }
            return true;
        }else{
            sender.sendMessage(ChatColor.RED + "Only players may execute that command.");
            return true;
        }
    }
}
