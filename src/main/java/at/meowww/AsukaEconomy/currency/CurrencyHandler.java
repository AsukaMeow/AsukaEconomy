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

    public CurrencyChain chain;
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

    public void removePlayerSurplusCurrency(Player player, long amount) {
        for (ItemStack is : filterInventory(player.getInventory())) {
            player.getInventory().remove(is);
        }
        this.givePlayerMissedCurrency(player, amount);
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
                // amount += getBaseValue(is);
                amount += chain.getBaseValue(is);
            }
        }
        return amount;
    }

    private List<Currency> getTopStack(ItemStack is, ArrayList<Currency> concat) {
        Currency c = this.currencies.get(is.getItemMeta().getDisplayName());
        concat.add(c);
        return c.isTop() ? concat : getTopStack(c.getNextItemStack(), concat);
    }

    private boolean currenciesLoad(MemorySection config) {
        if (config.get("Kyc") == null) {
            AsukaEconomy.logger.warning("Can not find Kyc setting in Currency config.");
            return false;
        }
        List list = config.getList("Kyc");
        for (int i = 0; i < list.size(); ++i) {
            Currency c = new Currency((Map) list.get(i));
            currencies.put(c.getName(), c);
        }
        return true;
    }

    private boolean chainLoad () {
        for (Iterator it = this.currencies.keySet().iterator(); it.hasNext();) {
            Currency c = this.currencies.get(it.next());
            if (c.isBase()) {
                ArrayList<Currency> chainList = new ArrayList<>();
                getTopStack(c.getThisItemStack(), chainList);
                // this.chains.add(new CurrencyChain(chainList));
                chain = new CurrencyChain(chainList);
                return true;
            }
        }
        return false;
    }

    @Override
    public void load (MemorySection config) {
        this.config = config;
        this.enable = config.getBoolean("Enable", true);
        this.debug = config.getBoolean("Debug", false);
        this.autoConvert = config.getBoolean("AutoConvert", false);

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
