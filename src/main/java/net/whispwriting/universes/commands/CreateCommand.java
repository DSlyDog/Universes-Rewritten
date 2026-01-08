package net.whispwriting.universes.commands;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.WorldBuilderHelper;
import net.whispwriting.universes.utils.WorldLoadEventHelper;
import net.whispwriting.universes.utils.WorldLoader;
import net.whispwriting.universes.utils.generation.UniversesGenerator;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CreateCommand implements CommandExecutor {

    private Universes plugin;
    private WorldBuilderHelper helper;

    public CreateCommand(Universes pl){
        plugin = pl;
        helper = new WorldBuilderHelper();
    }

    /**
     * Command to create a new world
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("Universes.createworld")) {
            if (args.length < 4){
                sender.sendMessage(ChatColor.RED + "Too few arguments");
                sender.sendMessage(ChatColor.GOLD + "/universecreate " + ChatColor.YELLOW + "<name> <environment> <world type> <difficulty> <group> <seed> <generator>");
                return true;
            }
            if (plugin.universes.containsKey(args[0])){
                sender.sendMessage(ChatColor.RED + "A world with that name already exists");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Starting creation of world " + ChatColor.DARK_GREEN + args[0]);
            UniversesGenerator universesGenerator = new UniversesGenerator(plugin, args[0]);
            WorldLoadEventHelper.getInstance().setCreateCommandExecuted(true);
            World world = helper.makeWorld(args[1], universesGenerator, sender, args);
            Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
            boolean hasUNehters = false;
            boolean hasEnds = false;
            for (Plugin plugin : plugins){
                if (plugin.getName().equals("Universe-Nethers"))
                    hasUNehters = true;
                else if (plugin.getName().equals("Universe-Ends"))
                    hasEnds = true;
            }
            if (hasUNehters){
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    if (Universes.plugin.netherPerOverworld) {
                        if (Bukkit.getWorld(args[0] + "_nether") == null) {
                            sender.sendMessage(ChatColor.GREEN + "Starting creation of corresponding nether");
                            UniversesGenerator universesGeneratorNether = new UniversesGenerator(plugin, args[0] + "_nether");
                            WorldLoadEventHelper.getInstance().setCreateCommandExecuted(true);
                            helper.makeWorld("nether", universesGeneratorNether, sender, args);
                        }
                    }
                }
            }
            if (hasEnds){
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    if (Universes.plugin.endPerOverworld) {
                        if (Bukkit.getWorld(args[0] + "_the_end") == null) {
                            sender.sendMessage(ChatColor.GREEN + "Starting creation of corresponding end");
                            UniversesGenerator universesGeneratorEnd = new UniversesGenerator(plugin, args[0] + "_the_end");
                            WorldLoadEventHelper.getInstance().setCreateCommandExecuted(true);
                            helper.makeWorld("end", universesGeneratorEnd, sender, args);
                        }
                    }
                }
            }
        }else{
            sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
            return true;
        }

        return true;
    }
}
