package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AbstractFile {

    protected File file;
    protected FileConfiguration config;

    public AbstractFile(Universes plugin, String filename, String d){
        makeFile(plugin, filename, d);
    }

    protected void makeFile(Universes plugin, String filename, String d){
        File dir = new File(plugin.getDataFolder() + d);
        if (!dir.exists()){
            dir.mkdirs();
        }
        file = new File(dir, filename);
        if (!file.exists()){
            try{
                file.createNewFile();
                writeComments();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void writeComments(){

    }

    public void save(){
        try{
            config.save(file);
        }catch(IOException e){
            System.out.println("Could not save file");
        }
    }
    public FileConfiguration get(){
        return config;
    }

    public void reload(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void delete() {
        config = null;
        file.delete();
    }
}
