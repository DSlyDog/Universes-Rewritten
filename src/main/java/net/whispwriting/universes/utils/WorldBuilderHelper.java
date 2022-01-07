package net.whispwriting.universes.utils;

import net.whispwriting.universes.utils.generation.UniversesGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public class WorldBuilderHelper {

    private String seed = "";
    private String generator = "";
    private String group = "";

    public World makeWorld(String environment, UniversesGenerator universesGenerator, CommandSender sender, String[] args){
        if (args.length == 5) {
            getType(args[4]);
        }else if (args.length == 6) {
            getType(args[4]);
            getType(args[5]);
        }else if (args.length >= 7) {
            getType(args[4]);
            getType(args[5]);
            getType(args[6]);
        }

        return WorldLoader.constructWorld(universesGenerator, sender, environment, args[2], args[3], group, seed, generator);
    }

    private void getType(String argument){
        try{
            Long.parseLong(argument);
            this.seed = argument;
        }catch(NumberFormatException e){
            if (Bukkit.getPluginManager().getPlugin(argument) != null)
                this.generator = argument;
            else
                this.group = argument;
        }
    }
}
