package gg.morphie.shophistory.commands;

import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.menus.HistoryMenu;
import gg.morphie.shophistory.util.GetQuickShop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.maxgamer.quickshop.api.command.CommandHandler;

import java.util.UUID;

public class SubCommand_Menu implements CommandHandler<CommandSender> {

    private final ShopHistory plugin;
    public SubCommand_Menu(ShopHistory plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(
            CommandSender sender, String commandLabel, String[] cmdArg) {
        sendHistory(sender, commandLabel);
    }

    private void sendHistory(CommandSender s, String commandLabel) {
        Player player = (Player)s;
        UUID uuid = player.getUniqueId();
        new HistoryMenu(plugin).openGUI(player);
    }
}
