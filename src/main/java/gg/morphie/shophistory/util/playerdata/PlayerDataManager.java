package gg.morphie.shophistory.util.playerdata;

import gg.morphie.shophistory.ShopHistory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PlayerDataManager {
    private ShopHistory plugin;

    public PlayerDataManager(ShopHistory plugin) {
        this.plugin = plugin;
    }

    public void setString(UUID uuid, String string, String string2) {
        File file = getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        fc.set(string, String.valueOf(string2));
        try {
            fc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getStringList(UUID uuid, String string) {
        File file = getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        return fc.getStringList(string);
    }

    public void setStringList(UUID uuid, String string, List<String> list) {
        File file = getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        fc.set(string, list);
        try {
            fc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(UUID uuid, String string) {
        File file = getPlayerFile(uuid);
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        return fc.getString(string);
    }

    public File getPlayerFile(UUID uuid) {
        File playerFile = new File(this.plugin.getDataFolder() + File.separator + "PlayerData", uuid + ".yml");
        FileConfiguration playerCFG = YamlConfiguration.loadConfiguration(playerFile);
        if (!playerFile.exists()) {
            try {
                playerCFG.save(playerFile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return playerFile;
    }
}
