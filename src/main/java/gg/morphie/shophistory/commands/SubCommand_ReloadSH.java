package gg.morphie.shophistory.commands;

import gg.morphie.shophistory.ShopHistory;
import gg.morphie.shophistory.menus.HistoryMenu;
import gg.morphie.shophistory.util.AddColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.maxgamer.quickshop.api.command.CommandHandler;

import java.util.UUID;

public class SubCommand_ReloadSH implements CommandHandler<CommandSender> {

    private final ShopHistory plugin;

    public SubCommand_ReloadSH(ShopHistory plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(
            CommandSender sender, String commandLabel, String[] cmdArg) {
        reloadPlugin(sender, commandLabel);
    }

    private void reloadPlugin(CommandSender s, String commandLabel) {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("ShopHistory");
        if (this.plugin != null) {
            this.plugin.reloadConfig();
            this.plugin.getServer().getPluginManager().disablePlugin(plugin);
            this.plugin.getServer().getPluginManager().enablePlugin(plugin);
            s.sendMessage(AddColor.addColor(this.plugin.getMessage("Prefix") + this.plugin.getMessage("ReloadMessage")));
        }
    }
}