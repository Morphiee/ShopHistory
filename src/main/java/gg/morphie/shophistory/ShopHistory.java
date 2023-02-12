package gg.morphie.shophistory;

import gg.morphie.shophistory.commands.CommandManager;
import gg.morphie.shophistory.files.Messages;
import gg.morphie.shophistory.util.AddColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ShopHistory extends JavaPlugin implements Listener {
    public Messages messagescfg;
    public String Version;
    Plugin quickshopPlugin = Bukkit.getPluginManager().getPlugin("QuickShop");

    public void onEnable() {

        Version = this.getDescription().getVersion();
        loadConfigManager();

        getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.Header")));
        getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.TitleVersion").replace("%VERSION%", this.Version)));
        getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.Author")));
        createConfig();
        getServer().getConsoleSender().sendMessage(" ");
        getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.Footer")));

        new CommandManager(this).registerCommand();
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.GenConfig")));
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
            } else {
                getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.Config")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadConfigManager() {
        this.messagescfg = new Messages(this);
        this.messagescfg.setup();
    }

    public String getMessage(String string) {
        String gotString = this.messagescfg.messagesCFG.getString(string);
        if (gotString != null) return gotString;
        return "Null message";
    }
}
