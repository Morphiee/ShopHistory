package gg.morphie.shophistory.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.maxgamer.quickshop.api.QuickShopAPI;

import java.util.UUID;

public class getQuickShop {

    Plugin quickshopPlugin = Bukkit.getPluginManager().getPlugin("QuickShop");

    public QuickShopAPI getQuickShopAPI() {
        if(quickshopPlugin != null && quickshopPlugin.isEnabled()){
            QuickShopAPI quickshopApi = (QuickShopAPI)quickshopPlugin;
            return quickshopApi;
        }
        return null;
    }

    public void returnShopData(UUID uuid, int index) {
        Player player = Bukkit.getPlayer(uuid);
        String owner = player.getDisplayName();
        String item = new getQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(0).getItem().getType().name();
        String stock = String.valueOf(new getQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(0).getRemainingStock());
        String price = String.valueOf(new getQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(0).getPrice());
        String type = new getQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(uuid).get(0).getShopType().toString();

        player.sendMessage(AddColor.addColor("&3&lShop Owner&8: &a" + owner));
        player.sendMessage(AddColor.addColor("&3&lShop Item&8: &a" + item));
        player.sendMessage(AddColor.addColor("&3&lShop Stock&8: &a" + stock));
        player.sendMessage(AddColor.addColor("&3&lShop Price&8: &a" + price));
        player.sendMessage(AddColor.addColor("&3&lShop Type&8: &a" + type));
    }
}
