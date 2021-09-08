package net.whispwriting.universes.commands;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.*;
import net.whispwriting.universes.utils.Serializer;
import net.whispwriting.universes.utils.Universe;
import net.whispwriting.universes.utils.WorldLoaderOld;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

import static org.bukkit.Difficulty.*;

public class ConvertCommand implements CommandExecutor {

    private Universes plugin;
    private List<String> groupList = new ArrayList<>();
    private Serializer serializer;
    private Map<UUID, JsonObject> playerStats = new HashMap<>();

    public ConvertCommand(Universes plugin){
        this.plugin = plugin;
        serializer = new Serializer(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        WorldLoaderOld.loadWorlds(plugin);
        if (sender.hasPermission("Universes.convert")) {
            for (Map.Entry<String, Universe> universeEntry : plugin.universes.entrySet()){
                Universe universe = universeEntry.getValue();
                WorldSettingsFile worldSettingsFile = new WorldSettingsFile(plugin, universe.serverWorld().getName());
                worldSettingsFile.updateValues(universe.gameMode(), universe.spawn(), universe.respawnWorld(),
                        universe.maxPlayers(), universe.isAllowAnimals(), universe.getDifficulty(), universe.isAllowMonsters(),
                        universe.isAllowFlight(), universe.isAllowPvP(), universe.blockedCommands());
                worldSettingsFile.save();
            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()){
                        for (World world : Bukkit.getWorlds()){
                            ItemStack[] inventory = serializer.deserialize(player.getUniqueId(), world.getName(), "inventory");
                            ItemStack[] enderchest = serializer.deserialize(player.getUniqueId(), world.getName(), "enderchest");
                            itemStore(inventory, "inventory", player, world.getName());
                            itemStore(enderchest, "enderchest", player, world.getName());
                        }
                    }
                }

                private void itemStore(ItemStack[] inventory, String type, OfflinePlayer player, String world){
                    List<String> serializedItems = new ArrayList<>();
                    for (ItemStack item : inventory){
                        String itemString = serializer.itemStackToString(item);
                        serializedItems.add(itemString);
                    }
                    PlayerInventoryFile playerInventory = new PlayerInventoryFile(plugin, player.getUniqueId().toString(), world, type);
                    playerInventory.get().set("inventory", serializedItems);
                    playerInventory.save();
                }
            });

            thread.start();

            return true;
        }else{
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }
    }
}
