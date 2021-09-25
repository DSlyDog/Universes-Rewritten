package net.whispwriting.universes.utils;

import net.whispwriting.universes.files.WorldSettingsFile;
import org.bukkit.*;

import java.util.*;
import java.util.logging.Level;

public class Universe {

    private World world;
    private int totalPlayers = 0;
    private String name;
    private List<String> blockedCommands;
    private boolean allowAnimals;
    private boolean allowMonsters;
    private boolean allowFlight;
    private boolean allowPvP;
    private boolean playerLimitEnabled;
    private GameMode gameMode;
    private int maxPlayers;
    private Location spawn;
    private String respawnWorld;
    private WorldSettingsFile worldSettings;

    public Universe(String name, World world, boolean allowAnimals, boolean allowMonsters, boolean allowFlight,
                    GameMode gameMode, int maxPlayers, Location spawn, String respawnWorld, boolean allowPvP,
                    boolean playerLimitEnabled, WorldSettingsFile worldSettings, List<String> blockedCommands){
        this.name = name;
        this.allowAnimals = allowAnimals;
        this.allowMonsters = allowMonsters;
        this.allowFlight = allowFlight;
        this.playerLimitEnabled = playerLimitEnabled;
        this.maxPlayers = maxPlayers;
        this.world = world;
        this.spawn = spawn;
        this.gameMode = gameMode;
        this.respawnWorld = respawnWorld;
        this.allowPvP = allowPvP;
        this.worldSettings = worldSettings;
        this.blockedCommands = blockedCommands;
        world.setSpawnFlags(allowMonsters, allowAnimals);
    }

    public World serverWorld(){
        return world;
    }

    public void incrementPlayerCount(){
        totalPlayers++;
    }

    public void decrementPlayerCount(){
        totalPlayers--;
        if (totalPlayers < 0)
            totalPlayers = 0;
    }

    public int playerCount(){
        return totalPlayers;
    }

    public boolean blockCommand(String command){
        if (blockedCommands.contains(command)) {
            return false;
        } else {
            blockedCommands.add(command.toLowerCase());
            save();
            return true;
        }
    }

    public boolean unblockCommand(String command){
        if (!blockedCommands.contains(command)) {
            return false;
        }else {
            blockedCommands.remove(command.toLowerCase());
            save();
            return true;
        }
    }

    public List<String> blockedCommands(){
        return blockedCommands;
    }

    public boolean isCommandBlocked(String command){
        return blockedCommands.contains(command.toLowerCase());
    }

    public String name(){
        return name;
    }

    public boolean equals(Universe universe){
        return this.name.equals(universe.name);
    }

    public void setDifficulty(Difficulty difficulty){
        world.setDifficulty(difficulty);
        save();
    }

    public void allowMonsters(boolean allowMonsters){
        this.allowMonsters = allowMonsters;
        world.setSpawnFlags(allowMonsters, allowAnimals);
        save();
    }

    public void allowAnimals(boolean allowAnimals){
        this.allowAnimals = allowAnimals;
        world.setSpawnFlags(allowMonsters, allowAnimals);
        save();
    }

    public void allowFlight(boolean allowFlight){
        this.allowFlight = allowFlight;
        save();
    }

    public void allowPvP(boolean allowPvP){
        this.allowPvP = allowPvP;
        save();
    }

    public void setMaxPlayers(int maxPlayers){
        this.maxPlayers = maxPlayers;
        this.playerLimitEnabled = updatePlayerLimitEnabled();
        save();
    }

    private boolean updatePlayerLimitEnabled(){
        return maxPlayers != -1;
    }

    public boolean isAtMaxPlayers(){
        if (playerLimitEnabled)
            return totalPlayers == maxPlayers;
        else
            return false;
    }

    public boolean isAllowMonsters(){
        return allowMonsters;
    }

    public boolean isAllowAnimals(){
        return allowAnimals;
    }

    public boolean isAllowFlight(){
        return allowFlight;
    }

    public boolean isAllowPvP(){
        return allowPvP;
    }

    public Location spawn(){
        return spawn;
    }

    public void setSpawn(Location spawn){
        this.spawn = spawn;
        save();
    }

    public GameMode gameMode(){
        return gameMode;
    }

    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
        save();
    }

    public String respawnWorld(){
        return respawnWorld;
    }

    public void setRespawnWorld(String respawnWorld){
        this.respawnWorld = respawnWorld;
        save();
    }

    public int maxPlayers(){
        return maxPlayers;
    }

    public Difficulty getDifficulty(){
        return world.getDifficulty();
    }

    public void resetTotalPlayers(){
        totalPlayers = 0;
        maxPlayers = -1;
    }

    public boolean isPlayerLimitEnabled(){
        return playerLimitEnabled;
    }

    public void save(){
        worldSettings.updateValues(gameMode, spawn, world.getEnvironment(), respawnWorld, maxPlayers, allowAnimals,
                world.getDifficulty(), allowMonsters, allowFlight, allowPvP, blockedCommands);
        worldSettings.save();
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD).append("World Name: ").append(ChatColor.YELLOW).append(world.getName()).append("\n")
                .append(ChatColor.GOLD).append("Group Name: ").append(ChatColor.YELLOW).append(name).append("\n");
        builder.append(ChatColor.GOLD).append("Blocked Commands: ").append(ChatColor.YELLOW);
        for (String command : blockedCommands){
            builder.append(command);
        }
        builder.append("\n");
        builder.append(ChatColor.GOLD).append("Allow Animals: ").append(ChatColor.YELLOW).append(allowAnimals).append("\n")
                .append(ChatColor.GOLD).append("Allow Monsters: ").append(ChatColor.YELLOW).append(allowMonsters).append("\n")
                .append(ChatColor.GOLD).append("Allow Flight: ").append(ChatColor.YELLOW).append(allowFlight).append("\n")
                .append(ChatColor.GOLD).append("Allow PvP: ").append(ChatColor.YELLOW).append(allowPvP).append("\n")
                .append(ChatColor.GOLD).append("Game Mode: ").append(ChatColor.YELLOW).append(gameMode.name().toLowerCase()).append("\n")
                .append(ChatColor.GOLD).append("Spawn Location: ").append(ChatColor.YELLOW)
                .append(spawn.getX()).append(", ").append(spawn.getY()).append(", ").append(spawn.getZ()).append("\n")
                .append(ChatColor.GOLD).append("Respawn World: ").append(ChatColor.YELLOW).append(respawnWorld).append("\n")
                .append(ChatColor.GOLD).append("Max Players: ").append(ChatColor.YELLOW).append(maxPlayers).append("\n")
                .append(ChatColor.GOLD).append("Total Players: ").append(ChatColor.YELLOW).append(totalPlayers).append("\n")
                .append(ChatColor.GOLD).append("Player Limit Enabled: ").append(ChatColor.YELLOW).append(playerLimitEnabled);
        return builder.toString();
    }
}
