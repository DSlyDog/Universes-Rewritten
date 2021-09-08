package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class PlayerInventoryFile extends AbstractFile {

    public PlayerInventoryFile(Universes pl, String uuid, String world, String type){
        super(pl, type+".yml", "/playerdata/inventories/"+world+"/"+uuid+"/");
    }

}
