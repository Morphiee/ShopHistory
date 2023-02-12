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
        cfg.addDefault("ServerStart.Footer", "&7‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        cfg.addDefault("Commands.Header", "&7‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        cfg.addDefault("Commands.Footer", "&7‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        cfg.addDefault("Commands.Help", "&a/history help &8- &7Shows this text menu.");
        cfg.addDefault("Commands.Menu", "&a/history &8- &7Opens the chest shop history / current stock menu.");
        cfg.addDefault("ErrorPrefix", "&8[&9&l!&8] ");
        cfg.addDefault("IgnoreFormat", "[X]");
        cfg.addDefault("InvalidArgsMessage", "&7Invalid arguments! &b/mr help &7to view all commands.");
        cfg.addDefault("InvalidCredits", "&7You do not have the valid credits for this! Canceling credit assignment.");
        cfg.addDefault("InvalidNumber", "&7The message entered was not recognized as a number! Canceling credit assignment.");
        cfg.addDefault("InvalidNumberNegative", "&7The number you entered was not positive! Canceling credit assignment.");
        cfg.addDefault("InvalidPlayer", "&7Cannot find that player!");
        cfg.addDefault("InvalidSkill", "&7The argument entered was not recognized as a skill!");
        cfg.addDefault("NoPermsMessage", "&7You don't have permission to do this!");
        cfg.addDefault("NoSkillCap", "&bNone");
        cfg.addDefault("OtherPlayerCreditMessage", "&b%PLAYER% &7currently has &b%CREDITS% &7credits.");
        cfg.addDefault("PlayerCreditsMessage", "&7You currently have &b%CREDITS% &7credits.");
        cfg.addDefault("Prefix", "&9&lMorphRedeem &8&l➙ ");
        cfg.addDefault("ReloadMessage", "&7Plugin files successfully reloaded!");
        cfg.addDefault("SkillCapReached", "&7You tried to get to level &b%LEVEL% &7in &b%SKILL%&7, but the skill cap is &b%CAP%&7.");
        cfg.addDefault("SkillDisabledMessage", "&7This skill has been disabled by administration!");
        cfg.addDefault("SpigotLink", "&7https://www.spigotmc.org/resources/morphredeem-mcmmo-credits-1-14.67435/");
    }
}
