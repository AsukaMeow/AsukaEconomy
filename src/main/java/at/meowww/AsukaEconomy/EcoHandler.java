package at.meowww.AsukaEconomy;

import at.meowww.AsukaEconomy.utils.Handler;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;

public class EcoHandler extends Handler {

    public boolean debug;

    @Override
    public void load (MemorySection config) {
        this.config = config;
        this.enable = config.getBoolean("Enable");
        this.debug = config.getBoolean("Debug");

    }

    @Override
    public MemorySection save () {
        MemorySection config = new MemoryConfiguration();
        config.set("Enable", this.enable);
        config.set("Debug", this.debug);
        return config;
    }
}
