package at.meowww.AsukaEconomy.currency;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CurrencyChain {

    private LinkedList<Currency> chain = new LinkedList<>();
    private ArrayList<Currency> currencies = new ArrayList<>();
    private Map<Long, Currency> amountMap = new LinkedHashMap<>();

    public CurrencyChain () {}

    public void addCurrency (Currency before, Currency next) {
        if (before == null) {
            this.chain.add(next);
        } else {
            ListIterator<Currency> iterator = this.chain.listIterator();
            Currency c;
            while (iterator.hasNext()) {
                c = iterator.next();
                if (c.equals(before)) {
                    iterator.add(next);
                }
            }
        }
        this.currencies.add(next);
    }

    public long getValue (ItemStack is) {
        for (Currency c : currencies) {
            if (c.equals(is)) {
                int count = is.getAmount();
                int index = chain.indexOf(c);
                if (index == 0) {
                    return count;
                } else {
                    return count * c.getMaxAmount() * getValue(chain.get(index + 1).getItemStack());
                }
            }
        }
        return 0L;
    }

    public List<ItemStack> getEqualCurrency (long amount) {
        ArrayList<ItemStack> list = new ArrayList<>();
        ArrayList<Long> reverse = new ArrayList<>(amountMap.keySet());

        for (int i = reverse.size() - 1; i >= 0; i--) {
            long mapAmount = reverse.get(i);
            if (mapAmount <= amount) {
                ItemStack is = amountMap.get(reverse.get(i)).getItemStack().clone();
                is.setAmount((int) (amount / mapAmount));
                amount %= mapAmount;
                list.add(is);
            }
        }
        return list;
    }

    public boolean containCurrency (Item i) {
        return i == null ? false : containCurrency(i.getItemStack());
    }

    public boolean containCurrency (ItemStack is) {
        if (is == null)
            return false;
        for (Currency c : currencies) {
            if (c.getItemStack().getItemMeta().getDisplayName().equals(is.getItemMeta().getDisplayName())) {
                return true;
            }
        }
        return false;
    }

}
