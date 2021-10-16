package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldSettingsFile extends AbstractFile{

    public WorldSettingsFile(Universes pl, String world){
        super(pl, "settings.yml", "/worlds/" + world + "/");
    }

    public void setDefaults(World world, String environment, String generator){
        config.set("spawn.x", world.getSpawnLocation().getX());
        config.set("spawn.y", world.getSpawnLocation().getY());
        config.set("spawn.z", world.getSpawnLocation().getZ());
        config.set("spawn.yaw", world.getSpawnLocation().getYaw());
        config.set("spawn.pitch", world.getSpawnLocation().getPitch());
        config.set("respawnWorld", world.getName());
        config.set("environment", environment);
        config.set("seed", world.getSeed());
        config.set("generator", generator);
        config.set("gameMode", "survival");
        config.set("playerLimit", -1);
        config.set("difficulty", world.getDifficulty().name());
        config.set("allowPvP", true);
        config.set("allowAnimals", true);
        config.set("allowMonsters", true);
        config.set("allowFlight", true);
        List<String> blockedCommands = new ArrayList<>();
        config.set("blockedCommands", blockedCommands);
    }

    public void updateValues(GameMode gameMode, Location spawn, World.Environment environment, String respawnWorld, int maxPlayers, boolean allowAnimals,
                             Difficulty difficulty, boolean allowMonsters, boolean allowFlight, boolean allowPvp,
                             List<String> blockedCommands){
        config.set("spawn.x", spawn.getX());
        config.set("spawn.y", spawn.getY());
        config.set("spawn.z", spawn.getZ());
        config.set("spawn.yaw", spawn.getYaw());
        config.set("spawn.pitch", spawn.getPitch());
        config.set("environment", getEnvironment(environment));
        config.set("respawnWorld", respawnWorld);
        config.set("gameMode", gameMode.name().toLowerCase());
        config.set("difficulty", difficulty.name());
        config.set("playerLimit", maxPlayers);
        config.set("allowAnimals", allowAnimals);
        config.set("allowMonsters", allowMonsters);
        config.set("allowFlight", allowFlight);
        config.set("allowPvP", allowPvp);
        config.set("blockedCommands", blockedCommands);
    }

    private static String getEnvironment(World.Environment environment){
        switch (environment){
            case NORMAL:
                return "normal";
            case NETHER:
                return "nether";
            case THE_END:
                return "end";
            default:
                return "normal";
        }
    }
}
