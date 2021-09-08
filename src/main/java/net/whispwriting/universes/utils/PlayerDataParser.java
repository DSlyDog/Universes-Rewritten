package net.whispwriting.universes.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerDataFile;

import java.util.UUID;

public class PlayerDataParser {

    private Universes plugin;

    public PlayerDataParser(Universes plugin){
        this.plugin = plugin;
    }

    public JsonObject parseStats(UUID uuid){
        PlayerDataFile dataFile = new PlayerDataFile(plugin, uuid.toString());
        String stats = dataFile.get().getString("stats");
        if (stats != null) {
            JsonParser parser = new JsonParser();
            return parser.parse(stats).getAsJsonObject();
        }else{
            return new JsonObject();
        }
    }

    public void storeStats(JsonObject stats, UUID uuid){
        PlayerDataFile dataFile = new PlayerDataFile(plugin, uuid.toString());
        dataFile.get().set("stats", stats.toString());
        dataFile.save();
    }

    public void storePreviousLocations(JsonObject previousLocations, UUID uuid){
        PlayerDataFile playerDataFile = new PlayerDataFile(Universes.plugin, uuid.toString());
        playerDataFile.get().set("previousLocations", previousLocations.toString());
        playerDataFile.save();
    }

    public JsonObject buildPreviousLocations(UUID uuid){
        PlayerDataFile playerDataFile = new PlayerDataFile(Universes.plugin, uuid.toString());
        String json = playerDataFile.get().getString("previousLocations");
        if (json != null) {
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        }else{
            return new JsonObject();
        }
    }
}
