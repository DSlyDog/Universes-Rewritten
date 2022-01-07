package net.whispwriting.universes.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerAccountFile;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.utils.sql.SQL;
import net.whispwriting.universes.utils.sql.SQLResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class UniversePlayer {

    private UUID uuid;
    private Player player;
    private Serializer serializer;
    private PlayerDataParser parser;
    private Map<String, ItemStack[]> inventories = new HashMap<>();
    private Map<String, ItemStack[]> enderchests = new HashMap<>();
    private JsonObject stats = new JsonObject();
    private JsonObject previousLocations = new JsonObject();
    private JsonObject bedSpawns = new JsonObject();

    public UniversePlayer(UUID uuid, Player player, Universes plugin){
        this.uuid = uuid;
        this.player = player;
        this.serializer = new Serializer(plugin);
        parser = new PlayerDataParser(plugin);
    }

    public Player spigotPlayer(){
        return player;
    }

    public World currentWorld(){
        return player.getWorld();
    }

    /* Player Inventory and EnderChest Management */
    public void storeInventory(Universe universe){
        ItemStack[] inventory = inventories.get(universe.name());
        ItemStack[] ecInventory = enderchests.get(universe.name());
        if (inventory != null) {
            serializer.serialize(inventory, "inventory", uuid, universe.name());
            serializer.serialize(ecInventory, "enderchest", uuid, universe.name());
        }
        else {
            serializer.serialize(player.getInventory().getContents(), "inventory", uuid, universe.name());
            serializer.serialize(player.getEnderChest().getContents(), "enderchest", uuid, universe.name());
        }
    }

    public void buildInventory(Universe universe){
        ItemStack[] inventory = serializer.deserialize(uuid, universe.name(), "inventory");
        ItemStack[] ecInventory = serializer.deserialize(uuid, universe.name(), "enderchest");
        if (inventory != null) {
            if (inventories.get(universe.name()) != null) {
                inventories.remove(universe.name());
                inventories.put(universe.name(), inventory);
                enderchests.remove(universe.name());
                enderchests.put(universe.name(), ecInventory);
            } else {
                inventories.put(universe.name(), inventory);
                enderchests.put(universe.name(), ecInventory);
            }
        }
    }

    public boolean loadInventory(Universe universe){
        ItemStack[] inventory = inventories.get(universe.name());
        ItemStack[] ecInventory = enderchests.get(universe.name());
        if (inventory == null){
            return false;
        }else{
            player.getInventory().setContents(inventory);
            player.getEnderChest().setContents(ecInventory);
            return true;
        }
    }

    public void saveInventory(Universe universe) {
        try {
            inventories.remove(universe.name());
            inventories.put(universe.name(), player.getInventory().getContents());
            enderchests.remove(universe.name());
            enderchests.put(universe.name(), player.getEnderChest().getContents());
        } catch(NullPointerException e) {
            inventories.put(universe.name(), player.getInventory().getContents());
            enderchests.put(universe.name(), player.getEnderChest().getContents());
        }
    }

    public void clearInventory(Universe universe) {
        player.getInventory().clear();
        player.getEnderChest().clear();
    }

    /* Stat Management */
    public void buildStats(){
        stats = parser.parseStats(uuid);
        if (stats == null){
            stats = new JsonObject();
        }
    }

    public void saveStats(Universe universe){
        JsonObject newStats = new JsonObject();
        newStats.addProperty("health", player.getHealth());
        newStats.addProperty("hunger", player.getFoodLevel());
        newStats.addProperty("saturation", player.getSaturation());
        newStats.addProperty("xpLevel", player.getLevel());
        newStats.addProperty("exp", player.getExp());

        JsonObject currentStats = stats.getAsJsonObject(universe.name());
        if (currentStats == null) {
            stats.add(universe.name(), newStats);
        }else {
            stats.remove(universe.name());
            stats.add(universe.name(), newStats);
        }
    }

    public void loadStats(Universe universe){
        JsonObject savedStats = stats.getAsJsonObject(universe.name());
        if (savedStats != null){
            player.setHealth(savedStats.get("health").getAsDouble());
            player.setFoodLevel(savedStats.get("hunger").getAsInt());
            player.setSaturation(savedStats.get("saturation").getAsFloat());
            player.setLevel(savedStats.get("xpLevel").getAsInt());
            player.setExp(savedStats.get("exp").getAsFloat());
        }else{
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setSaturation(5);
            player.setExp(0);
            player.setLevel(0);
        }
    }

    public void storeStats(){
        parser.storeStats(stats, uuid);
    }

    public void savePreviousLocation(Universe universe, Location location){
        JsonObject newLocation = new JsonObject();
        newLocation.addProperty("x", location.getX());
        newLocation.addProperty("y", location.getY());
        newLocation.addProperty("z", location.getZ());
        newLocation.addProperty("yaw", location.getYaw());
        newLocation.addProperty("pitch", location.getPitch());

        JsonObject previousLocation = previousLocations.getAsJsonObject(universe.serverWorld().getName());
        if (previousLocation == null) {
            previousLocations.add(universe.serverWorld().getName(), newLocation);
        }else {
            previousLocations.remove(universe.serverWorld().getName());
            previousLocations.add(universe.serverWorld().getName(), newLocation);
        }
    }

    public Location loadPreviousLocation(Universe universe){
        JsonObject previousLocation = previousLocations.getAsJsonObject(universe.serverWorld().getName());
        if (previousLocation != null){
            double x = previousLocation.get("x").getAsDouble();
            double y = previousLocation.get("y").getAsDouble();
            double z = previousLocation.get("z").getAsDouble();
            float yaw = previousLocation.get("yaw").getAsFloat();
            float pitch = previousLocation.get("pitch").getAsFloat();
            return new Location(universe.serverWorld(), x, y, z, yaw, pitch);
        }else{
            return null;
        }
    }

    public void storePreviousLocations(){
        parser.storeLocations(previousLocations, uuid, "previousLocations");
    }

    public void buildPreviousLocations(){
        previousLocations = parser.buildLocations(uuid, "previousLocations");
    }

    public void saveBedLocation(Universe universe, Location location){
        JsonObject newLocation = new JsonObject();
        newLocation.addProperty("world", location.getWorld().getName());
        newLocation.addProperty("x", location.getX());
        newLocation.addProperty("y", location.getY());
        newLocation.addProperty("z", location.getZ());
        newLocation.addProperty("yaw", location.getYaw());
        newLocation.addProperty("pitch", location.getPitch());

        JsonObject bedSpawn;
        if (Universes.plugin.perWorldBedRespawn)
            bedSpawn = bedSpawns.getAsJsonObject(universe.name());
        else
            bedSpawn = bedSpawns.getAsJsonObject(universe.serverWorld().getName());
        if (bedSpawn == null) {
            if (Universes.plugin.perWorldBedRespawn)
                bedSpawns.add(universe.name(), newLocation);
            else
                bedSpawns.add(universe.serverWorld().getName(), newLocation);
        }else {
            bedSpawns.remove(universe.name());
            bedSpawns.add(universe.name(), newLocation);
        }
    }

    public Location loadBedLocation(Universe universe){
        //System.out.println(bedSpawns);
        JsonObject bedSpawn;
        if (Universes.plugin.perWorldBedRespawn)
            bedSpawn = bedSpawns.getAsJsonObject(universe.name());
        else
            bedSpawn = bedSpawns.getAsJsonObject(universe.serverWorld().getName());
        if (bedSpawn != null){
            String world = bedSpawn.get("world").getAsString();
            double x = bedSpawn.get("x").getAsDouble();
            double y = bedSpawn.get("y").getAsDouble();
            double z = bedSpawn.get("z").getAsDouble();
            float yaw = bedSpawn.get("yaw").getAsFloat();
            float pitch = bedSpawn.get("pitch").getAsFloat();
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }else{
            return null;
        }
    }

    public void storeBedLocations(){
        parser.storeLocations(bedSpawns, uuid, "bedSpawns");
    }

    public void buildBedLocations(){
        bedSpawns = parser.buildLocations(uuid, "bedSpawns");
    }

    public void setPreviousLocations(JsonObject previousLocations){
        this.previousLocations = previousLocations;
    }

    public void buildBalances(){
        if (Universes.plugin.inventoryGrouping) {
            for (Map.Entry<String, String> group : Universes.plugin.groups.entrySet()) {
                PlayerAccountFile account = new PlayerAccountFile(Universes.plugin, player.getUniqueId().toString(), group.getValue());
                Universes.econ.createPlayerAccount(player, group.getValue());
                Universes.econ.depositPlayer(player, account.get().getDouble("balance"));
                //Bukkit.getLogger().log(Level.INFO, group.getValue() + ": " + account.get().getDouble("balance"));
            }
        }else {
            for (World world : Bukkit.getWorlds()) {
                PlayerAccountFile account = new PlayerAccountFile(Universes.plugin, player.getUniqueId().toString(), world.getName());
                Universes.econ.createPlayerAccount(player, world.getName());
                Universes.econ.depositPlayer(player, account.get().getDouble("balance"));
            }
        }
    }
}
