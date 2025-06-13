package net.whispwriting.universes;

import net.whispwriting.universes.files.*;
import net.whispwriting.universes.gui.WorldSettingsUI_Old;
import net.whispwriting.universes.utils.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

public final class Universes extends JavaPlugin {

    public ConfigFile config;
    public SpawnFile spawnFile;
    public ChatPrefixFile prefixFile;
    public EconomyConfig economyFile;
    public GroupsFile groupsFile = new GroupsFile(this);
    public List<PlayersWhoCanConfirm> players = new ArrayList<>();
    public KitsFile kitsFile = new KitsFile(this);
    public String defaultWorld;
    public Map<String, UniversePlayer> onlinePlayers = new HashMap<>();
    public Map<String, Universe> universes = new HashMap<>();
    public Map<String, String> groups = new HashMap<>();
    public boolean perWorldInventories;
    public boolean inventoryGrouping;
    public boolean perWorldStats;
    public boolean removeEffectsOnWorldChange;
    public boolean useRespawnWorld;
    public boolean trackLastLocation;
    public boolean prefixChat;
    public boolean useFirstJoinSpawn;
    public boolean saveLastLocOnDeath;
    public boolean worldEntryPermissions;
    public boolean hubOnJoin;
    public boolean netherPerOverworld;
    public boolean endPerOverworld;
    public boolean othersControlLeaveEnd;
    public boolean toEntryPortal;
    public boolean toHubOnRespawn;
    public boolean toGroupOnRespawn;
    public boolean useBedRespawn;
    public boolean perWorldBedRespawn;
    public boolean startupComplete;
    public boolean usePerWorldTeleportPermissions;
    public boolean perWorldKitGrouping;
    public boolean useEconomy;
    public boolean returnToPreviousLocation;
    public String currencySingular, currencyPlural;
    public String currencyIndicator;
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
        startupComplete = true;
        checkConfigVersion();

        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                registerOnlinePlayers();
            }
        }, 20);
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
            if (Bukkit.getPluginManager().getPlugin("Universe-Spawnify") != null){
                uPlayer.buildBedLocations();
                player.setBedSpawnLocation(uPlayer.loadBedLocation(universes.get(world)));
            }
        }
    }

    private void loadFiles(){
        config = new ConfigFile(this);
        spawnFile = new SpawnFile(this);
        spawnFile.get().options().copyDefaults(true);
        economyFile = new EconomyConfig(this);
        prefixFile = new ChatPrefixFile(this);
    }

    public void setDefaults(){
        perWorldInventories = config.get().getBoolean("per-world-inventories");
        inventoryGrouping = config.get().getBoolean("per-world-inventory-grouping");
        perWorldStats = config.get().getBoolean("per-world-stats");
        removeEffectsOnWorldChange = config.get().getBoolean("remove-effects-on-world-change");
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
        othersControlLeaveEnd = config.get().getBoolean("let-other-plugin-control-leaving-end");
        toHubOnRespawn = config.get().getBoolean("respawn-at-hub");
        toGroupOnRespawn = config.get().getBoolean("respawn-at-group-spawn");
        useBedRespawn = config.get().getBoolean("use-bed-respawn");
        perWorldBedRespawn = config.get().getBoolean("per-world-bed-spawns");
        usePerWorldTeleportPermissions = config.get().getBoolean("use-per-world-teleport-permissions");
        perWorldKitGrouping = config.get().getBoolean("per-world-kit-grouping");
        returnToPreviousLocation = config.get().getBoolean("return-to-previous-locations");
        useEconomy = economyFile.get().getBoolean("use-universes-economy");
        currencySingular = economyFile.get().getString("currency-name-singular");
        currencyPlural = economyFile.get().getString("currency-name-plural");
        currencyIndicator = economyFile.get().getString("currency-prefix");
    }

    public void checkConfigVersion(){
        String pluginVersion = getDescription().getVersion();
        String configVersion = config.get().getString("version");

        if (configVersion == null)
            config.writeComments();

        if (!pluginVersion.equals(configVersion))
            config.writeComments();
    }

    public void updateInstanceVariable(){
        plugin = this;
    }
}
