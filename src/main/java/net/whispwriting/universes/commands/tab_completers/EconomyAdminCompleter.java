package net.whispwriting.universes.commands.tab_completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class EconomyAdminCompleter implements TabCompleter {

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
