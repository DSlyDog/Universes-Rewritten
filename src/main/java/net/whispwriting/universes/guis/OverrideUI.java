package net.whispwriting.universes.guis;

import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerSettingsFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverrideUI {

    public static Map<Material, UIItemData> items = new HashMap<>();
    public static Inventory inv;
    public static String inventoryName;
    public static int boxes = 9;
    public static int rows = boxes * 1;
    public static int i = -1;

    public static void init(){
        inventoryName = Utils.chat("&2&lOverrides");

        inv = Bukkit.createInventory(null, rows);
    }

    public static Inventory GUI(Player p, Universes plugin){
        Inventory toReturn = Bukkit.createInventory(null, rows, inventoryName);
        PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, p.getUniqueId().toString());;
        if (p.hasPermission("Universes.override.gamemode")) {
            i++;
            boolean gameModeOverride = playerSettings.get().getBoolean("gameModeOverride");
            if (!gameModeOverride) {
                Utils.createItem(inv, Material.GRASS_BLOCK, 1, i, "&bGameMode Override", "WhispGMO", Material.GRASS_BLOCK, items, "&dClick to enable or disable.", "&cGameModeOverride is disabled.");
            } else {
                Utils.createItem(inv, Material.GRASS_BLOCK, Enchantment.MENDING, 1, i, "&bGameMode Override", "WhispGMO", Material.GRASS_BLOCK, items, "&dClick to enable or disable.", "&2GameModeOverride is enabled.");
            }
        }

        if (p.hasPermission("Universes.override.fullworld")) {
            i++;
            boolean canJoinFullWorlds = playerSettings.get().getBoolean("canJoinFullWorlds");
            if (!canJoinFullWorlds) {
                Utils.createItem(inv, Material.BEDROCK, 1, i, "&bFull World Override", "WhispFWO", Material.BEDROCK, items, "&dClick to enable or disable.", "&cFullWorldOverride is disabled.");
            } else {
                Utils.createItem(inv, Material.BEDROCK, Enchantment.MENDING, 1, i, "&bFull World Override", "WhispFWO", Material.BEDROCK, items, "&dClick to enable or disable.", "&2FullWorldOverride is enabled.");
            }
        }

        if (p.hasPermission("Universes.override.flight")) {
            i++;
            boolean flightOverride = playerSettings.get().getBoolean("flightOverride");
            if (!flightOverride) {
                Utils.createItem(inv, Material.ELYTRA, 1, i, "&bFlight Override", "WhispFW", Material.ELYTRA, items, "&dClick to enable or disable.", "&cFlightOverride is disabled.");
            } else {
                Utils.createItem(inv, Material.ELYTRA, Enchantment.MENDING, 1, i, "&bFlight Override", "WhispFW", Material.ELYTRA, items, "&dClick to enable or disable.", "&2FlightOverride is enabled.");
            }
        }

        if (p.hasPermission("Universes.override.perworldinv")) {
            i++;
            boolean perWorldInvOverride = playerSettings.get().getBoolean("perWorldInvOverride");
            if (!perWorldInvOverride) {
                Utils.createItem(inv, Material.CRAFTING_TABLE, 1, i, "&bPer World Inventory Override", "WhispPWIO", Material.CRAFTING_TABLE, items, "&dClick to enable or disable.", "&cPerWorldInventoryOverride is disabled.");
            } else {
                Utils.createItem(inv, Material.CRAFTING_TABLE, Enchantment.MENDING, 1, i, "&bPer World Inventory Override", "WhispPWIO", Material.CRAFTING_TABLE, items, "&dClick to enable or disable.", "&2PerWorldInventoryOverride is enabled.");
            }
        }

        if (p.hasPermission("Universes.override.blockedCommands")){
            i++;
            boolean overrideBlockedCommands = playerSettings.get().getBoolean("blockedCommandsOverride");
            if (!overrideBlockedCommands){
                Utils.createItem(inv, Material.BARRIER, 1, i, "&bOverride Blocked Commands", "WhispBCO", Material.BARRIER, items, "&dClick to enable or disable.", "&cBlocked Commands Override is disabled.");
            }else{
                Utils.createItem(inv, Material.BARRIER, Enchantment.MENDING, 1, i, "&bOverride Blocked Commands", "WhispBCO", Material.BARRIER, items, "&dClick to enable or disable.", "&cBlocked Commands Override is disabled.");
            }
        }

        toReturn.setContents(inv.getContents());
        return toReturn;
    }

    public static void clickItem(Player p, int slot, ItemStack clicked, Inventory inventory, Universes plugin){

        if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bGameMode Override"))){
            PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, p.getUniqueId().toString());;
            boolean gameModeOverride = playerSettings.get().getBoolean("gameModeOverride");
            if (p.hasPermission("Universes.override.gamemode")) {
                if (gameModeOverride) {
                    playerSettings.get().set("gameModeOverride", false);
                    playerSettings.save();
                    //p.closeInventory();
                    ItemMeta meta = clicked.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&cGameMode Override is disabled"));
                    meta.setLore(lore);
                    clicked.setItemMeta(meta);
                    clicked.removeEnchantment(Enchantment.MENDING);
                    p.sendMessage(ChatColor.RED + "GameMode Override has been disabled.");
                } else {
                    playerSettings.get().set("gameModeOverride", true);
                    playerSettings.save();
                    //p.closeInventory();
                    ItemMeta meta = clicked.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&2GameMode Override is enabled"));
                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    clicked.setItemMeta(meta);
                    clicked.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    p.sendMessage(ChatColor.DARK_GREEN + "GameMode Override has been enabled.");
                }
            }else{
                p.sendMessage(ChatColor.DARK_RED + "You do not have permission to change that setting.");
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bFull World Override"))){
            PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, p.getUniqueId().toString());;
            boolean canJoinFullWorlds = playerSettings.get().getBoolean("canJoinFullWorlds");
            if (p.hasPermission("Universes.override.fullworld")){
                if (canJoinFullWorlds) {
                    playerSettings.get().set("canJoinFullWorlds", false);
                    playerSettings.save();
                    //p.closeInventory();
                    ItemMeta meta = clicked.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&cFull World Override is disabled"));
                    meta.setLore(lore);
                    clicked.setItemMeta(meta);
                    clicked.removeEnchantment(Enchantment.MENDING);
                    p.sendMessage(ChatColor.RED + "Full World Override has been disabled.");
                } else {
                    playerSettings.get().set("canJoinFullWorlds", true);
                    playerSettings.save();
                    //p.closeInventory();
                    ItemMeta meta = clicked.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&2Full World Override is enabled"));
                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    clicked.setItemMeta(meta);
                    clicked.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    p.sendMessage(ChatColor.DARK_GREEN + "Full World Override has been enabled.");
                }
            }else{
                p.sendMessage(ChatColor.DARK_RED + "You do not have permission to change that setting.");
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bFlight Override"))){
            PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, p.getUniqueId().toString());;
            boolean flightOverride = playerSettings.get().getBoolean("flightOverride");
            if (p.hasPermission("Universes.override.flight")){
                if (flightOverride) {
                    playerSettings.get().set("flightOverride", false);
                    playerSettings.save();
                    //p.closeInventory();
                    ItemMeta meta = clicked.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&cFlight Override is disabled"));
                    meta.setLore(lore);
                    meta.removeEnchant(Enchantment.MENDING);
                    clicked.setItemMeta(meta);
                    //p.openInventory(GUI(p, plugin));
                    p.sendMessage(ChatColor.RED + "Flight Override has been disabled.");
                } else {
                    playerSettings.get().set("flightOverride", true);
                    playerSettings.save();
                    //p.closeInventory();
                    clicked.removeEnchantment(Enchantment.MENDING);
                    ItemMeta meta = clicked.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&2Flight Override is enabled"));
                    meta.setLore(lore);
                    meta.addEnchant(Enchantment.MENDING, 1, true);
                    clicked.setItemMeta(meta);
                    //p.openInventory(GUI(p, plugin));
                    p.sendMessage(ChatColor.DARK_GREEN + "Flight Override has been enabled.");
                }
            }else{
                p.sendMessage(ChatColor.DARK_RED + "You do not have permission to change that setting.");
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bPer World Inventory Override"))){
            PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, p.getUniqueId().toString());;
            boolean perWorldInvOverride = playerSettings.get().getBoolean("perWorldInvOverride");
            if (p.hasPermission("Universes.override.perworldinv")){
                if (perWorldInvOverride) {
                    playerSettings.get().set("perWorldInvOverride", false);
                    playerSettings.save();
                    //p.closeInventory();
                    ItemMeta meta = clicked.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&cPer World Inventory Override is disabled"));
                    meta.setLore(lore);
                    clicked.setItemMeta(meta);
                    clicked.removeEnchantment(Enchantment.MENDING);
                    //p.openInventory(GUI(p, plugin));
                    p.sendMessage(ChatColor.RED + "Per World Inventory Override has been disabled.");
                } else {
                    playerSettings.get().set("perWorldInvOverride", true);
                    playerSettings.save();
                    //p.closeInventory();
                    ItemMeta meta = clicked.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.remove(1);
                    lore.add(Utils.chat("&2Per World Inventory Override is enabled"));
                    meta.setLore(lore);
                    clicked.setItemMeta(meta);
                    clicked.addUnsafeEnchantment(Enchantment.MENDING, 1);
                    //p.openInventory(GUI(p, plugin));
                    p.sendMessage(ChatColor.DARK_GREEN + "Pe rWorld Inventory Override has been enabled.");
                }
            }else{
                p.sendMessage(ChatColor.DARK_RED + "You do not have permission to change that setting.");
            }
        }else if (clicked.getItemMeta().getDisplayName().equals(Utils.chat("&bOverride Blocked Commands"))){
            PlayerSettingsFile playerSettings = new PlayerSettingsFile(plugin, p.getUniqueId().toString());;
            boolean overrideBlockedCommands = playerSettings.get().getBoolean("blockedCommandsOverride");
            if (overrideBlockedCommands) {
                playerSettings.get().set("blockedCommandsOverride", false);
                playerSettings.save();
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&cBlocked Commands Override is disabled"));
                meta.removeEnchant(Enchantment.MENDING);
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                p.sendMessage(ChatColor.RED + "Blocked Commands Override has been disabled.");
            }else{
                playerSettings.get().set("blockedCommandsOverride", true);
                playerSettings.save();
                ItemMeta meta = clicked.getItemMeta();
                List<String> lore = meta.getLore();
                lore.remove(1);
                lore.add(Utils.chat("&2Blocked Commands Override is enabled"));
                meta.setLore(lore);
                clicked.setItemMeta(meta);
                clicked.addUnsafeEnchantment(Enchantment.MENDING, 1);
                p.sendMessage(ChatColor.DARK_GREEN + "Blocked Commands Override has been enabled.");
            }
        }

    }

}
