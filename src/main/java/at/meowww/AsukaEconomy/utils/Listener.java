package at.meowww.AsukaEconomy.utils;


import at.meowww.AsukaEconomy.AsukaEconomy;

import static org.bukkit.Bukkit.getServer;

public class Listener implements org.bukkit.event.Listener {

    public Listener () {}

    public void register () {
        getServer().getPluginManager().registerEvents(this, AsukaEconomy.INSTANCE);
    }

}