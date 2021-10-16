package net.whispwriting.universes.utils;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.commands.*;
import net.whispwriting.universes.commands.tab_completers.*;
import net.whispwriting.universes.events.*;
import org.bukkit.Bukkit;

public class UniverseLoader {
    
    public static void registerCommands(Universes plugin){
        plugin.getCommand("universecreate").setExecutor(new CreateCommand(plugin));
        plugin.getCommand("universeimport").setExecutor(new ImportCommand(plugin));
        plugin.getCommand("universelist").setExecutor(new ListWorldsCommand());
        plugin.getCommand("universeteleport").setExecutor(new TeleportCommand(plugin));
        plugin.getCommand("universeoverride").setExecutor(new OverrideCommand(plugin));
        plugin.getCommand("universemodify").setExecutor(new ModifyCommand(plugin));
        plugin.getCommand("universedelete").setExecutor(new DeleteCommand(plugin));
        plugin.getCommand("universeunload").setExecutor(new UnloadCommand(plugin));
        plugin.getCommand("uconfirm").setExecutor(new ConfirmCommand(plugin));
        plugin.getCommand("ucancel").setExecutor(new CancelCommand(plugin));
        plugin.getCommand("universehelp").setExecutor(new HelpCommand());
        plugin.getCommand("universereload").setExecutor(new ReloadCommand(plugin));
        plugin.getCommand("universekits").setExecutor(new KitCommand(plugin));
        plugin.getCommand("usetspawn").setExecutor(new FirstJoinSpawnCommand(plugin.spawnFile));
        plugin.getCommand("universespawn").setExecutor(new SpawnCommand(plugin));
        plugin.getCommand("universeconvert").setExecutor(new ConvertCommand(plugin));
        plugin.getCommand("universeinfo").setExecutor(new InfoCommand(plugin));
        plugin.getCommand("universeresetplayercount").setExecutor(new PlayerCountResetCommand(plugin));
        plugin.getCommand("ueconomy").setExecutor(new EconomyCommand());
        plugin.getCommand("ubalance").setExecutor(new BalanceCommand());
    }

    public static void registerTabCompleters(Universes plugin){
        plugin.getCommand("universecreate").setTabCompleter(new GenerateWorldTabCompleter());
        plugin.getCommand("universeimport").setTabCompleter(new GenerateWorldTabCompleter());
        plugin.getCommand("universedelete").setTabCompleter(new WorldListCompleter());
        plugin.getCommand("universeteleport").setTabCompleter(new TeleportTabComplete());
        plugin.getCommand("universespawn").setTabCompleter(new TeleportTabComplete());
        plugin.getCommand("universeunload").setTabCompleter(new WorldListCompleter());
        plugin.getCommand("universeinfo").setTabCompleter(new WorldListCompleter());
        plugin.getCommand("ueconomy").setTabCompleter(new EconomyAdminCompleter());
    }
    
    public static void registerEventHandlers(Universes plugin){
        Bukkit.getPluginManager().registerEvents(new TeleportEvent(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new RespawnEvent(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerChangeOnlineState(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new FlyEvent(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PVPEvent(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerChangeWorld(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new PortalEvent(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new CommandPreprocessEvent(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new WorldGenerationEvent(plugin), plugin);
    }
    
    public static void loadWorlds(Universes plugin){
        WorldLoader.loadWorlds(plugin);
    }
}
