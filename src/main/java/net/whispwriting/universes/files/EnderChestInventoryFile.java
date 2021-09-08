package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;

public class EnderChestInventoryFile extends AbstractFile {

    public EnderChestInventoryFile(Universes pl, String uuid, String world){
        super(pl, uuid+"enderchest.yml", "/inventories/"+world);
    }

}
