package gg.morphie.shophistory.events;

import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.util.AddColor;
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
import java.util.List;
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
        Integer amount = shopPurchaseEvent.getAmount();
        double cost = shopPurchaseEvent.getBalanceWithoutTax();
        UUID uuid = shopPurchaseEvent.getShop().getOwner();
        Player player = Bukkit.getPlayer(shopPurchaseEvent.getPurchaser());
        if (new PlayerDataManager(plugin).getStringList(uuid, "ShopLogger." + shop) != null) {
            List<String> loggedShops = new PlayerDataManager(plugin).getStringList(uuid, "ShopLogger." + shop);
            if (loggedShops.size() <= plugin.getConfig().getInt("Settings.ShopLoggerNum")-1) {
                loggedShops.add(plugin.getMessage("ShopLogger.Log").replace("%BUYER%", player.getName()).replace("%ITEM%", new AddColor().fixCase(item.getType().name())).replace("%AMOUNT%", String.valueOf(amount)).replace("%MONEY_SPENT%", String.valueOf(cost)));
                new PlayerDataManager(plugin).setStringList(uuid, "ShopLogger." + shop, loggedShops);
            } else {
                loggedShops.remove(0);
                deleteLoggedShop(uuid, shop);
                loggedShops.add(plugin.getMessage("ShopLogger.Log").replace("%BUYER%", player.getName()).replace("%ITEM%", new AddColor().fixCase(item.getType().name())).replace("%AMOUNT%", String.valueOf(amount)).replace("%MONEY_SPENT%", String.valueOf(cost)));
                new PlayerDataManager(plugin).setStringList(uuid, "ShopLogger." + shop, loggedShops);
            }
        } else {
            new PlayerDataManager(plugin).setStringList(uuid, "ShopLogger." + shop, Arrays.asList(plugin.getMessage("ShopLogger.Log").replace("%BUYER%", player.getName()).replace("%ITEM%", new AddColor().fixCase(item.getType().name())).replace("%AMOUNT%", String.valueOf(amount)).replace("%MONEY_SPENT%", String.valueOf(cost))));
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onShopDelete(ShopDeleteEvent shopDeleteEvent) {
        Shop shop = shopDeleteEvent.getShop();
        UUID uuid = shopDeleteEvent.getShop().getOwner();
        deleteLoggedShop(uuid, shop);
    }

    public void deleteLoggedShop(UUID uuid, Shop shop) {
        File file = new PlayerDataManager(plugin).getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

        if (fc.contains(shop.toString())) {
            fc.set("ShopLogger." + shop, null);
            try {
                fc.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
