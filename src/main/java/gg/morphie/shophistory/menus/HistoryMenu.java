package gg.morphie.shophistory.menus;

import de.themoep.inventorygui.*;
import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.util.*;
import gg.morphie.shophistory.util.playerdata.PlayerDataManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.api.shop.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryMenu implements Listener {

    private ShopHistory plugin;

    public HistoryMenu(ShopHistory plugin) {
        this.plugin = plugin;
    }

    String[] guiSetup = {
            " ggggggg ",
            " ggggggg ",
            " ggggggg ",
            " ggggggg ",
            " ggggggg ",
            "  ( 1 )  "
    };

    public void openGUI(Player p) {
        InventoryGui gui = new InventoryGui(plugin, p, AddColor.addColor(plugin.getMessage("Menu.Title")), guiSetup);

        List<Shop> list = new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(p.getUniqueId());
        UUID uuid = p.getUniqueId();

        GuiElementGroup group = new GuiElementGroup('g');

        ItemStack material;

        // Filler Item
        gui.setFiller(new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.FillerItem.Material"), 1, plugin.getConfig().getInt("Menu.FillerItem.CustomModelID"), null, null, false)); // fill the empty slots with this

        // Previous page
        gui.addElement(new GuiPageElement('(', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.PrevPage.Material"), 1, plugin.getConfig().getInt("Menu.PrevPage.CustomModelID"), null, null, false), GuiPageElement.PageAction.PREVIOUS, "&8&l<< &aGo to Previous Page"));

        // Next page
        gui.addElement(new GuiPageElement(')', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.NextPage.Material"), 1, plugin.getConfig().getInt("Menu.NextPage.CustomModelID"), null, null, false), GuiPageElement.PageAction.NEXT, "&aGo to Next Page &8&l>>"));


        ArrayList<String> filterLore = new ArrayList();
        for (String s : plugin.getMessageList("Menu.FilterItem.Lore")) {
            filterLore.add(AddColor.addColor(s
                    .replace("%ALL%", new ShopsFilterManager(plugin).getTag("All", p.getUniqueId()))
                    .replace("%BUYING%", new ShopsFilterManager(plugin).getTag("Buying", p.getUniqueId()))
                    .replace("%SELLING%", new ShopsFilterManager(plugin).getTag("Selling", p.getUniqueId()))
                    .replace("%OUTOFSTOCK%", new ShopsFilterManager(plugin).getTag("Out Of Stock", p.getUniqueId()))));
        }
        gui.addElement(new DynamicGuiElement('1', (viewer) -> new StaticGuiElement('d', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.FilterItem.Material"), 1, plugin.getConfig().getInt("Menu.FilterItem.CustomModelID"), null, null, false),
            click -> {
                String playerCurrentFilter = new PlayerDataManager(plugin).getString(uuid, "CurrentFilter");
                if (playerCurrentFilter.equalsIgnoreCase(new ShopsFilterManager(plugin).getDefaultFilter())) {
                    new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new ShopsFilterManager(plugin).getShopFilters().get(0));
                    this.openGUI(p);
                    InventoryGui.clearHistory(p);
                    return true;
                } else if (new ShopsFilterManager(plugin).getShopFilters().indexOf(playerCurrentFilter) == new ShopsFilterManager(plugin).getShopFilters().size()-1) {
                    new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new ShopsFilterManager(plugin).getDefaultFilter());
                    this.openGUI(p);
                    InventoryGui.clearHistory(p);
                    return true;
                } else {
                    int playerFilterIndex = new ShopsFilterManager(plugin).getShopFilters().indexOf(playerCurrentFilter);
                    new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new ShopsFilterManager(plugin).getShopFilters().get(playerFilterIndex+1));
                    this.openGUI(p);
                    InventoryGui.clearHistory(p);
                    return true;
                }
            },
            AddColor.addColor(plugin.getMessage("Menu.FilterItem.Title").replace("%CURRENT_FILTER%", new ShopsFilterManager(plugin).getPlayerTag(p.getUniqueId()))),
                filterLore.get(0),
                filterLore.get(1),
                filterLore.get(2),
                filterLore.get(3),
                filterLore.get(4),
                filterLore.get(5),
                filterLore.get(6),
                filterLore.get(7)
            )));

        for (int i = 0; i < list.size(); i++) {
            String playerCurrentFilter = new PlayerDataManager(plugin).getString(uuid, "CurrentFilter");
            if (playerCurrentFilter.equalsIgnoreCase(new ShopsFilterManager(plugin).getDefaultFilter())) {
                if (Integer.parseInt(new GetQuickShop().getStock(uuid, i)) > 0) {
                    if (plugin.getConfig().getBoolean("Settings.UseShopItem")) {
                        material = new GetQuickShop().getItem(uuid, i);
                    } else {
                        material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.ShopItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                    }
                } else {
                    material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.EmptyStockItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                }

                String itemname;

                if (new GetQuickShop().getItem(uuid, i).getItemMeta().hasDisplayName()) {
                    itemname = new GetQuickShop().getItem(uuid, i).getItemMeta().getDisplayName();
                } else {
                    String nameToChange = new GetQuickShop().getItem(uuid, i).getType().name();

                    itemname = new AddColor().fixCase(nameToChange);
                }

                ItemStack finalMaterial = material;
                int modelData;
                if (finalMaterial.getItemMeta().hasCustomModelData()) {
                    modelData = finalMaterial.getItemMeta().getCustomModelData();
                } else {
                    modelData = 1;
                }
                int finalI3 = i;
                ArrayList<String> lore = new ArrayList();
                for (String s : plugin.getMessageList("Menu.ShopItem.Lore")) {
                    lore.add(AddColor.addColor(s
                            .replace("%PRICE%", new AddColor().fixCase(new GetQuickShop().getPrice(uuid, finalI3)))
                            .replace("%STOCK%", new GetQuickShop().getStock(uuid, finalI3)))
                            .replace("%SHOP_SPACE%", new GetQuickShop().getSpace(uuid, finalI3)));
                }
                group.addElement(new DynamicGuiElement('g', (viewer) -> new StaticGuiElement('g', new ItemStackUtils().createItem(finalMaterial.getType().name(), 1, modelData, null, lore, false),
                    click -> {
                        if (click.getType().isLeftClick()) {
                            List<String> shopLogs = new PlayerDataManager(plugin).getStringList(uuid, "ShopLogger." + new GetQuickShop().getShop(uuid, finalI3));
                            if (shopLogs.size() > 0) {
                                p.sendMessage(AddColor.addColor(plugin.getMessage("ShopLogger.Title").replace("%LOG_NUMS%", String.valueOf(shopLogs.size()))));
                                p.sendMessage(" ");
                                for (String shopLog : shopLogs) {
                                    p.sendMessage(AddColor.addColor(shopLog));
                                }
                                gui.close();
                                return true;
                            } else {
                                gui.close();
                                p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopLogger.NoShopsLogged")));
                                return true; // returning true will cancel the click event and stop taking the item
                            }
                        } else if (click.getType().isRightClick()) {
                            gui.close();
                            Location loc = new GetQuickShop().getLocation(uuid, finalI3);
                            try {
                                p.teleport(LocationUtils.findSafeLocationAroundShop(loc));
                                p.sendMessage(AddColor.addColor(plugin.getMessage("Prefix") + plugin.getMessage("TeleportMessage")));
                                return true;
                            } catch (IllegalArgumentException args) {
                                p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopNotSafeMessage")));
                                return true;
                            }
                        }
                        return true; // returning false will not cancel the initial click event to the gui
                    },
                        AddColor.addColor(plugin.getMessage("Menu.ShopItem.Title").replace("%ITEM_TITLE%", itemname).replace("%SHOP_TYPE%", new AddColor().fixCase(new GetQuickShop().getType(uuid, finalI3)))),
                        lore.get(0),
                        lore.get(1),
                        lore.get(2),
                        lore.get(3),
                        lore.get(4),
                        lore.get(5),
                        lore.get(6)
                )));
            } else if (playerCurrentFilter.equals("Buying")) {
                if (new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(p.getUniqueId()).get(i).getShopType().toString().equalsIgnoreCase("buying")) {
                    if (Integer.parseInt(new GetQuickShop().getStock(uuid, i)) > 0) {
                        if (plugin.getConfig().getBoolean("Settings.UseShopItem")) {
                            material = new ItemStackUtils().createItem(new GetQuickShop().getItem(uuid, i).getType().name(), 1, 1, null, null, false);
                        } else {
                            material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.ShopItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                        }
                    } else {
                        material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.EmptyStockItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                    }
                    String itemname;
                    if (new GetQuickShop().getItem(uuid, i).getItemMeta().hasDisplayName()) {
                        itemname = new GetQuickShop().getItem(uuid, i).getItemMeta().getDisplayName();
                    } else {
                        String nameToChange = new GetQuickShop().getItem(uuid, i).getType().name();

                        itemname = new AddColor().fixCase(nameToChange);
                    }

                    ItemStack finalMaterial = material;
                    int modelData;
                    if (finalMaterial.getItemMeta().hasCustomModelData()) {
                        modelData = finalMaterial.getItemMeta().getCustomModelData();
                    } else {
                        modelData = 1;
                    }
                    int finalI1 = i;
                    ArrayList<String> lore = new ArrayList();
                    for (String s : plugin.getMessageList("Menu.ShopItem.Lore")) {
                        lore.add(AddColor.addColor(s
                                        .replace("%PRICE%", new AddColor().fixCase(new GetQuickShop().getPrice(uuid, finalI1)))
                                        .replace("%STOCK%", new GetQuickShop().getStock(uuid, finalI1)))
                                        .replace("%SHOP_SPACE%", new GetQuickShop().getSpace(uuid, finalI1)));
                    }
                    group.addElement(new DynamicGuiElement('g', (viewer) -> new StaticGuiElement('g', new ItemStackUtils().createItem(finalMaterial.getType().name(), 1, modelData, null, lore, false),
                        click -> {
                            if (click.getType().isLeftClick()) {
                                List<String> shopLogs = new PlayerDataManager(plugin).getStringList(uuid, "ShopLogger." + new GetQuickShop().getShop(uuid, finalI1));
                                if (shopLogs.size() > 0) {
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("ShopLogger.Title").replace("%LOG_NUMS%", String.valueOf(shopLogs.size()))));
                                    p.sendMessage(" ");
                                    for (String shopLog : shopLogs) {
                                        p.sendMessage(AddColor.addColor(shopLog));
                                    }
                                    gui.close();
                                    return true;
                                } else {
                                    gui.close();
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopLogger.NoShopsLogged")));
                                    return true; // returning true will cancel the click event and stop taking the item
                                }
                            } else if (click.getType().isRightClick()) {
                                gui.close();
                                Location loc = new GetQuickShop().getLocation(uuid, finalI1);
                                try {
                                    p.teleport(LocationUtils.findSafeLocationAroundShop(loc));
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("Prefix") + plugin.getMessage("TeleportMessage")));
                                    return true;
                                } catch (IllegalArgumentException args) {
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopNotSafeMessage")));
                                    return true;
                                }
                            }
                            return true; // returning false will not cancel the initial click event to the gui
                        },
                            AddColor.addColor(plugin.getMessage("Menu.ShopItem.Title").replace("%ITEM_TITLE%", itemname).replace("%SHOP_TYPE%", new AddColor().fixCase(new GetQuickShop().getType(uuid, finalI1)))),
                            lore.get(0),
                            lore.get(1),
                            lore.get(2),
                            lore.get(3),
                            lore.get(4),
                            lore.get(5),
                            lore.get(6)
                    )));
                }
            } else if (playerCurrentFilter.equals("Selling")) {
                if (new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(p.getUniqueId()).get(i).getShopType().toString().equalsIgnoreCase("selling")) {
                    if (Integer.parseInt(new GetQuickShop().getStock(uuid, i)) > 0) {
                        if (plugin.getConfig().getBoolean("Settings.UseShopItem")) {
                            material = new GetQuickShop().getItem(uuid, i);
                        } else {
                            material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.ShopItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                        }
                    } else {
                        material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.EmptyStockItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                    }
                    String itemname;
                    if (new GetQuickShop().getItem(uuid, i).getItemMeta().hasDisplayName()) {
                        itemname = new GetQuickShop().getItem(uuid, i).getItemMeta().getDisplayName();
                    } else {
                        String nameToChange = new GetQuickShop().getItem(uuid, i).getType().name();

                        itemname = new AddColor().fixCase(nameToChange);
                    }

                    ItemStack finalMaterial = material;
                    int modelData;
                    if (finalMaterial.getItemMeta().hasCustomModelData()) {
                        modelData = finalMaterial.getItemMeta().getCustomModelData();
                    } else {
                        modelData = 1;
                    }
                    int finalI2 = i;
                    ArrayList<String> lore = new ArrayList();
                    for (String s : plugin.getMessageList("Menu.ShopItem.Lore")) {
                        lore.add(AddColor.addColor(s
                                        .replace("%PRICE%", new AddColor().fixCase(new GetQuickShop().getPrice(uuid, finalI2)))
                                        .replace("%STOCK%", new GetQuickShop().getStock(uuid, finalI2)))
                                        .replace("%SHOP_SPACE%", new GetQuickShop().getSpace(uuid, finalI2)));
                    }
                    group.addElement(new DynamicGuiElement('g', (viewer) -> new StaticGuiElement('g', new ItemStackUtils().createItem(finalMaterial.getType().name(), 1, modelData, null, lore, false),
                        click -> {
                            if (click.getType().isLeftClick()) {
                                List<String> shopLogs = new PlayerDataManager(plugin).getStringList(uuid, "ShopLogger." + new GetQuickShop().getShop(uuid, finalI2));
                                if (shopLogs.size() > 0) {
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("ShopLogger.Title").replace("%LOG_NUMS%", String.valueOf(shopLogs.size()))));
                                    p.sendMessage(" ");
                                    for (String shopLog : shopLogs) {
                                        p.sendMessage(AddColor.addColor(shopLog));
                                    }
                                    gui.close();
                                    return true;
                                } else {
                                    gui.close();
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopLogger.NoShopsLogged")));
                                    return true; // returning true will cancel the click event and stop taking the item
                                }
                            } else if (click.getType().isRightClick()) {
                                gui.close();
                                Location loc = new GetQuickShop().getLocation(uuid, finalI2);
                                try {
                                    p.teleport(LocationUtils.findSafeLocationAroundShop(loc));
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("Prefix") + plugin.getMessage("TeleportMessage")));
                                    return true;
                                } catch (IllegalArgumentException args) {
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopNotSafeMessage")));
                                    return true;
                                }
                            }
                            return true; // returning false will not cancel the initial click event to the gui
                        },
                            AddColor.addColor(plugin.getMessage("Menu.ShopItem.Title").replace("%ITEM_TITLE%", itemname).replace("%SHOP_TYPE%", new AddColor().fixCase(new GetQuickShop().getType(uuid, finalI2)))),
                            lore.get(0),
                            lore.get(1),
                            lore.get(2),
                            lore.get(3),
                            lore.get(4),
                            lore.get(5),
                            lore.get(6)
                    )));
                }
            } else if (playerCurrentFilter.equals("Out Of Stock")) {
                if (new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(p.getUniqueId()).get(i).getRemainingStock() == 0) {
                    if (Integer.parseInt(new GetQuickShop().getStock(uuid, i)) > 0) {
                        if (plugin.getConfig().getBoolean("Settings.UseShopItem")) {
                            material = new GetQuickShop().getItem(uuid, i);
                        } else {
                            material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.ShopItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                        }
                    } else {
                        material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.EmptyStockItem.Material"), 1, plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
                    }
                    String itemname;
                    if (new GetQuickShop().getItem(uuid, i).getItemMeta().hasDisplayName()) {
                        itemname = new GetQuickShop().getItem(uuid, i).getItemMeta().getDisplayName();
                    } else {
                        String nameToChange = new GetQuickShop().getItem(uuid, i).getType().name();

                        itemname = new AddColor().fixCase(nameToChange);
                    }

                    ItemStack finalMaterial = material;
                    int finalI2 = i;
                    ArrayList<String> lore = new ArrayList();
                    for (String s : plugin.getMessageList("Menu.ShopItem.Lore")) {
                        lore.add(AddColor.addColor(s
                                        .replace("%PRICE%", new AddColor().fixCase(new GetQuickShop().getPrice(uuid, finalI2)))
                                        .replace("%STOCK%", new GetQuickShop().getStock(uuid, finalI2)))
                                        .replace("%SHOP_SPACE%", new GetQuickShop().getSpace(uuid, finalI2)));
                    }
                    group.addElement(new DynamicGuiElement('g', (viewer) -> new StaticGuiElement('g', new ItemStackUtils().createItem(finalMaterial.getType().name(), 1, finalMaterial.getItemMeta().getCustomModelData(), null, lore, false),
                            click -> {
                                if (click.getType().isLeftClick()) {
                                    List<String> shopLogs = new PlayerDataManager(plugin).getStringList(uuid, "ShopLogger." + new GetQuickShop().getShop(uuid, finalI2));
                                    if (shopLogs.size() > 0) {
                                        p.sendMessage(AddColor.addColor(plugin.getMessage("ShopLogger.Title").replace("%LOG_NUMS%", String.valueOf(shopLogs.size()))));
                                        p.sendMessage(" ");
                                        for (String shopLog : shopLogs) {
                                            p.sendMessage(AddColor.addColor(shopLog));
                                        }
                                        gui.close();
                                        return true;
                                    } else {
                                        gui.close();
                                        p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopLogger.NoShopsLogged")));
                                        return true; // returning true will cancel the click event and stop taking the item
                                    }
                                } else if (click.getType().isRightClick()) {
                                    gui.close();
                                    Location loc = new GetQuickShop().getLocation(uuid, finalI2);
                                    try {
                                        p.teleport(LocationUtils.findSafeLocationAroundShop(loc));
                                        p.sendMessage(AddColor.addColor(plugin.getMessage("Prefix") + plugin.getMessage("TeleportMessage")));
                                        return true;
                                    } catch (IllegalArgumentException args) {
                                        p.sendMessage(AddColor.addColor(plugin.getMessage("ErrorPrefix") + plugin.getMessage("ShopNotSafeMessage")));
                                        return true;
                                    }
                                }
                                return true; // returning false will not cancel the initial click event to the gui
                            },
                            AddColor.addColor(plugin.getMessage("Menu.ShopItem.Title").replace("%ITEM_TITLE%", itemname).replace("%SHOP_TYPE%", new AddColor().fixCase(new GetQuickShop().getType(uuid, finalI2)))),
                            lore.get(0),
                            lore.get(1),
                            lore.get(2),
                            lore.get(3),
                            lore.get(4),
                            lore.get(5),
                            lore.get(6)
                    )));
                }
            }
        }
        gui.addElement(group);

        gui.show(p);
    }
}
