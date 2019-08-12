package at.meowww.AsukaEconomy.currency;

import at.meowww.AsukaEconomy.AsukaEconomy;
import at.meowww.AsukaEconomy.utils.Handler;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CurrencyHandler extends Handler {


    protected CurrencyListener listener;

    public CurrencyChain chain = new CurrencyChain();
    public Map<String, Currency> currencies = new HashMap<>();

    public boolean debug;
    public boolean autoConvert;


    public CurrencyHandler () {
        this.listener = new CurrencyListener(this);
    }

    public void invokePlayerBalance (Player player) {
        long amount = calculateInventoryCurrency(player.getInventory());
        double balance = AsukaEconomy.vaultInvoke.getEconomy().getBalance(player);
        if (balance > amount) {
            AsukaEconomy.vaultInvoke.getEconomy().withdrawPlayer(player, balance - amount);
        } else {
            AsukaEconomy.vaultInvoke.getEconomy().depositPlayer(player, amount - balance);
        }
    }

    public ArrayList<ItemStack> filterInventory (Inventory inv) {
        ArrayList<ItemStack> isl = new ArrayList<>();
        for (ItemStack is : inv.getContents()) {
            if (chain.containCurrency(is)) {
                isl.add(is);
            }
        }
        return isl;
    }

    public void givePlayerMissedCurrency(Player player, long amount) {
        for (ItemStack is : chain.getEqualCurrency(amount)) {
            player.getInventory().addItem(is);
        }
        if (this.autoConvert) {
            this.convertCurrencies (player);
        }
    }

    public void convertCurrencies (Player player) {
        long amount = calculateInventoryCurrency(player.getInventory());
        for (ItemStack is : filterInventory(player.getInventory())) {
            player.getInventory().removeItem(is);
        }
        for (ItemStack is : chain.getEqualCurrency(amount)) {
            player.getInventory().addItem(is);
        }
    }

    public long calculateInventoryCurrency(Inventory inv) {
        long amount = 0;
        for (ItemStack is : inv.getContents()) {
            if (is != null && currencies.containsKey(is.getItemMeta().getDisplayName())) {
                amount += chain.getValue(is);
            }
        }
        return amount;
    }

    private boolean currenciesLoad (MemorySection config) {
        List list = config.getList("CurrencyChain");
        for (int i = 0; i < list.size(); ++i) {
            Map map = (Map) list.get(i);
            Currency c = new Currency(map);
            currencies.put(c.getItemKey(), c);
        }
        return true;
    }

    private void chainLoad () {
        for (Iterator it = this.currencies.keySet().iterator(); it.hasNext();) {
            Currency c = this.currencies.get(it.next());
            Currency n = this.currencies.get(c.getNextItemKey());
            this.chain.addCurrency(c, n);
        }
    }

    @Override
    public void load (MemorySection config) {
        this.config = config;
        this.enable = config.getBoolean("Enable", true);
        this.debug = config.getBoolean("Debug", false);
        this.autoConvert = config.getBoolean("AutoConvert", false);

        if (!this.enable) {
            AsukaEconomy.logger.info("Currency enable is off, currency init will not function.");
        } else {
            this.currenciesLoad(this.config);
            this.chainLoad();
        }
    }

    @Override
    public MemorySection save () {
        MemorySection config = new MemoryConfiguration();
        config.set("Enable", this.enable);
        config.set("Debug", this.debug);
        config.set("AutoConvert", this.autoConvert);
        return config;
    }

}
