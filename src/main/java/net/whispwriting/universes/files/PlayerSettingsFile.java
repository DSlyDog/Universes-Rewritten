package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class PlayerSettingsFile extends AbstractFile {

    public PlayerSettingsFile(Universes pl, String uuid){
        super(pl, "settings.yml", "/playerdata/settings/" + uuid + "/");
    }

    public void setup(){
        config.addDefault("gameModeOverride", false);
        config.addDefault("canJoinFullWorlds", false);
        config.addDefault("flightOverride", false);
        save();
    }

}
