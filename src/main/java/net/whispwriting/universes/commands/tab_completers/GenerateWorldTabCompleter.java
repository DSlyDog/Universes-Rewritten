package net.whispwriting.universes.commands.tab_completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GenerateWorldTabCompleter implements TabCompleter {

    /**
     *
     * @param sender Source of the command.  For players tab-completing a
     *     command inside of a command block, this will be the player, not
     *     the command block.
     * @param command Command which was executed
     * @param alias Alias of the command which was used
     * @param args The arguments passed to the command, including final
     *     partial argument to be completed
     * @return options array or null
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> options = new ArrayList<>();
            options.add("world name");
            return options;
        }else if (args.length == 2){
            List<String> environment = new ArrayList<>();
            environment.add("normal");
            environment.add("nether");
            environment.add("end");
            return environment;
        }else if (args.length == 3){
            List<String> type = new ArrayList<>();
            type.add("normal");
            type.add("amplified");
            type.add("flat");
            type.add("large_biomes");
            return type;
        }else if (args.length == 4){
            List<String> difficulty = new ArrayList<>();
            difficulty.add("peaceful");
            difficulty.add("easy");
            difficulty.add("normal");
            difficulty.add("hard");
            return difficulty;
        }else if (args.length == 5){
            List<String> options = new ArrayList<>();
            options.add("group");
            options.add("seed");
            options.add("generator");
            return options;
        }else if (args.length == 6){
            List<String> options = new ArrayList<>();
            options.add("seed");
            options.add("generator");
            return options;
        }else if (args.length == 7){
            List<String> options = new ArrayList<>();
            options.add("generator");
            return options;
        }
        return null;
    }
}
