package net.whispwriting.universes.guis;

import com.google.gson.JsonObject;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.events.BlockCommand;
import net.whispwriting.universes.events.ChangePlayerLimit;
import net.whispwriting.universes.events.ChangeRespawnWorld;
import net.whispwriting.universes.events.UnblockCommand;
import net.whispwriting.universes.files.PlayerSettingsFile;
import net.whispwriting.universes.utils.Universe;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class WorldSettingsUI {

    public static Map<Material, UIItemData> items = new HashMap<>();
    public static Inventory inv;
    public static String inventoryString;
    public static int columns = 9;
    public static int rows = columns * 2;
    public static String respawnWorld = "";

    public static void init(){
        inventoryString = Utils.chat("&6&lWorld Settings");
        inv = Bukkit.createInventory(null, rows);
    }

    public static Inventory GUI(Player player, Universe universe, Universes plugin){
        Inventory returnInv = Bukkit.createInventory(null, rows, Utils.chat("&6&l"+universe.serverWorld().getName()+"'s Settings"));

        if (!universe.isAllowPvP()){
            Utils.createItem(inv, Material.DIAMOND_SWORD, 1, 0, "&bPVP", "WhispPVP", Material.DIAMOND_SWORD, items, "&dClick to enable or disable.", "&cPVP is currently disabled.");
        }else{
            Utils.createItem(inv, Material.DIAMOND_SWORD, Enchantment.FIRE_ASPECT, 1, 0, "&bPVP", "WhispPVP", Material.DIAMOND_SWORD, items, "&dClick to enable or disable.", "&2PVP is currently enabled.");
        }

        Utils.createItem(inv, Material.SPAWNER, 1, 1, "&bWorld Spawn", "WhispWS", Material.SPAWNER, items, "&dClick to change the spawn point of the world.");

        if (!universe.isAllowAnimals()){
            Utils.createItem(inv, Material.WOLF_SPAWN_EGG, 1, 2, "&bAllow Animals", "WhispAA", Material.WOLF_SPAWN_EGG, items, "&dClick to enable or disable.", "&callowAnimals is disabled.");
        }else{
            Utils.createItem(inv, Material.WOLF_SPAWN_EGG, Enchantment.MENDING, 1, 2, "&bAllow Animals", "WhispAA", Material.WOLF_SPAWN_EGG, items, "&dClick to enable or disable.", "&2allowAnimals is enabled.");
        }

        if (!universe.isAllowMonsters()){
            Utils.createItem(inv, Material.ZOMBIE_SPAWN_EGG, 1, 3, "&bAllow Monsters", "WhispAA", Material.ZOMBIE_SPAWN_EGG, items, "&dClick to enable or disable.", "&callowMonsters is disabled.");
        }else{
            Utils.createItem(inv, Material.ZOMBIE_SPAWN_EGG, Enchantment.MENDING, 1, 3, "&bAllow Monsters", "WhispAA", Material.ZOMBIE_SPAWN_EGG, items, "&dClick to enable or disable.", "&2allowMonsters is enabled.");
        }

        GameMode mode = universe.gameMode();
        if (mode == GameMode.SURVIVAL){
            List<Material> swapItems = Arrays.asList(Material.BEDROCK, Material.END_CRYSTAL, Material.TORCH);
            Utils.createItem(inv, Material.GRASS_BLOCK, 1, 4, "&bChange GameMode", "WhispGM", swapItems, items, "&dClick to change GameMode.", "&2GameMode is currently Survival.");
        }else if (mode == GameMode.CREATIVE){
            List<Material> swapItems = Arrays.asList(Material.GRASS_BLOCK, Material.END_CRYSTAL, Material.TORCH);
            Utils.createItem(inv, Material.BEDROCK, 1, 4, "&bChange GameMode", "WhispGM", swapItems, items, "&dClick to change GameMode.", "&2GameMode is currently Creative.");
        }else if (mode == GameMode.SPECTATOR){
            List<Material> swapItems = Arrays.asList(Material.BEDROCK, Material.GRASS_BLOCK, Material.TORCH);
            Utils.createItem(inv, Material.END_CRYSTAL, 1, 4, "&bChange GameMode", "WhispGM", swapItems, items, "&dClick to change GameMode.", "&2GameMode is currently Spectator.");
        }else{
            List<Material> swapItems = Arrays.asList(Material.BEDROCK, Material.END_CRYSTAL, Material.GRASS_BLOCK);
            Utils.createItem(inv, Material.TORCH, 1, 4, "&bChange GameMode", "WhispGM", swapItems, items, "&dClick to change GameMode.", "&2GameMode is currently Adventure.");
        }

        String currentWorld = universe.respawnWorld();
        Utils.createItem(inv, Material.OAK_SAPLING, 1, 5, "&bRespawn World", "WhispRS", Material.OAK_SAPLING, items, "&dClick to change the respawn world.", "&2Respawn world is currently: "+currentWorld+".");

        int playerLimit = universe.maxPlayers();
        Utils.createItem(inv, Material.PLAYER_HEAD, 1, 6, "&bPlayer Limit", "WhispPL", Material.PLAYER_HEAD, items, "&dClick to change the player limit.", "&2Player limit is currently: "+playerLimit+".");

        if (!universe.isAllowFlight()){
            Utils.createItem(inv, Material.ELYTRA, 1, 7, "&bAllow Flight", "WhispFly", Material.ELYTRA, items, "&dClick to enable or disable.", "&callowFlight is currently disabled.");
        }else{
            Utils.createItem(inv, Material.ELYTRA, Enchantment.MENDING,1, 7, "&bAllow Flight", "WhispFly", Material.ELYTRA, items, "&dClick to enable or disable.", "&2allowFlight is currently enabled.");
        }

        Difficulty difficulty = universe.getDifficulty();
        if (difficulty == Difficulty.PEACEFUL){
            List<Material> swapItems = Arrays.asList(Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD);
            Utils.createItem(inv, Material.WOODEN_SWORD, 1, 8, "&bDifficulty", "WhispDiff", swapItems, items, "&dClick to change difficulty.", "&2Difficulty is currently: "+difficulty.name().toLowerCase()+".");
        }else if (difficulty == Difficulty.EASY){
            List<Material> swapItems = Arrays.asList(Material.WOODEN_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD);
            Utils.createItem(inv, Material.STONE_SWORD, 1, 8, "&bDifficulty", "WhispDiff", swapItems, items, "&dClick to change difficulty.", "&2Difficulty is currently: "+difficulty.name().toLowerCase()+".");
        }else if (difficulty == Difficulty.NORMAL){
            List<Material> swapItems = Arrays.asList(Material.STONE_SWORD, Material.WOODEN_SWORD, Material.GOLDEN_SWORD);
            Utils.createItem(inv, Material.IRON_SWORD, 1, 8, "&bDifficulty", "WhispDiff", swapItems, items, "&dClick to change difficulty.", "&2Difficulty is currently: "+difficulty.name().toLowerCase()+".");
        }else{
            List<Material> swapItems = Arrays.asList(Material.STONE_SWORD, Material.IRON_SWORD, Material.WOODEN_SWORD);
            Utils.createItem(inv, Material.GOLDEN_SWORD, 1, 8, "&bDifficulty", "WhispDiff", swapItems, items, "&dClick to change difficulty.", "&2Difficulty is currently: "+difficulty.name().toLowerCase()+".");
        }

        Utils.createItem(inv, Material.BARRIER, 1, 9, "&bBlock Command", "WhispBC", Material.BARRIER, items, "&dClick to block a command.");
        Utils.createItem(inv, Material.SLIME_BALL, 1, 10, "&bUnblock Command", "WhispUbC", Material.SLIME_BALL, items, "&dClick to unblock a command.");

        returnInv.setContents(inv.getContents());
        return returnInv;
    }

    public static void clicked(Player player, Universes plugin, ItemStack clicked, String worldName){
        Universe universe = plugin.universes.get(worldName);
        if(clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bPVP"))){
            if (universe.isAllowPvP()){
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&cPVP is currently disabled."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.removeEnchantment(Enchantment.FIRE_ASPECT);
                universe.allowPvP(false);
                player.sendMessage(Utils.chat("&cPVP is no longer allowed in "+Utils.chat("&4"+worldName)));
            }else{
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2PVP is currently enabled."));
                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                clicked.setItemMeta(meta);
                clicked.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
                universe.allowPvP(true);
                player.sendMessage(Utils.chat("&2PVP is now allowed in "+Utils.chat("&a"+worldName)));
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bWorld Spawn"))){
            Location loc = player.getLocation();
            JsonObject locJson = new JsonObject();
            locJson.addProperty("name", loc.getWorld().getName());
            locJson.addProperty("x", loc.getX());
            locJson.addProperty("y", loc.getY());
            locJson.addProperty("z", loc.getZ());
            locJson.addProperty("yaw", loc.getYaw());
            locJson.addProperty("pitch", loc.getPitch());
            universe.setSpawn(loc);
            player.sendMessage(Utils.chat("&2Spawn point for world &a"+worldName+" &2has been set to where you stand."));
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bAllow Animals"))){
            if (universe.isAllowAnimals()){
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&callowAnimals is disabled."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.removeEnchantment(Enchantment.MENDING);
                universe.allowAnimals(false);
                List<Entity> entities = universe.serverWorld().getEntities();
                for (Entity e : entities){
                    if (e instanceof Animals)
                        if (e instanceof Tameable) {
                            if (!((Tameable) e).isTamed())
                                e.remove();
                        }else {
                            e.remove();
                        }
                }
                player.sendMessage(Utils.chat("&cAnimals are no longer allowed in this world."));
            }else{
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2allowAnimals is enabled."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.addUnsafeEnchantment(Enchantment.MENDING, 2);
                universe.allowAnimals(true);
                player.sendMessage(Utils.chat("&2Animals are now allowed in this world."));
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bAllow Monsters"))){
            if (universe.isAllowMonsters()){
                universe.allowMonsters(false);
                List<Entity> entities = universe.serverWorld().getEntities();
                for (Entity e : entities){
                    if (e instanceof Monster || e instanceof Slime){
                        e.remove();
                    }
                }
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add("&callowMonsters is disabled.");
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.removeEnchantment(Enchantment.MENDING);
                player.sendMessage(Utils.chat("&callowMonsters has been disabled."));
            }else{
                universe.allowMonsters(true);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add("&2allowMonsters is enabled.");
                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                clicked.setItemMeta(meta);
                clicked.addUnsafeEnchantment(Enchantment.MENDING, 2);
                player.sendMessage(Utils.chat("&2allowMonsters has been enabled."));
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bChange GameMode"))){
            GameMode mode = universe.gameMode();
            if (mode == GameMode.SURVIVAL){
                universe.setGameMode(GameMode.CREATIVE);
                updateGameMode(GameMode.CREATIVE, universe);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2GameMode is currently Creative."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.setType(Material.BEDROCK);
                player.sendMessage(Utils.chat("&2GameMode has been changed to Creative."));
            }else if (mode == GameMode.CREATIVE){
                universe.setGameMode(GameMode.SPECTATOR);
                updateGameMode(GameMode.SPECTATOR, universe);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2GameMode is currently Spectator."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.setType(Material.END_CRYSTAL);
                player.sendMessage(Utils.chat("&2GameMode has been changed to Spectator."));
            }else if (mode == GameMode.SPECTATOR){
                universe.setGameMode(GameMode.ADVENTURE);
                updateGameMode(GameMode.ADVENTURE, universe);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2GameMode is currently Adventure."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.setType(Material.TORCH);
                player.sendMessage(Utils.chat("&2GameMode has been changed to Adventure."));
            }else{
                universe.setGameMode(GameMode.SURVIVAL);
                updateGameMode(GameMode.SURVIVAL, universe);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2GameMode is currently Survival."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.setType(Material.GRASS_BLOCK);
                player.sendMessage(Utils.chat("&2GameMode has been changed to Survival."));
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bRespawn World"))){
            player.closeInventory();
            Bukkit.getPluginManager().registerEvents(new ChangeRespawnWorld(player.getUniqueId().toString(), plugin, universe), plugin);
            player.sendMessage(Utils.chat("&2Please enter the name of the new respawn world."));
        }else if(clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bPlayer Limit"))){
            player.closeInventory();
            Bukkit.getPluginManager().registerEvents(new ChangePlayerLimit(player.getUniqueId().toString(), plugin, universe), plugin);
            player.sendMessage(Utils.chat("&2Please enter a number for the new player limit."));
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bAllow Flight"))){
            if (universe.isAllowFlight()){
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&callowFlight is currently disabled."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                universe.allowFlight(false);
                clicked.removeEnchantment(Enchantment.MENDING);
                player.sendMessage(Utils.chat("&cFlight has been disabled in this world."));
                Collection<Player> players = universe.serverWorld().getPlayers();
                for (Player p : players){
                    if (p.isFlying()){
                        PlayerSettingsFile playerSettingsFile = new PlayerSettingsFile(plugin, p.getUniqueId().toString());;
                        boolean flightOverride = playerSettingsFile.get().getBoolean("flightOverride");
                        if (!flightOverride) {
                            p.setFlying(false);
                            p.sendMessage(Utils.chat("&cFlying has been disabled in this world."));
                        }
                    }
                }
            }else{
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2allowFlight is currently enabled."));
                meta.setLore(lore);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                clicked.setItemMeta(meta);
                universe.allowFlight(true);
                clicked.addUnsafeEnchantment(Enchantment.MENDING, 2);
                player.sendMessage(Utils.chat("&2Flight has been enabled in this world."));
            }
        }else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&bDifficulty"))){
            Difficulty difficulty = universe.getDifficulty();
            if (difficulty == Difficulty.PEACEFUL){
                universe.setDifficulty(Difficulty.EASY);
                clicked.setType(Material.STONE_SWORD);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2Difficulty is currently: easy."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
            }else if (difficulty == Difficulty.EASY){
                universe.setDifficulty(Difficulty.NORMAL);
                clicked.setType(Material.IRON_SWORD);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2Difficulty is currently: normal."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
            }else if (difficulty == Difficulty.NORMAL){
                universe.setDifficulty(Difficulty.HARD);
                clicked.setType(Material.GOLDEN_SWORD);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2Difficulty is currently: hard."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
            }else{
                universe.setDifficulty(Difficulty.PEACEFUL);
                clicked.setType(Material.WOODEN_SWORD);
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2Difficulty is currently: peaceful."));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
            }
        }else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&bBlock Command"))){
            player.closeInventory();
            Bukkit.getPluginManager().registerEvents(new BlockCommand(player.getUniqueId().toString(), plugin, universe), plugin);
            player.sendMessage(Utils.chat("&2Please enter a command to block. Do not include the slash."));
        }else if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&bUnblock Command"))){
            player.closeInventory();
            if (universe.blockedCommands().size() == 0){
                player.sendMessage(ChatColor.RED + "There are no blocked commands in this world.");
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (int i=0; i<universe.blockedCommands().size(); i++){
                if (i == universe.blockedCommands().size() - 1)
                    builder.append(universe.blockedCommands().get(i));
                else
                    builder.append(universe.blockedCommands().get(i)).append("\n");
            }
            player.sendMessage(ChatColor.DARK_GREEN + "Blocked commands:\n" + ChatColor.GOLD + builder.toString());
            Bukkit.getPluginManager().registerEvents(new UnblockCommand(player.getUniqueId().toString(), plugin, universe), plugin);
            player.sendMessage(Utils.chat("&2Please enter a command to unblock. Do not include the slash."));
        }
    }

    private static void updateGameMode(GameMode mode, Universe universe){
        Collection<Player> players = universe.serverWorld().getPlayers();
        for (Player player : players){
            player.setGameMode(mode);
        }
    }
}
