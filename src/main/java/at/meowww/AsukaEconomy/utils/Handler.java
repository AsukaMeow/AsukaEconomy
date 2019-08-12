package at.meowww.AsukaEconomy.utils;

import at.meowww.AsukaEconomy.AsukaEconomy;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.HandlerList;

import static org.bukkit.Bukkit.getServer;

public class Handler {

    public Listener listener;

    public MemorySection config;
    public boolean enable;

    public Handler () {}

    public void load (MemorySection config) {}

    public MemorySection save () {
        return null;
    }

    public void toggleRegister () {
        if (this.enable) {
            getServer().getPluginManager().registerEvents(this.listener, AsukaEconomy.INSTANCE);
        } else {
            HandlerList.unregisterAll(this.listener);
        }
    }

}
