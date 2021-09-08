package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class ChatPrefixFile extends AbstractFile{
    public ChatPrefixFile(Universes plugin) {
        super(plugin, "prefixes.yml", "");
    }

    public void writeComments(){
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("#By default, the plugin will use the world name when prefixes are enabled.\n" +
                    "#To use a custom prefix to a world, follow this format.\n" +
                    "#world name: \"[Prefix]\"\n" +
                    "#world_nether: \"&0[&4Nether&0]&f\"");
            writer.close();
        }catch(IOException e){
            Bukkit.getLogger().log(Level.WARNING, "[Universes] Failed to write comments to prefix file");
        }
    }
}
