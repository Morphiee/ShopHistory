package gg.morphie.shophistory.commands;

import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.util.GetQuickShop;
import org.maxgamer.quickshop.api.command.CommandContainer;

public class CommandManager {
    private CommandContainer rootContainer;
    private ShopHistory plugin;

    public CommandManager(ShopHistory plugin) {
        this.plugin = plugin;
    }

    public void registerCommand() {

        this.rootContainer = CommandContainer.builder()
                .prefix("menu")
                .permission(null)
                .executor(new SubCommand_Menu(plugin))
                .description("Opens a menu displaying information on all of your shops.")
                .build();

        new GetQuickShop().getQuickShopAPI().getCommandManager().registerCmd(this.rootContainer);

        this.rootContainer = CommandContainer.builder()
                .prefix("reloadsh")
                .permission("quickshop.reloadsh")
                .executor(new SubCommand_ReloadSH(plugin))
                .description("Reloads the Shop History Quickshop Remake addon.")
                .build();

        new GetQuickShop().getQuickShopAPI().getCommandManager().registerCmd(this.rootContainer);
    }
}
