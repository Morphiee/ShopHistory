package gg.morphie.shophistory.events;

import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.util.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.api.event.ShopDeleteEvent;
import org.maxgamer.quickshop.api.event.ShopSuccessPurchaseEvent;
import org.maxgamer.quickshop.api.shop.Shop;
import org.maxgamer.quickshop.api.shop.ShopType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class QuickShopLogger implements Listener {
    private ShopHistory plugin;

    public QuickShopLogger(ShopHistory plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onQuickShopSale(ShopSuccessPurchaseEvent shopPurchaseEvent) {
        if (shopPurchaseEvent.isCancelled()) {
            return;
        }

        Shop shop = shopPurchaseEvent.getShop();
        ShopType shopType = shop.getShopType();
        ItemStack item = shop.getItem();
        double cost = shopPurchaseEvent.getBalanceWithoutTax();
        UUID uuid = shopPurchaseEvent.getShop().getOwner();
        Player player = Bukkit.getPlayer(shopPurchaseEvent.getPurchaser());

        new PlayerDataManager(plugin).setStringList(uuid, shop.toString(), Arrays.asList(player.getName(), String.valueOf(cost), item.getType().name(), String.valueOf(shopType)));
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onShopDelete(ShopDeleteEvent shopDeleteEvent) {

        Shop shop = shopDeleteEvent.getShop();
        UUID uuid = shopDeleteEvent.getShop().getOwner();
        File file = new PlayerDataManager(plugin).getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

        if (fc.contains(shop.toString())) {
            fc.set(shop.toString(), null);
            try {
                fc.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
