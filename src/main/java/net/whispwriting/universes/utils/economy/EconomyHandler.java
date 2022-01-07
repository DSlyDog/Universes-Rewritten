package net.whispwriting.universes.utils.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.whispwriting.universes.Universes;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class EconomyHandler implements Economy {

    private DecimalFormat formatter = new DecimalFormat("###.##");
    private Map<String, Map<String, EconomyPlayer>> players = new HashMap<>();
    private Map<String, BankAccount> accounts = new HashMap<>();
    private String currentWorld;

    @Override
    public boolean isEnabled() {
        return Universes.plugin.useEconomy;
    }

    @Override
    public String getName() {
        return "Universes Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return Universes.plugin.currencyIndicator + formatter.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return Universes.plugin.currencyPlural;
    }

    @Override
    public String currencyNameSingular() {
        return Universes.plugin.currencySingular;
    }

    @Override
    public boolean hasAccount(String playerName) {
        checkCurrentWorld(Bukkit.getPlayer(playerName));

        if (!players.containsKey(currentWorld))
            return false;

        Map<String, EconomyPlayer> accounts = players.get(currentWorld);
        return accounts.containsKey(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return hasAccount(offlinePlayer.getPlayer().getName());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        this.currentWorld = worldName;
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        this.currentWorld = worldName;
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName) {
        checkCurrentWorld(Bukkit.getPlayer(playerName));
        return players.get(currentWorld).get(playerName).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return getBalance(offlinePlayer.getName());
    }

    @Override
    public double getBalance(String playerName, String world) {
        this.currentWorld = world;
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        this.currentWorld = world;
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String playerName, double amount) {
        checkCurrentWorld(Bukkit.getPlayer(playerName));
        EconomyPlayer player = players.get(currentWorld).get(playerName);
        return player.canWithdraw(amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return has(offlinePlayer.getName(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        this.currentWorld = worldName;
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        this.currentWorld = worldName;;
        return has(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        checkCurrentWorld(Bukkit.getPlayer(playerName));
        EconomyPlayer player = players.get(currentWorld).get(playerName);
        if (player.canWithdraw(amount)) {
            return player.withdraw(amount);
        } else {
            return new EconomyResponse(amount, player.getBalance(), EconomyResponse.ResponseType.FAILURE,
                    "Given player lacks sufficient funds");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        checkCurrentWorld(offlinePlayer);
        EconomyPlayer player = players.get(currentWorld).get(offlinePlayer.getName());
        if (player.canWithdraw(amount)) {
            return player.withdraw(amount);
        } else {
            return new EconomyResponse(amount, player.getBalance(), EconomyResponse.ResponseType.FAILURE,
                    "Given player lacks sufficient funds");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        this.currentWorld = worldName;
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        this.currentWorld = worldName;
        return withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return players.get(currentWorld).get(playerName).deposit(amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        Bukkit.getLogger().log(Level.INFO, "current world (dP): " + currentWorld);
        checkCurrentWorld(offlinePlayer);
        Bukkit.getLogger().log(Level.INFO, "current world (dP): " + currentWorld);
        return players.get(currentWorld).get(offlinePlayer.getName()).deposit(amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        this.currentWorld = worldName;
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String playerName) {
        if (accounts.containsKey(name)){
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                    "A bank already exists with that name");
        }
        accounts.put(name, new BankAccount(playerName));
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS,
                "Bank created");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer offlinePlayer) {
        return createBank(offlinePlayer.getPlayer().getName(), name);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        if (accounts.containsKey(name)){
            accounts.remove(name);
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS,
                    "Bank removed");
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                "Bank not found with that name");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        if (accounts.containsKey(name)){
            BankAccount account = accounts.get(name);
            return new EconomyResponse(0, account.getBalance(), EconomyResponse.ResponseType.SUCCESS,
                    "Current bank account balance: ");
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE,
                "Bank not found");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        Bukkit.getLogger().log(Level.INFO, "current world (cPA): " + currentWorld);
        checkCurrentWorld(Bukkit.getPlayer(playerName));
        Bukkit.getLogger().log(Level.INFO, "current world (cPA): " + currentWorld);
        if (!players.containsKey(currentWorld)){
            Map<String, EconomyPlayer> accounts = new HashMap<>();
            accounts.put(playerName, new EconomyPlayer(playerName, currentWorld));
            players.put(currentWorld, accounts);
        }else{
            players.get(currentWorld).put(playerName, new EconomyPlayer(playerName, currentWorld));
        }
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return createPlayerAccount(offlinePlayer.getName());
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        this.currentWorld = worldName;
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        this.currentWorld = worldName;
        return createPlayerAccount(offlinePlayer);
    }

    public void checkCurrentWorld(OfflinePlayer player){
        if (currentWorld == null) {
            if (Universes.plugin.inventoryGrouping)
                currentWorld = Universes.plugin.groups.get(player.getPlayer().getLocation().getWorld().getName());
            else
                currentWorld = player.getPlayer().getLocation().getWorld().getName();
        }else{
            if (Universes.plugin.inventoryGrouping)
                if (Universes.plugin.groups.containsKey(currentWorld))
                    currentWorld = Universes.plugin.groups.get(currentWorld);
                else if (Universes.plugin.groups.containsValue(currentWorld))
                    return;
                else
                    currentWorld = Universes.plugin.groups.get(player.getPlayer().getLocation().getWorld().getName());
        }
    }
}
