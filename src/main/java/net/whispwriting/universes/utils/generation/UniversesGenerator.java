package net.whispwriting.universes.utils.generation;

import net.whispwriting.universes.Universes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;


public class UniversesGenerator {

    private Universes plugin;
    private WorldCreator creator;
    private World createdWorld;

    public UniversesGenerator(Universes pl, String name){
        plugin = pl;
        creator = new WorldCreator(name);
    }

    public void setEnvironment(World.Environment env){
        creator.environment(env);
    }

    public void generateStructures(boolean b){
        creator.generateStructures(b);
    }

    public void setSeed(long seed){
        creator.seed(seed);
    }

    public void setType(WorldType type){
        creator.type(type);
    }

    public void setGenerator(String generator){
        creator.generator(generator);
    }

    public void createWorld(){
        createdWorld = Bukkit.createWorld(creator);
        createdWorld.setAutoSave(true);
    }

    public World getWorld(){
        return createdWorld;
    }
}
