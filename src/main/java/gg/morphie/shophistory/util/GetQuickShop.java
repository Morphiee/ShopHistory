package gg.morphie.shophistory.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.maxgamer.quickshop.api.QuickShopAPI;

import java.util.UUID;

public class GetQuickShop {

    Plugin quickshopPlugin = Bukkit.getPluginManager().getPlugin("QuickShop");

    public QuickShopAPI getQuickShopAPI() {
        if(quickshopPlugin != null && quickshopPlugin.isEnabled()){
            QuickShopAPI quickshopApi = (QuickShopAPI)quickshopPlugin;
            return quickshopApi;
        }
        return null;
    }

    public String getOwner(UUID uuid, int index) {
        Player player = Bukkit.getPlayer(uuid);
        String owner = player.getDisplayName();
        return owner;
    }

    public ItemStack getItem(UUID uuid, int index) {
        Player player = Bukkit.getPlayer(uuid);
        return new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(index).getItem();
    }

    public String getStock(UUID uuid, int index) {
        Player player = Bukkit.getPlayer(uuid);
        String stock = String.valueOf(new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(index).getRemainingStock());
        return stock;
    }

    public String getPrice(UUID uuid, int index) {
        Player player = Bukkit.getPlayer(uuid);
        String price = "$" + (String.valueOf(new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(index).getPrice()));
        return price;
    }

    public String getType(UUID uuid, int index) {
        Player player = Bukkit.getPlayer(uuid);
        String type = new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(index).getShopType().toString();
        return type;
    }

    public Location getLocation(UUID uuid, int index) {
        Location loc = new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(index).getLocation();
        return loc;
    }

    public org.maxgamer.quickshop.api.shop.Shop getShopByIndex(UUID uuid, int index) {
        return new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(index);
    }
}
