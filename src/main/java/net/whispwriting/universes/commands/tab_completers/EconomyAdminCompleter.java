package net.whispwriting.universes.commands.tab_completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class EconomyAdminCompleter implements TabCompleter {

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
            options.add("give");
            options.add("take");
            options.add("set");
            options.add("view");
            return options;
        }

        if (args.length == 3){
            List<String> options = new ArrayList<>();
            options.add("balance");
            return options;
        }
        return null;
    }
}
