package at.meowww.AsukaEconomy;

import org.bukkit.configuration.MemorySection;

public class ConfigManager extends at.meowww.AsukaMeow.util.ConfigManager {

    // public MemorySection ecoConfig;
    public MemorySection currencyConfig;
    public boolean debug = false;

    public ConfigManager (AsukaEconomy plugin) {
        super(plugin);
    }

    public void loadConfig () {
        super.loadConfig();
        // this.ecoConfig = (MemorySection) this.config.get("Economy");
        this.currencyConfig = (MemorySection) this.config.get("Currency");
    }


    public void reloadConfig () {
        this.loadConfig();
    }

    public void saveConfig () {
        // this.config.set("Economy", this.ecoConfig);
        // Temporary remove currency saving, becuz it overrides default copy.
        //this.config.set("Currency", this.currencyConfig);
        super.saveConfig();
    }

}
