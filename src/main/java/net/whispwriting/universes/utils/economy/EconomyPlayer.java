package net.whispwriting.universes.utils.economy;

import net.milkbowl.vault.economy.EconomyResponse;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.files.PlayerAccountFile;
import net.whispwriting.universes.files.PlayerSettingsFile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class EconomyPlayer {

    private String playerName, worldName;
    private double balance;

    public EconomyPlayer(String playerName, String worldName, double balance){
        this.playerName = playerName;
        this.worldName = worldName;
        this.balance = balance;
    }

    public EconomyPlayer(String playerName, String worldName){
        this.playerName = playerName;
        this.worldName = worldName;
    }

    public void pay(EconomyPlayer player){}

    public void buy(){}

    public EconomyResponse deposit(Double deposit){
        balance += deposit;
        EconomyResponse response = new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "Successful deposit");
        save();
        return response;
    }

    public boolean canWithdraw(Double withdraw){
        return withdraw <= balance;
    }

    public EconomyResponse withdraw(Double withdraw){
        balance -= withdraw;
        EconomyResponse response = new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "Successful withdrawal");
        save();
        return response;
    }

    public double getBalance(){
        return balance;
    }

    public void save(){
        Player player = Bukkit.getPlayer(playerName);
        PlayerAccountFile file = new PlayerAccountFile(Universes.plugin, player.getUniqueId().toString(), worldName);
        file.get().set("balance", balance);
        file.save();
    }
}
