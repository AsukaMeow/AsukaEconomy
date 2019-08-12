package at.meowww.AsukaEconomy.currency;

import at.meowww.AsukaEconomy.AsukaEconomy;
import at.meowww.AsukaEconomy.utils.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static org.bukkit.Bukkit.getServer;

public class CurrencyListener extends Listener {
    
    private CurrencyHandler currencyHandler = null;

    public CurrencyListener (CurrencyHandler handler) {
        this.currencyHandler = handler;
        getServer().getPluginManager().registerEvents(this, AsukaEconomy.INSTANCE);
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        getServer().getScheduler().scheduleSyncDelayedTask(AsukaEconomy.INSTANCE, () -> {
            try {
                currencyHandler.invokePlayerBalance((Player) event.getPlayer());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5L);
    }

    @EventHandler
    public void onPlayerOpenInventory (InventoryOpenEvent event) {
        currencyHandler.invokePlayerBalance((Player) event.getPlayer());
    }

    @EventHandler
    public void onPlayerCloseInventory (InventoryCloseEvent event) {
        currencyHandler.invokePlayerBalance((Player) event.getPlayer());
    }

    @EventHandler
    public void onPlayerDropItem (PlayerDropItemEvent event) {
        if (currencyHandler.chain.containCurrency(event.getItemDrop())) {
            currencyHandler.invokePlayerBalance(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerPickItem (EntityPickupItemEvent event) {
        getServer().getScheduler().scheduleSyncDelayedTask(AsukaEconomy.INSTANCE, () -> {
            try {
                if (event.getEntity() instanceof Player && currencyHandler.chain.containCurrency(event.getItem())) {
                    Player player = (Player) event.getEntity();
                    currencyHandler.invokePlayerBalance(player);
                    if (currencyHandler.autoConvert) {
                        currencyHandler.convertCurrencies(player);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5L);//20L = 1 sec
    }

    @EventHandler
    public void onPlayerUseItem (PlayerInteractEvent event) {
        try {
            if (currencyHandler.chain.containCurrency(event.getItem())) {
                currencyHandler.invokePlayerBalance((Player) event.getPlayer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
