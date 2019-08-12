package at.meowww.AsukaEconomy;

import at.meowww.AsukaEconomy.currency.CurrencyHandler;
import at.meowww.AsukaEconomy.invoke.VaultInvoke;
import at.meowww.AsukaEconomy.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AsukaEconomy extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("Minecraft");
    public static JavaPlugin INSTANCE;


    public static VaultInvoke vaultInvoke = new VaultInvoke();

    public static ConfigManager configManager;
    public static CurrencyHandler currencyHandler;

    @Override
    public void onEnable () {
        INSTANCE = this;

        vaultInvoke.invoke();

        configManager =  new ConfigManager(this);
        configManager.loadConfig();
        currencyHandler.load(configManager.currencyConfig);
    }

    @Override
    public void onDisable () {
        configManager.currencyConfig = currencyHandler.save();
        configManager.saveConfig();
    }

}
