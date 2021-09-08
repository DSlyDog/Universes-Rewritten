package net.whispwriting.universes.files;

import net.whispwriting.universes.Universes;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigFile extends AbstractFile{

    public ConfigFile (Universes pl){
        super(pl, "config.yml", "");
    }

    public void writeCommentsFistTime() {
        Universes.plugin.perWorldInventories = false;
        Universes.plugin.inventoryGrouping = false;
        Universes.plugin.perWorldStats = false;
        Universes.plugin.useRespawnWorld = false;
        Universes.plugin.trackLastLocation = true;
        Universes.plugin.saveLastLocOnDeath =  true;
        Universes.plugin.prefixChat = false;
        Universes.plugin.useFirstJoinSpawn = false;
        Universes.plugin.worldEntryPermissions = false;
        Universes.plugin.hubOnJoin = false;
        Universes.plugin.netherPerOverworld = true;
        Universes.plugin.toEntryPortal = false;
        Universes.plugin.endPerOverworld = true;
        Universes.plugin.toGroupOnRespawn = true;
        Universes.plugin.usePerWorldTeleportPermissions = false;
        Universes.plugin.perWorldKitGrouping = false;
    }

    @Override
    public void writeComments() {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "# |                         _    _      _                                                       |\n" +
                    "# |                        | |  | |    (_)                                                      |\n" +
                    "# |                        | |  | |_ __  ___   _____ _ __ ___  ___  ___                         |\n" +
                    "# |                        | |  | | '_ \\| \\ \\ / / _ \\ '__/ __|/ _ \\/ __|                        |\n" +
                    "# |                        | |__| | | | | |\\ V /  __/ |  \\__ \\  __/\\__ \\                        |\n" +
                    "# |                         \\____/|_| |_|_| \\_/ \\___|_|  |___/\\___||___/                        |\n" +
                    "# |                                                                                             |\n" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "#Welcome to Universes, and thank you for choosing my plugin!\n" +
                    "#In this config file you will find all of the primary Universe settings.\n" +
                    "#Every setting can be modified and applied immediately with the universes\n" +
                    "#reload command (/universesreload : /ur). No server restarts are necessary.\n" +
                    "\n" +
                    "#Version is simply the current plugin version. This is used by the plugin primarily\n" +
                    "#for updating configuration files when a new version has been installed. For example,\n" +
                    "#it was used to rewrite the config.yml file with these comments in Universes 5.0.\n" +
                    "version: \"5.0\"" +
                    "\n" +
                    "\n" +
                    "#Per-world-inventories is exactly what it says. Turning this option on will\n" +
                    "#give each world its own inventory and swap out players' current inventories\n" +
                    "#when they change worlds. This is false by default.\n" +
                    "per-world-inventories: " + Universes.plugin.perWorldInventories +
                    "\n" +
                    "\n" +
                    "#Per-world-stats will only work if per-world-inventories is enabled. When this\n" +
                    "#option is enabled, each world will have its own set of stats. This includes hunger,\n" +
                    "#health, and xp. This is false by default.\n" +
                    "per-world-stats: " + Universes.plugin.perWorldStats +
                    "\n" +
                    "\n" +
                    "#Per-world-inventory-grouping will only work if per-world-inventories is enabled. This\n" +
                    "#option allows worlds to be grouped, via the groups.yml, so that they share an inventory\n" +
                    "#and stats if per-world-stats is also enabled. Please set the groups in the groups.yml\n" +
                    "#before turning this on. This is false by default.\n" +
                    "per-world-inventory-grouping: " + Universes.plugin.inventoryGrouping +
                    "\n" +
                    "\n" +
                    "#Per-world-kit-grouping will only work if per-world-inventories is enabled. While kits\n" +
                    "#are always enabled and configurable, this option will allow each world to have its own kits.\n" +
                    "#The developer is aware that the system for building kits is complex and verbose, but has not been\n" +
                    "#able to come up with a solution yet. This is false by default.\n" +
                    "per-world-kit-grouping: " + Universes.plugin.perWorldKitGrouping +
                    "\n" +
                    "\n" +
                    "#Use-respawnWorld will control where players are spawned when they die. In the settings.yml file\n" +
                    "#for each world, there is a respawnWorld section. Turning this option on will have players respawn\n" +
                    "#at that world. The respawnWorld world setting is set per world regardless of any configuration options.\n" +
                    "#This is false by default." +
                    "use-respawnWorld: " + Universes.plugin.useRespawnWorld +
                    "\n" +
                    "\n" +
                    "#Use-first-join-spawn is an option that affects where users appear when they join the server for\n" +
                    "#the first time. This is useful for if you want new players to be placed in a tutorial area upon\n" +
                    "#their first join. This location is set via /usetspawn and is stored in the joinspawn.yml.\n" +
                    "#This is false by default." +
                    "use-first-join-spawn: " + Universes.plugin.useFirstJoinSpawn +
                    "\n" +
                    "\n" +
                    "#Track-previous-locations is an option that affects the /universeteleport command. When enabled,\n" +
                    "#a player's location will be saved when they change worlds. When /universeteleport (/ut) us used,\n" +
                    "#the player will be taken back to that location. If the target world has not yet been visited,\n" +
                    "#the player will be sent to the same location that /universespawn (/us) takes them.\n" +
                    "#This is true by default.\n" +
                    "track-previous-locations: " + Universes.plugin.trackLastLocation +
                    "\n" +
                    "\n" +
                    "#Save-location-on-death affects previous location tracking. When this is enabled, the location of\n" +
                    "#a player is saved when they die. After respawning, the /universeteleport (/ut) command will return\n" +
                    "#the player to the location of their death. This is true by default.\n" +
                    "save-location-on-death: " + Universes.plugin.saveLastLocOnDeath +
                    "\n" +
                    "\n" +
                    "#World-entry-permissions is another option that affects teleporting. With this option enabled,\n" +
                    "#players will require a specific permissions to enter worlds. This can be useful for things like\n" +
                    "#VIP or staff only worlds. Permissions for this are as follows: Universes.universe.<world_name>.\n" +
                    "#Currently this is always per world, regardless of whether inventory grouping has been turned on.\n" +
                    "#This is false by default.\n" +
                    "world-entry-permissions: " + Universes.plugin.worldEntryPermissions +
                    "\n" +
                    "\n" +
                    "#Use-per-world-teleport-permissions is another option that affects teleportation. When this option\n" +
                    "#is enabled, the permission checks of /universespawn (/us) and /universeteleport (/ut) are changed\n" +
                    "#so that players can be given permission to teleport to specific worlds. This is like the world entry\n" +
                    "#permissions option, but modifies what the /ut and /us commands look like. The player will only see\n" +
                    "#worlds they have been given access to in the tab list. This is false by default.\n" +
                    "use-per-world-teleport-permissions: " + Universes.plugin.usePerWorldTeleportPermissions +
                    "\n" +
                    "\n" +
                    "#Prefix-chat is an option that will affect chat messages. When enabled, a prefix will appear\n" +
                    "#in front of the player's name. By default it is the world name in brackets (i.e. [world]),\n" +
                    "#but the prefixes are fully customizable in the prefixes.yml file. Prefixes do not necessarily\n" +
                    "#have to be the world name. The prefixes.yml file has examples. This is false by default.\n" +
                    "prefix-chat: " + Universes.plugin.prefixChat +
                    "\n" +
                    "\n" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "# |         _    _       _                           _   _      _   _                           |\n" +
                    "# |        | |  | |     (_)                         | \\ | |    | | | |                          |\n" +
                    "# |        | |  | |_ __  ___   _____ _ __ ___  ___  |  \\| | ___| |_| |__   ___ _ __ ___         |\n" +
                    "# |        | |  | | '_ \\| \\ \\ / / _ \\ '__/ __|/ _ \\ | . ` |/ _ \\ __| '_ \\ / _ \\ '__/ __|        |\n" +
                    "# |        | |__| | | | | |\\ V /  __/ |  \\__ \\  __/ | |\\  |  __/ |_| | | |  __/ |  \\__ \\        |\n" +
                    "# |         \\____/|_| |_|_| \\_/ \\___|_|  |___/\\___| |_| \\_|\\___|\\__|_| |_|\\___|_|  |___/        |\n" +
                    "# |                                                                                             |\n" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "#Universe Nethers is an extension plugin for Universes that will allow overworlds generated with\n" +
                    "#Universes to have their own nethers. This section is for its configuration options. Please be\n" +
                    "#aware, you must have the extension plugin installed in your plugins folder for these options to\n" +
                    "#have any effect.\n" +
                    "\n" +
                    "#Nether-per-overworld is the toggle that completely enables or disables this extension plugin.\n" +
                    "#In order to link an overworld and nether, make sure the name of the nether world is identical\n" +
                    "#to the overworld name with _nether after it. For example, if I have an overworld named Survival,\n" +
                    "#I name the nether world Survival_nether. This is true by default.\n" +
                    "nether-per-overworld: " + Universes.plugin.netherPerOverworld +
                    "\n" +
                    "\n" +
                    "#Return-to-entry-portal is a tweak to the default functionality of nether portals. When this is\n" +
                    "#enabled, players will return to the portal they used to enter the nether when they leave. For\n" +
                    "#example, if they enter the nether at their base and walk 500 blocks, then build a nether portal\n" +
                    "#there to leave, instead of taking them to the relative location in the overworld, the player will\n" +
                    "#come out at their base. This is false by default." +
                    "return-to-entry-portal: " + Universes.plugin.toEntryPortal +
                    "\n" +
                    "\n" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "# |               _    _       _                           ______           _                   |\n" +
                    "# |              | |  | |     (_)                         |  ____|         | |                  |\n" +
                    "# |              | |  | |_ __  ___   _____ _ __ ___  ___  | |__   _ __   __| |___               |\n" +
                    "# |              | |  | | '_ \\| \\ \\ / / _ \\ '__/ __|/ _ \\ |  __| | '_ \\ / _` / __|              |\n" +
                    "# |              | |__| | | | | |\\ V /  __/ |  \\__ \\  __/ | |____| | | | (_| \\__ \\              |\n" +
                    "# |               \\____/|_| |_|_| \\_/ \\___|_|  |___/\\___| |______|_| |_|\\__,_|___/              |\n" +
                    "# |                                                                                             |\n" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "" +
                    "#Universe Ends is an extension plugin for Universes like Universe Nethers. It will allow overworlds\n" +
                    "#generated with Universes to have their own ends. This section is for its configuration options. Please\n" +
                    "#be aware, you must have the extension plugin installed in your plugins folder for these options to\n" +
                    "#have any effect.\n" +
                    "\n" +
                    "#End-per-overworld is a toggle that completely enables or disables this extension plugin. In order\n" +
                    "#to link an overworld and end, make sure the name of the end world is identical to the overworld\n" +
                    "#name with _the_end after it. For example, if I have an overworld named Survival, I name the end\n" +
                    "#end world Survival_the_end. This is true by default.\n" +
                    "end-per-overworld: " + Universes.plugin.endPerOverworld +
                    "\n" +
                    "\n" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "# |   _    _       _                            _____                            _  __          |\n" +
                    "# |  | |  | |     (_)                          / ____|                          (_)/ _|         |\n" +
                    "# |  | |  | |_ __  ___   _____ _ __ ___  ___  | (___  _ __   __ ___      ___ __  _| |_ _   _    |\n" +
                    "# |  | |  | | '_ \\| \\ \\ / / _ \\ '__/ __|/ _ \\  \\___ \\| '_ \\ / _` \\ \\ /\\ / / '_ \\| |  _| | | |   |\n" +
                    "# |  | |__| | | | | |\\ V /  __/ |  \\__ \\  __/  ____) | |_) | (_| |\\ V  V /| | | | | | | |_| |   |\n" +
                    "# |   \\____/|_| |_|_| \\_/ \\___|_|  |___/\\___| |_____/| .__/ \\__,_| \\_/\\_/ |_| |_|_|_|  \\__, |   |\n" +
                    "# |                                                  | |                                __/ |   |\n" +
                    "# |                                                  |_|                               |___/    |\n" +
                    "# |                                                                                             |\n" +
                    "#  ---------------------------------------------------------------------------------------------\n" +
                    "#Universe Spawnify is a new extension that is still in beta. It allows for a spawn point to be set\n" +
                    "#that players can teleport to via /spawn, and it allows for a hub point to be set that players can\n" +
                    "#return to via /hub. This section is for Spawnify's configuration options. Please be aware, you must\n" +
                    "#have the extension plugin installed in your plugins folder for these options to have any effect.\n" +
                    "\n" +
                    "#Rejoin-at-hub is exactly what it says. When enabled, players will be returned to the hub when they\n" +
                    "#log in. This is false by default.\n" +
                    "rejoin-at-hub: " + Universes.plugin.hubOnJoin +
                    "\n" +
                    "\n" +
                    "#Respawn-at-group-spawn is also exactly what it says. When enabled, players will be spawned at the\n" +
                    "#group spawnpoint for the group they are in when they die. This is true by default.\n" +
                    "respawn-at-group-spawn: " + Universes.plugin.toGroupOnRespawn);
            writer.close();
        }catch(IOException e){
            Bukkit.getLogger().log(Level.WARNING, "[Universes] Failed to write comments to config file");
        }
    }
}
