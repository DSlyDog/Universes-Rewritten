package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void update(CommandSender sender){
        Map<String, List<String>> groups = new HashMap<>();
        for (World world : Bukkit.getWorlds()){
            String group = config.getString(world.getName()+".group");
            if (group == null){
                sender.sendMessage(ChatColor.RED + "ABORTING GROUP CONVERSION!!\n" +
                        "An issue was detected in your groups.yml, to preserve your groups the conversion process has" +
                        "been aborted. This is likely due to an entry being improperly formatted. Please reach out" +
                        "to Whisp if you need help. Joining the Discord (https://discord.gg/E784KgeMQB) and ping" +
                        "@Whisp Reedwell#5879 for the fastest response. Do not panic! None of your data has been lost.");
                return;
            }
            if (groups.containsKey(group)){
                groups.get(group).add(world.getName());
            }else{
                List<String> newGroup = new ArrayList<>();
                newGroup.add(world.getName());
                groups.put(group, newGroup);
            }
        }
        try {
            file.delete();
            file.createNewFile();
            for (Map.Entry<String, List<String>> group : groups.entrySet()){
                String name = group.getKey();
                config.set(name, group.getValue());
            }
            save();
            WorldLoader.loadWorlds(Universes.plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
