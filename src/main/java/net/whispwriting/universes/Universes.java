package net.whispwriting.universes;

import net.milkbowl.vault.economy.Economy;
import net.whispwriting.universes.files.*;
import net.whispwriting.universes.guis.WorldSettingsUI;
import net.whispwriting.universes.utils.*;
import net.whispwriting.universes.utils.economy.EconomyHandler;
import net.whispwriting.universes.utils.sql.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Universes extends JavaPlugin {

    public ConfigFile config;
    public SpawnFile spawnFile;
    public ChatPrefixFile prefixFile;
    public List<PlayersWhoCanConfirm> players = new ArrayList<>();
    public KitsFile kitsFile = new KitsFile(this);
    public String defaultWorld;
    public Map<String, UniversePlayer> onlinePlayers = new HashMap<>();
    public Map<String, Universe> universes = new HashMap<>();
    public Map<String, String> groups = new HashMap<>();
    public SQL sql;
    private DatabaseFile databaseFile;
    public boolean useRemote;
    public boolean perWorldInventories;
    public boolean inventoryGrouping;
    public boolean perWorldStats;
    public boolean useRespawnWorld;
    public boolean trackLastLocation;
    public boolean prefixChat;
    public boolean useFirstJoinSpawn;
    public boolean saveLastLocOnDeath;
    public boolean worldEntryPermissions;
    public boolean hubOnJoin;
    public boolean netherPerOverworld;
    public boolean endPerOverworld;
    public boolean toEntryPortal;
    public boolean toGroupOnRespawn;
    public boolean startupComplete;
    public boolean useEconomy;
    public boolean usePerWorldTeleportPermissions;
    public boolean perWorldKitGrouping;
    public static Economy econ;
    private VaultHook vaultHook = new VaultHook();
    public static Universes plugin;

    @Override
    public void onEnable() {
        plugin = this;
        loadFiles();
        setDefaults();
        UniverseLoader.registerCommands(this);
        UniverseLoader.registerTabCompleters(this);
        UniverseLoader.registerEventHandlers(this);
        UniverseLoader.loadWorlds(this);
        WorldSettingsUI.init();
        startupComplete = true;
        vaultHook.hook();
        setupEconomy();
        checkConfigVersion();

        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                registerOnlinePlayers();
            }
        }, 20);
    }

    @Override
    public void onDisable() {
        sql.close();
        vaultHook.unhook();
    }

    private void registerOnlinePlayers(){
        for (Player player : Bukkit.getOnlinePlayers()){
            String name = player.getName();
            UUID uuid = player.getUniqueId();
            String world = player.getWorld().getName();
            UniversePlayer uPlayer = new UniversePlayer(uuid, player, this);
            uPlayer.buildStats();
            uPlayer.buildPreviousLocations();
            onlinePlayers.put(name, uPlayer);
            universes.get(world).incrementPlayerCount();
        }
    }

    private void loadFiles(){
        config = new ConfigFile(this);
        databaseFile = new DatabaseFile(this);
        databaseFile.createConfig();
        databaseFile.get().options().copyDefaults(true);
        databaseFile.save();
        spawnFile = new SpawnFile(this);
        spawnFile.get().options().copyDefaults(true);
        prefixFile = new ChatPrefixFile(this);
    }

    public void setDefaults(){
        useRemote = databaseFile.get().getBoolean("remote-database");
        if (useRemote)
            sql = new MySQL(databaseFile);
        else
            sql = new SQLite(this);

        perWorldInventories = config.get().getBoolean("per-world-inventories");
        inventoryGrouping = config.get().getBoolean("per-world-inventory-grouping");
        perWorldStats = config.get().getBoolean("per-world-stats");
        useRespawnWorld = config.get().getBoolean("use-respawnWorld");
        trackLastLocation = config.get().getBoolean("track-previous-locations");
        saveLastLocOnDeath = plugin.config.get().getBoolean("save-location-on-death");
        prefixChat = config.get().getBoolean("prefix-chat");
        useFirstJoinSpawn = config.get().getBoolean("use-first-join-spawn");
        worldEntryPermissions = config.get().getBoolean("world-entry-permissions");
        hubOnJoin = config.get().getBoolean("rejoin-at-hub");
        netherPerOverworld = config.get().getBoolean("nether-per-overworld");
        toEntryPortal = config.get().getBoolean("return-to-entry-portal");
        endPerOverworld = config.get().getBoolean("end-per-overworld");
        toGroupOnRespawn = config.get().getBoolean("respawn-at-group-spawn");
        usePerWorldTeleportPermissions = config.get().getBoolean("use-per-world-teleport-permissions");
        perWorldKitGrouping = config.get().getBoolean("per-world-kit-grouping");
    }

    public void checkConfigVersion(){
        String pluginVersion = getDescription().getVersion();
        String configVersion = config.get().getString("version");

        if (configVersion == null)
            config.writeComments();

        if (!pluginVersion.equals(configVersion))
            config.writeComments();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
