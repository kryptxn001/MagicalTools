package me.spirek.magicalTools;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private static File configFile;
    private static FileConfiguration config;

    public static void initialize(MagicalTools plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        UpdateManager.update_id = config.getInt("update_id",0);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("§4[MagicalTools] Error while saving config!");
        }
    }
}