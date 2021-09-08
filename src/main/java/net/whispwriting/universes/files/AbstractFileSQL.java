package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AbstractFileSQL {

    protected Universes plugin;
    private File file;
    protected FileConfiguration config;

    public AbstractFileSQL(Universes plugin, String filename, String dir){
        this.plugin = plugin;
        file = new File(plugin.getDataFolder() + dir, filename);
        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void save(){
        try{
            config.save(file);
        }catch (IOException e){
            System.out.println("Could not save file.");
        }
    }

    public File get(){
        return file;
    }

}
