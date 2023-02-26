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
                    String nameToChange = new GetQuickShop().getItem(uuid, i).getType().name()
                            .replace("_", " ").toLowerCase();

                    itemname = new AddColor().fixCase(nameToChange);
                }

                int finalI = i;

                group.addElement(new StaticGuiElement( '!',
                        new ItemStack(material),
                        1, // Display a number as the item count
                        click -> {
                            if (click.getType().isLeftClick()) {
                                return true; // returning true will cancel the click event and stop taking the item
                            } else if (click.getType().isRightClick()) {
                                gui.close();
                                Location loc = new GetQuickShop().getLocation(uuid, finalI);
                                p.teleport(LocationUtils.findSafeLocationAroundShop(loc));
                                p.sendMessage(AddColor.addColor(plugin.getMessage("Prefix") + plugin.getMessage("TeleportMessage")));
                                return true; // returning true will cancel the click event and stop taking the item
                            }
                            return true; // returning false will not cancel the initial click event to the gui
                        },
                        AddColor.addColor("&3&l" + itemname + " &8| &a" + new AddColor().fixCase(new GetQuickShop().getType(uuid, i))),
                        " ",
                        AddColor.addColor("&7Price&8: &a" + new AddColor().fixCase(new GetQuickShop().getPrice(uuid, i))),
                        AddColor.addColor("&7Remaining Stock&8: &a" + new GetQuickShop().getStock(uuid, i) + "&7/&c" + new GetQuickShop().getSpace(uuid, i)),
                        " ",
                        AddColor.addColor("&7Right&8-&7Click to teleport to this shop."),
                        AddColor.addColor("&7Left&8-&7Click to view this shops logs")
                ));
            } else if (playerCurrentFilter.equals("Buying")) {
                if (new GetQuickShop().getQuickShopAPI().getShopManager().getPlayerAllShops(p.getUniqueId()).get(i).getShopType().toString().equalsIgnoreCase("buying")) {
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
                        String nameToChange = new GetQuickShop().getItem(uuid, i).getType().name()
                                .replace("_", " ").toLowerCase();

                        itemname = new AddColor().fixCase(nameToChange);
                    }

                    int finalI = i;

                    group.addElement(new StaticGuiElement( '!',
                            new ItemStack(material),
                            1, // Display a number as the item count
                            click -> {
                                if (click.getType().isLeftClick()) {
                                    return true; // returning true will cancel the click event and stop taking the item
                                } else if (click.getType().isRightClick()) {
                                    gui.close();
                                    Location loc = new GetQuickShop().getLocation(uuid, finalI);
                                    p.teleport(LocationUtils.findSafeLocationAroundShop(loc));
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("Prefix") + plugin.getMessage("TeleportMessage")));
                                    return true; // returning true will cancel the click event and stop taking the item
                                }
                                return true; // returning false will not cancel the initial click event to the gui
                            },
                            AddColor.addColor("&3&l" + itemname + " &8| &a" + new AddColor().fixCase(new GetQuickShop().getType(uuid, i))),
                            " ",
                            AddColor.addColor("&7Price&8: &a" + new AddColor().fixCase(new GetQuickShop().getPrice(uuid, i))),
                            AddColor.addColor("&7Remaining Stock&8: &a" + new GetQuickShop().getStock(uuid, i) + "&7/&c" + new GetQuickShop().getSpace(uuid, i)),
                            " ",
                            AddColor.addColor("&7Right&8-&7Click to teleport to this shop."),
                            AddColor.addColor("&7Left&8-&7Click to view this shops logs")
                    ));
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
                        String nameToChange = new GetQuickShop().getItem(uuid, i).getType().name()
                                .replace("_", " ").toLowerCase();

                        itemname = new AddColor().fixCase(nameToChange);
                    }

                    int finalI = i;

                    group.addElement(new StaticGuiElement( '!',
                            new ItemStack(material),
                            1, // Display a number as the item count
                            click -> {
                                if (click.getType().isLeftClick()) {
                                    return true; // returning true will cancel the click event and stop taking the item
                                } else if (click.getType().isRightClick()) {
                                    gui.close();
                                    Location loc = new GetQuickShop().getLocation(uuid, finalI);
                                    p.teleport(LocationUtils.findSafeLocationAroundShop(loc));
                                    p.sendMessage(AddColor.addColor(plugin.getMessage("Prefix") + plugin.getMessage("TeleportMessage")));
                                    return true; // returning true will cancel the click event and stop taking the item
                                }
                                return true; // returning false will not cancel the initial click event to the gui
                            },
                            AddColor.addColor("&3&l" + itemname + " &8| &a" + new AddColor().fixCase(new GetQuickShop().getType(uuid, i))),
                            " ",
                            AddColor.addColor("&7Price&8: &a" + new AddColor().fixCase(new GetQuickShop().getPrice(uuid, i))),
                            AddColor.addColor("&7Remaining Stock&8: &a" + new GetQuickShop().getStock(uuid, i) + "&7/&c" + new GetQuickShop().getSpace(uuid, i)),
                            " ",
                            AddColor.addColor("&7Right&8-&7Click to teleport to this shop."),
                            AddColor.addColor("&7Left&8-&7Click to view this shops logs")
                    ));
                }
            }
        }
        gui.addElement(group);

        gui.addElement(new DynamicGuiElement('1', (viewer) -> new StaticGuiElement('d', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.FilterItem.Material"), 1, plugin.getConfig().getInt("Menu.FilterItem.CustomModelID"), null, null, false),
                click -> {
                    String playerCurrentFilter = new PlayerDataManager(plugin).getString(uuid, "CurrentFilter");
                    if (playerCurrentFilter.equalsIgnoreCase(new ShopsFilterManager(plugin).getDefaultFilter())) {
                        new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new ShopsFilterManager(plugin).getShopFilters().get(0));
                        gui.draw();
                        return true;
                    } else if (new ShopsFilterManager(plugin).getShopFilters().indexOf(playerCurrentFilter) == new ShopsFilterManager(plugin).getShopFilters().size()-1) {
                        new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new ShopsFilterManager(plugin).getDefaultFilter());
                        gui.draw();
                        return true;
                    } else {
                        int playerFilterIndex = new ShopsFilterManager(plugin).getShopFilters().indexOf(playerCurrentFilter);
                        new PlayerDataManager(plugin).setString(uuid, "CurrentFilter", new ShopsFilterManager(plugin).getShopFilters().get(playerFilterIndex+1));
                        gui.draw();
                        return true;
                    }
                },
                AddColor.addColor("&3&lMenu Filter &8(&a" + new ShopsFilterManager(plugin).getPlayerTag(p.getUniqueId()) + "&8)"),
                " ",
                AddColor.addColor("&7Current Filter Tag&8:"),
                AddColor.addColor("&7" + new ShopsFilterManager(plugin).getPrevTag(p.getUniqueId()) + " &8-&a " + new ShopsFilterManager(plugin).getPlayerTag(p.getUniqueId()) + " &8-&7 " +  new ShopsFilterManager(plugin).getNextTag(p.getUniqueId())
                ))));

        // Filler Item
        gui.setFiller(new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.FillerItem.Material"), 1, plugin.getConfig().getInt("Menu.FillerItem.CustomModelID"), null, null, false)); // fill the empty slots with this

        // Previous page
        gui.addElement(new GuiPageElement('(', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.PrevPage.Material"), 1, plugin.getConfig().getInt("Menu.PrevPage.CustomModelID"), null, null, false), GuiPageElement.PageAction.PREVIOUS, "&8&l<< &aGo to Previous Page &7(%prevpage%)"));

        // Next page
        gui.addElement(new GuiPageElement(')', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.NextPage.Material"), 1, plugin.getConfig().getInt("Menu.NextPage.CustomModelID"), null, null, false), GuiPageElement.PageAction.NEXT, "&7(%nextpage%) &aGo to Next Page &8&l>>"));

        gui.show(p);
    }
}
