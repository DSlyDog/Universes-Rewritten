package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class PlayerDataFile extends AbstractFile{

    public PlayerDataFile(Universes pl, String uuid){
        super(pl, "data.yml", "/playerdata/data/" + uuid + "/");
    }
}
