package gg.morphie.shophistory;

import gg.morphie.shophistory.commands.CommandManager;
import gg.morphie.shophistory.events.PlayerDataEvents;
import gg.morphie.shophistory.events.QuickShopLogger;
import gg.morphie.shophistory.files.Messages;
import gg.morphie.shophistory.util.AddColor;
import gg.morphie.shophistory.util.playerdata.PlayerDataCleaner;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ShopHistory extends JavaPlugin implements Listener {
    public Messages messagescfg;
    private PlayerDataEvents pe;
    private QuickShopLogger ql;
    public String Version;

    public void onEnable() {
        this.pe = new PlayerDataEvents(this);
        this.ql = new QuickShopLogger(this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(this.pe, this);
        getServer().getPluginManager().registerEvents(this.ql, this);

        Version = this.getDescription().getVersion();
        loadConfigManager();

        getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.Header")));
        getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.TitleVersion").replace("%VERSION%", this.Version)));
        getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.Author")));
        createConfig();
        if (this.getConfig().getBoolean("Settings.AutoDeletePlayerFiles.Enabled")) {
            getServer().getConsoleSender().sendMessage(" ");
            getServer().getConsoleSender().sendMessage(AddColor.addColor(this.getMessage("ServerStart.CleanerCheck")));
            new PlayerDataCleaner(this).cleanPlayerData();
        }
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
