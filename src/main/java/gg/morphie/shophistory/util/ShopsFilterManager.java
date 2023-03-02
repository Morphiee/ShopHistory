package gg.morphie.shophistory.util;

import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.util.playerdata.PlayerDataManager;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ShopsFilterManager {

    private ShopHistory plugin;

    public ShopsFilterManager(ShopHistory plugin) {
        this.plugin = plugin;
    }

    String DefaultFilter = "All";
    List<String> ShopFilters = Arrays.asList("Buying", "Selling", "Out Of Stock");

    public String getPlayerTag(UUID uuid) {
        return new PlayerDataManager(plugin).getString(uuid, "CurrentFilter");
    }

    public String getDefaultFilter() {
        return DefaultFilter;
    }

    public List<String> getShopFilters() {
        return ShopFilters;
    }

    public String getPrevTag(UUID uuid) {
        int listSize = ShopFilters.size();
        if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(DefaultFilter)) {
            return ShopFilters.get(listSize - 1);
        } else if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(ShopFilters.get(0))) {
            return DefaultFilter;
        } else {
            int prevIndex = ShopFilters.indexOf(getPlayerTag(uuid));
            return ShopFilters.get(prevIndex-1);
        }
    }

    public String getNextTag(UUID uuid) {
        int listSize = ShopFilters.size();
        if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(DefaultFilter)) {
            List<String> customTags = ShopFilters;
            return customTags.get(0);
        } else if (new PlayerDataManager(plugin).getString(uuid, "CurrentFilter").equalsIgnoreCase(ShopFilters.get(listSize-1))) {
            return DefaultFilter;
        } else {
            int nextIndex = ShopFilters.indexOf(getPlayerTag(uuid));
            return ShopFilters.get(nextIndex+1);
        }
    }
}