package at.meowww.AsukaEconomy.currency;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class Currency {

    private ItemStack itemStack;
    private String thisItemKey, nextItemKey;
    private long maxAmount = 0;

    public Currency (Map map) {
        this(map.get("ItemKey").toString(), Material.valueOf(map.get("Material").toString()),
                map.get("DisplayName").toString(), (List<String>) map.get("Lore"),
                map.containsKey("NextCurrency") ? map.get("NextCurrency").toString() : null);
    }

    public Currency (String thisItemKey, Material material, String displayName, List<String> lore, String nextItemKey) {
        this.thisItemKey = thisItemKey;
        this.nextItemKey = nextItemKey;
        this.itemStack = new ItemStack(material);
        this.itemStack.getItemMeta().setDisplayName(displayName);
        this.itemStack.setLore(lore);
    }

    public ItemStack getItemStack () {
        return this.itemStack;
    }

    public String getItemKey () {
        return this.thisItemKey;
    }

    public String getNextItemKey () {
        return this.nextItemKey;
    }

    public long getMaxAmount () {
        return this.maxAmount;
    }

    public boolean equals (ItemStack is) {
        return is.equals(this.itemStack);
    }

    public boolean equals (Currency c) {
        return c.itemStack.equals(this.itemStack) && c.thisItemKey.equals(this.thisItemKey) &&
                c.nextItemKey.equals(this.nextItemKey);
    }

}
