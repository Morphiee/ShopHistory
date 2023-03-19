package gg.morphie.shophistory.files;

import gg.morphie.shophistory.ShopHistory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        cfg.addDefault("Menu.Title", "&3&lYour Player Shops");
        cfg.addDefault("Menu.ShopItem.Title", "&3&l%ITEM_TITLE% &7(&a%SHOP_TYPE%&7)");
        List<String> list1 = new ArrayList<String>();
        list1.add(" ");
        list1.add("&3&lInformation:");
        list1.add("&b&l| &7Price: &a%PRICE%");
        list1.add("&b&l| &7Remaining Stock: &a%STOCK%&7/&c%SHOP_SPACE%");
        list1.add(" ");
        list1.add("&b➦ Left-Click to view this shops logs");
        list1.add("&b➥ Right-Click to teleport to this shop");
        cfg.addDefault("Menu.ShopItem.Lore", list1);
        cfg.addDefault("Menu.FilterItem.Title", "&3&lMenu Filter &7(&a%CURRENT_FILTER%&7)");
        List<String> list2 = new ArrayList<String>();
        list2.add(" ");
        list2.add("&3&lCurrent Filter Tag:");
        list2.add("&b&l| &7%ALL%");
        list2.add("&b&l| &7%BUYING%");
        list2.add("&b&l| &7%SELLING%");
        list2.add("&b&l| &7%OUTOFSTOCK%");
        list2.add(" ");
        list2.add("&b➥ Click to change the current menu filter");
        cfg.addDefault("Menu.FilterItem.Lore", list2);
        cfg.addDefault("ReloadMessage", "&aPlugin has been successfully reloaded!");
        cfg.addDefault("Prefix", "&8&l[&3ShopHistory&8&l] ");
        cfg.addDefault("ErrorPrefix", "&8&l[&3&l!&8&l] ");
        cfg.addDefault("TeleportMessage", "&aYou have been successfully teleported to your shop.");
        cfg.addDefault("ShopNotSafeMessage", "&7Surrounding shop location is not safe for teleportation!");
        cfg.addDefault("ShopLogger.Title", "&3&lShop Logger &7(&a%LOG_NUMS%&7)");
        cfg.addDefault("ShopLogger.Log", "&b&l| &aBuyer: &7%BUYER%, &aItem: &7%ITEM%, &aAmount: &7%AMOUNT%, &aMoney Spent: &7$%MONEY_SPENT%");
        cfg.addDefault("ShopLogger.NoShopsLogged", "&7There are no logs saved for this shop.");

    }
}
