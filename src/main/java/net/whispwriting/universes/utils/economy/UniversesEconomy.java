package net.whispwriting.universes.utils.economy;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class UniversesEconomy {

    private static UniversesEconomy instance;
    private Map<String, Map<String, EconomyPlayer>> players = new HashMap<>();

    private UniversesEconomy(){
        instance = this;
    }

    public boolean hasAccount(String world, String s){
        Map<String, EconomyPlayer> worldMap = players.get(world);
        if (worldMap == null)
            return false;
        if (!worldMap.containsKey(s))
            return false;
        return true;
    }

    public boolean hasAccount(OfflinePlayer offlinePlayer){
        return players.containsKey(offlinePlayer.getUniqueId().toString());
    }

    public double getBalance(String world, String s){
        return players.get(world).get(s).getBalance();
    }

    public double getBalance(String world, OfflinePlayer offlinePlayer){
        return players.get(world).get(offlinePlayer.getUniqueId().toString()).getBalance();
    }

    public EconomyResponse depositPlayer(String world, String s, double v){
       return players.get(world).get(s).deposit(v);
    }

    public EconomyResponse depositPlayer(String world, OfflinePlayer offlinePlayer, double v){
        return players.get(world).get(offlinePlayer.getUniqueId().toString()).deposit(v);
    }

    public EconomyResponse withdrawPlayer(String world, String s, double v){
        EconomyPlayer player = players.get(world).get(s);
        if (player.canWithdraw(v))
            return player.withdraw(v);
        else {
            return new EconomyResponse(v, player.getBalance(), EconomyResponse.ResponseType.FAILURE,
                    "Given player lacks sufficient funds");
        }
    }

    public EconomyResponse withdrawPlayer(String world, OfflinePlayer offlinePlayer, double v){
        EconomyPlayer player = players.get(world).get(offlinePlayer.getUniqueId().toString());
        if (player.canWithdraw(v))
            return player.withdraw(v);
        else {
            return new EconomyResponse(v, player.getBalance(), EconomyResponse.ResponseType.FAILURE,
                    "Given player lacks sufficient funds");
        }
    }

    public void createPlayerAccount(String world, String s){
        Map<String, EconomyPlayer> playerAcc = players.get(world);
        if (playerAcc == null){
            playerAcc = new HashMap<>();
            //playerAcc.put(s, new EconomyPlayer(s));
            players.put(world, playerAcc);
        }
        //playerAcc.put(s, new EconomyPlayer(s));
    }

    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    public static UniversesEconomy getInstance(){
        if (instance == null){
            return new UniversesEconomy();
        }
        return instance;
    }
}
