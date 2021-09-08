package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class GroupsFile extends AbstractFile{

    public GroupsFile(Universes pl){
        super(pl, "groups.yml", "");
    }

    public void setDefaults(){
        World world = Bukkit.getWorlds().get(0);
        List<String> defaultGroup = new ArrayList<>();
        defaultGroup.add(world.getName());
        defaultGroup.add(world.getName()+"_nether");
        defaultGroup.add(world.getName()+"_the_end");
        config.set("default", defaultGroup);
    }

}
