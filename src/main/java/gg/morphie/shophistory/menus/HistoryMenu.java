package gg.morphie.shophistory.menus;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.util.AddColor;
import gg.morphie.shophistory.util.GetQuickShop;
import gg.morphie.shophistory.util.ItemStackUtils;
import gg.morphie.shophistory.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

        for (int i = 0; i < list.size(); i++) {
            ItemStack material;
            if (Integer.parseInt(new GetQuickShop().getStock(uuid, i)) > 0) {
                material = new GetQuickShop().getItem(uuid, i);
            } else {
                material = new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.EmptyStockItem.Material"), 1, (Integer) plugin.getConfig().getInt("Menu.EmptyStockItem.CustomModelID"), null, null, false);
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
                        AddColor.addColor("&3&l" + itemname + new AddColor().fixCase(new GetQuickShop().getType(uuid, i))),
                        " ",
                        AddColor.addColor("&7Price&8: &a" + new AddColor().fixCase(new GetQuickShop().getPrice(uuid, i))),
                        AddColor.addColor("&7Remaining Stock&8: &a" + new GetQuickShop().getStock(uuid, i) + new GetQuickShop().getSpace(uuid, i)),
                        " ",
                        AddColor.addColor("&7Right&8-&7Click to teleport to this shop."),
                        AddColor.addColor("&7Left&8-&7Click to view this shops logs")
                ));
        }
        gui.addElement(group);

        // Filler Item
        gui.setFiller(new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.FillerItem.Material"), 1, (Integer) plugin.getConfig().getInt("Menu.FillerItem.CustomModelID"), null, null, false)); // fill the empty slots with this

        // Previous page
        gui.addElement(new GuiPageElement('(', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.PrevPage.Material"), 1, (Integer) plugin.getConfig().getInt("Menu.PrevPage.CustomModelID"), null, null, false), GuiPageElement.PageAction.PREVIOUS, "&8&l<< &aGo to Previous Page &7(%prevpage%)"));

        // Next page
        gui.addElement(new GuiPageElement(')', new ItemStackUtils().createItem(plugin.getConfig().getString("Menu.NextPage.Material"), 1, (Integer) plugin.getConfig().getInt("Menu.NextPage.CustomModelID"), null, null, false), GuiPageElement.PageAction.NEXT, "&7(%nextpage%) &aGo to Next Page &8&l>>"));

        gui.show(p);
    }
}
