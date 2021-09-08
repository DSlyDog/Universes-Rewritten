package net.whispwriting.universes.utils;

import net.milkbowl.vault.economy.Economy;
import net.whispwriting.universes.Universes;
import net.whispwriting.universes.utils.economy.EconomyHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Level;

public class VaultHook {

    private Economy provider;

    public void hook(){
        provider = new EconomyHandler();
        Bukkit.getServicesManager().register(Economy.class, this.provider, Universes.plugin, ServicePriority.Normal);
        Bukkit.getLogger().log(Level.FINE, "Economy registered");
    }

    public void unhook(){
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
    }
}
