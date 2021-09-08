package net.whispwriting.universes.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.GroupsFile;
import net.whispwriting.universes.files.WorldSettingsFile;
import net.whispwriting.universes.utils.generation.UniversesGenerator;
import net.whispwriting.universes.utils.sql.SQLResult;
import org.bukkit.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import static org.bukkit.Difficulty.*;

public class WorldLoaderOld {

    public static void loadWorlds(Universes plugin){
        try {
            SQLResult result = plugin.sql.query("select * from universe", "query");
            if (!result.isBooleanResult()) {
                ResultSet resultSet = result.sqlResults();
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String environment = resultSet.getString("environment");
                    boolean pvp = resultSet.getBoolean("pvp");
                    boolean allowMonsters = resultSet.getBoolean("allowMonsters");
                    boolean allowAnimals = resultSet.getBoolean("allowAnimals");
                    boolean allowFlight = resultSet.getBoolean("allowFlight");
                    String gameMode = resultSet.getString("gameMode");
                    int maxPlayers = resultSet.getInt("maxPlayers");
                    boolean playerLimitEnabled;
                    if (maxPlayers == -1)
                        playerLimitEnabled = false;
                    else
                        playerLimitEnabled = true;
                    String difficulty = resultSet.getString("difficulty");
                    String respawnWorld = resultSet.getString("respawnWorld");
                    String blockedCommandsString = resultSet.getString("blockedCommands");
                    List<String> blockedCommands = new ArrayList<>();
                    if (!blockedCommandsString.equals("")){
                        blockedCommands.addAll(Arrays.asList(blockedCommandsString.split(",")));
                    }
                    GroupsFile groupsFile = new GroupsFile(plugin);
                    boolean worldInGround = false;
                    for (String group : groupsFile.get().getConfigurationSection("").getKeys(false)){
                        List<String> groop = groupsFile.get().getStringList(group);
                        if (groop.contains(name))
                            worldInGround = true;
                    }
                    if (!worldInGround) {
                        List<String> group = new ArrayList<>();
                        group.add(name);
                        Bukkit.getLogger().log(Level.INFO, name);
                        //groupsFile.get().set(name, group);
                        groupsFile.save();
                    }
                    UniversesGenerator universesGenerator = new UniversesGenerator(plugin, name);
                    universesGenerator.setEnvironment(getTypeFromString(environment));
                    universesGenerator.createWorld();
                    World world = universesGenerator.getWorld();
                    world.setDifficulty(getDifficulty(difficulty));
                    world.setSpawnFlags(allowMonsters, allowAnimals);
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            world.setSpawnFlags(allowMonsters, allowAnimals);
                        }
                    }, 2);
                    JsonParser parser = new JsonParser();
                    JsonObject spawnJson = parser.parse(resultSet.getString("spawn")).getAsJsonObject();
                    double x = spawnJson.get("x").getAsDouble();
                    double y = spawnJson.get("y").getAsDouble();
                    double z = spawnJson.get("z").getAsDouble();
                    float yaw = spawnJson.get("yaw").getAsFloat();
                    float pitch = spawnJson.get("pitch").getAsFloat();
                    Location spawn = new Location(world, x, y, z, yaw, pitch);
                    Universe universe;
                    WorldSettingsFile worldSettings = new WorldSettingsFile(plugin, world.getName());
                    if (plugin.perWorldInventories && plugin.inventoryGrouping)
                        universe = new Universe(name, world, allowAnimals, allowMonsters, allowFlight,
                                getGameModeFromString(gameMode), maxPlayers, spawn, respawnWorld, pvp, playerLimitEnabled,
                                worldSettings, blockedCommands);
                    else
                        universe = new Universe(name, world, allowAnimals, allowMonsters, allowFlight,
                                getGameModeFromString(gameMode), maxPlayers, spawn, respawnWorld, pvp, playerLimitEnabled,
                                worldSettings, blockedCommands);

                    plugin.universes.put(name, universe);
                }
            }
        }catch(SQLException e){
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load worlds from database");
            e.printStackTrace();
        }
    }

    /*public static void createWorldConfig(Universes plugin){
        List<World> loadedWorlds = Bukkit.getWorlds();
        plugin.defaultWorld = loadedWorlds.get(0).getName();
        for (World loadedWorld : loadedWorlds) {
            String world = loadedWorld.getName();
            SQLResult result = plugin.sql.query("select * from universe where name='" + world + "'", "query");
            try {
                if (!result.sqlResults().next()) {
                    JsonObject spawn = new JsonObject();
                    double x = loadedWorld.getSpawnLocation().getX();
                    double y = loadedWorld.getSpawnLocation().getY();
                    double z = loadedWorld.getSpawnLocation().getZ();
                    float yaw = loadedWorld.getSpawnLocation().getYaw();
                    float pitch = loadedWorld.getSpawnLocation().getPitch();
                    spawn.addProperty("world", world);
                    spawn.addProperty("x", x);
                    spawn.addProperty("y", y);
                    spawn.addProperty("z", z);
                    spawn.addProperty("yaw", yaw);
                    spawn.addProperty("pitch", pitch);
                    plugin.sql.query("insert into universe values('" + world + "', '" +
                            loadedWorld.getEnvironment().name() + "', '" + loadedWorld.getDifficulty() + "', " +
                            true + ", " + true + ", " + true + ", " + true + ", 'survival', " + -1 +
                            ", '" + spawn + "', '" + world + "', '')", "insert");
                    Universe universe = new Universe(world, loadedWorld, true, true, true,
                            GameMode.SURVIVAL, -1, loadedWorld.getSpawnLocation(), world, true, false,
                            new ArrayList<>());
                    plugin.universes.put(world, universe);
                }
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "[Universes] Failed to load default worlds into database.");
                e.printStackTrace();
            }
        }
    }*/

    private static World.Environment getTypeFromString(String type){
        switch (type){
            case "normal":
                return World.Environment.NORMAL;
            case "nether":
                return World.Environment.NETHER;
            case "end":
                return World.Environment.THE_END;
            default:
                return World.Environment.NORMAL;
        }
    }

    private static Difficulty getDifficulty(String arg) {
        arg = arg.toLowerCase();
        switch (arg){
            case "peaceful":
                return PEACEFUL;
            case "easy":
                return EASY;
            case "normal":
                return NORMAL;
            case "hard":
                return HARD;
            default:
                return EASY;
        }
    }

    private static GameMode getGameModeFromString(String gameModeString) {
        switch (gameModeString){
            case "adventure":
                return GameMode.ADVENTURE;
            case "creative":
                return GameMode.CREATIVE;
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return GameMode.SURVIVAL;
        }
    }
}
