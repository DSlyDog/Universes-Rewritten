package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class PlayerAccountFile extends AbstractFile{

    public PlayerAccountFile(Universes pl, String uuid, String world){
        super(pl, "economy.yml", "/playerdata/economy/" + world + "/" + uuid + "/");
    }
}
