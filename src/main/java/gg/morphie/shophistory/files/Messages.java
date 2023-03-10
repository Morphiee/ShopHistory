package gg.morphie.shophistory.files;

import gg.morphie.shophistory.ShopHistory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;

public class Messages implements Listener {
    private ShopHistory plugin;
    public FileConfiguration messagesCFG;
    public File messagesFile;

    public Messages(ShopHistory plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }
        this.messagesFile = new File(this.plugin.getDataFolder(), "messages.yml");
        if (!this.messagesFile.exists()) {
            try {
                this.messagesFile.createNewFile();

                this.messagesCFG = YamlConfiguration.loadConfiguration(this.messagesFile);

                this.addDefaults(this.messagesCFG);

                this.messagesCFG.options().copyDefaults(true);
                saveMessages();
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not create the messages.yml file");
            }
        }
        this.messagesCFG = YamlConfiguration.loadConfiguration(this.messagesFile);

        this.addDefaults(this.messagesCFG);
    }

    public void saveMessages() {
        try {
            this.messagesCFG.save(this.messagesFile);
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save the messages.yml file");
        }
    }

    public void reloadMessages() {
        this.messagesCFG = YamlConfiguration.loadConfiguration(this.messagesFile);
        this.addDefaults(this.messagesCFG);
    }

    private void addDefaults(FileConfiguration cfg) {
        cfg.addDefault("ServerStart.Header", "&7‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        cfg.addDefault("ServerStart.TitleVersion", "&aShopHistory&7: &2%VERSION%");
        cfg.addDefault("ServerStart.Author", "&aAuthor&7: &2Morphie#1738 &7(Discord)");
        cfg.addDefault("ServerStart.Config", "&aConfig&7: &2Loaded");
        cfg.addDefault("ServerStart.GenConfig", "&aConfig&7: &2Generating");
        cfg.addDefault("ServerStart.CleanerCheck", "&aPlayerData Cleaner&8: &2Checking for old files.");
        cfg.addDefault("ServerStart.CleanerNoneCleared", "&aPlayerData Cleaner&8: &2No files cleared.");
        cfg.addDefault("ServerStart.CleanerCleared", "&aPlayerData Cleaner&8: &2Successfully cleared %FILES_DELETED% files.");
        cfg.addDefault("ServerStart.Footer", "&7‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        cfg.addDefault("Menu.Title", ":offset_-8::shop:");
        cfg.addDefault("ReloadMessage", "&aPlugin has been successfully reloaded!");
        cfg.addDefault("Prefix", "&8&l[&3ShopHistory&8&l] ");
        cfg.addDefault("ErrorPrefix", "&8&l[&3&l!&8&l] ");
        cfg.addDefault("TeleportMessage", "&aYou have been successfully teleported to your shop.");
        cfg.addDefault("ShopLogger.Title", "&3&lShop Logger &7(&a%LOG_NUMS%&7)");
        cfg.addDefault("ShopLogger.Log", "&b&l| &aBuyer: &7%BUYER%, &aItem: &7%ITEM%, &aAmount: &7%AMOUNT%, &aMoney Spent: &7$%MONEY_SPENT%");
        cfg.addDefault("ShopLogger.NoShopsLogged", "&7There are no logs saved for this shop.");
    }
}
