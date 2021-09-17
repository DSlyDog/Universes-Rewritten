package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class PlayerSettingsFileOld extends AbstractFile {
    public PlayerSettingsFileOld(Universes pl, String uuid) {
        super(pl, uuid + ".yml", "/playerdata");
    }

    public void setup() {
        this.config.addDefault("gameModeOverride", false);
        this.config.addDefault("canJoinFullWorlds", false);
        this.config.addDefault("flightOverride", false);
        this.save();
    }
}
