package at.meowww.AsukaEconomy.utils;

import at.meowww.AsukaEconomy.AsukaEconomy;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private AsukaEconomy plugin;
    private FileConfiguration config;

    public MemorySection ecoConfig;
    public MemorySection currencyConfig;
    public boolean debug = false;

    public ConfigManager (AsukaEconomy plugin) {
        this.plugin = plugin;
    }

    public void loadConfig () {
        this.config = this.plugin.getConfig();
        this.config.options().copyDefaults(true);
        this.plugin.saveConfig();
        this.ecoConfig = (MemorySection) this.config.get("Economy");
        this.currencyConfig = (MemorySection) this.config.get("Currency");
    }


    public void reloadConfig () {
        this.loadConfig();
    }

    public void saveConfig () {
        this.config.set("Economy", this.ecoConfig);
        this.config.set("Currenct", this.currencyConfig);
        this.plugin.saveConfig();
    }

}
