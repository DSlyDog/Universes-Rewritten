package net.whispwriting.universes.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.*;
import net.whispwriting.universes.utils.*;
import net.whispwriting.universes.utils.sql.MySQL;
import net.whispwriting.universes.utils.sql.SQL;
import net.whispwriting.universes.utils.sql.SQLResult;
import net.whispwriting.universes.utils.sql.SQLite;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import static org.bukkit.Difficulty.*;

public class ConvertCommand implements CommandExecutor {

    private Universes plugin;
    private SerializerOld serializerOld;
    private Serializer serializer;
    private SQL sql;

    public ConvertCommand(Universes plugin){
        this.plugin = plugin;

        DatabaseFile databaseFile = new DatabaseFile(plugin); //old file storage setup initialization to retrieve data
        boolean useRemote = databaseFile.get().getBoolean("remote-database");
        if (useRemote)
            sql = new MySQL(databaseFile);
        else
            sql = new SQLite(plugin);

        serializerOld = new SerializerOld(plugin, sql);
        serializer = new Serializer(plugin);
    }

    /**
     * Command to convert stored data to the new configuration
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        WorldLoadEventHelper.getInstance().setConvertCommandExecuted(true);
        WorldLoaderOld.loadWorlds(plugin, sql);
        if (sender.hasPermission("Universes.convert")) {
            for (Map.Entry<String, Universe> universeEntry : plugin.universes.entrySet()){ //creating new world settings files for each world
                Universe universe = universeEntry.getValue();
                sender.sendMessage(ChatColor.GOLD + "Converting world settings for world " + ChatColor.YELLOW + universe.serverWorld().getName());
                WorldSettingsFile worldSettingsFile = new WorldSettingsFile(plugin, universe.serverWorld().getName());
                worldSettingsFile.updateValues(universe.gameMode(), universe.spawn(), universe.serverWorld().getEnvironment(),
                        universe.respawnWorld(), universe.maxPlayers(), universe.isAllowAnimals(), universe.getDifficulty(),
                        universe.isAllowMonsters(), universe.isAllowFlight(), universe.isAllowPvP(), universe.isPlayerLimitEnabled(),
                        universe.blockedCommands());
                worldSettingsFile.save();
            }

            //separate thread for the rest of the data conversion to prevent the server from thinking there is a hang and crashing
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sender.sendMessage(ChatColor.GOLD + "Converting player inventories...");
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()){ //converting inventory files for all players
                        sender.sendMessage(ChatColor.YELLOW + player.getName());
                        for (World world : Bukkit.getWorlds()){
                            ItemStack[] inventory = serializerOld.deserialize(player.getUniqueId(), player, world.getName(), "inventory");
                            ItemStack[] enderchest = serializerOld.deserialize(player.getUniqueId(), player, world.getName(), "enderchest");
                            itemStore(inventory, "inventory", player, world.getName());
                            itemStore(enderchest, "enderchest", player, world.getName());
                        }
                    }

                    sender.sendMessage(ChatColor.GOLD + "Converting player stats...");
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()){ //converting player stats (health, hunger, etc.) for all players
                        JsonObject stats = serializerOld.parseStats(player.getUniqueId());
                        PlayerDataFile data = new PlayerDataFile(plugin, player.getUniqueId().toString());
                        if (stats != null) {
                            data.get().set("stats", stats.toString());
                        }
                        data.save();
                    }

                    sender.sendMessage(ChatColor.GOLD + "Converting player settings files...");
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()){ //converting player settings for all players
                        sender.sendMessage(ChatColor.YELLOW + player.getName());
                        PlayerSettingsFileOld oldSettings = new PlayerSettingsFileOld(plugin, player.getUniqueId().toString());
                        boolean gameModeOverride = oldSettings.get().getBoolean("gameModeOverride");
                        boolean canJoinFullWorlds = oldSettings.get().getBoolean("canJoinFullWorlds");
                        boolean flightOverride = oldSettings.get().getBoolean("flightOverride");
                        boolean perWorldInvOverride = oldSettings.get().getBoolean("perWorldInvOverride");
                        boolean blockedCommandsOverride = oldSettings.get().getBoolean("blockedCommandsOverride");
                        PlayerSettingsFile newSettings = new PlayerSettingsFile(plugin, player.getUniqueId().toString());
                        newSettings.get().set("gameModeOverride", gameModeOverride);
                        newSettings.get().set("canJoinFullWorlds", canJoinFullWorlds);
                        newSettings.get().set("flightOverride", flightOverride);
                        newSettings.get().set("perWorldInvOverride", perWorldInvOverride);
                        newSettings.get().set("blockedCommandsOverride", blockedCommandsOverride);
                        newSettings.save();
                        oldSettings.delete();
                        buildPreviousLocations(player);
                    }
                    sender.sendMessage(ChatColor.GOLD + "Converting groups.yml");
                    plugin.groupsFile.update(sender);
                    WorldLoadEventHelper.getInstance().setConvertCommandExecuted(false);
                    sender.sendMessage(ChatColor.GREEN + "conversion complete");
                    sql.close();
                }

                /**
                 * Serializes the given inventory to store in the new configuration
                 * @param inventory The inventory to serialize
                 * @param type Player inventory or ender chest inventory
                 * @param player The player who owns the inventory
                 * @param world The world the inventory is located in
                 */
                private void itemStore(ItemStack[] inventory, String type, OfflinePlayer player, String world){
                    if (inventory != null) {
                        serializer.serialize(inventory, type, player.getUniqueId(), world);
                    }
                }

                /**
                 * Builds a json string from the database query to save in the new configuration
                 * @param player The player whose previous locations should be hooked up
                 */
                public void buildPreviousLocations(OfflinePlayer player) {
                    SQLResult result = sql.query("select * from playerdata where uuid='" + player.getUniqueId() + "'", "query");

                    try {
                        if (result.sqlResults().next()) {
                            String json = result.sqlResults().getString("previousLocations");
                            if (!json.equals("")) {
                                JsonParser parser = new JsonParser();
                                JsonObject previousLocations = parser.parse(json).getAsJsonObject();
                                PlayerDataFile settings = new PlayerDataFile(plugin, player.getUniqueId().toString());
                                settings.get().set("previousLocations", previousLocations.toString());
                                settings.save();
                            }
                        }
                    } catch (SQLException var4) {
                        var4.printStackTrace();
                    }

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
