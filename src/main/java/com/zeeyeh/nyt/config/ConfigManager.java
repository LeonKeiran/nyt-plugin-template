package com.zeeyeh.nyt.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 配置管理器
 * @author LeonKeiran
 * @date 2025/3/1 13:42
 */
public class ConfigManager {
    private final Plugin plugin;
    private final String suffix;
    public static final String DEFAULT_CONFIG_SUFFIX = "yml";
    private FileConfiguration configurationHandler;
    private final Map<String, Configuration> configs;

    public ConfigManager(Plugin plugin) {
        this(plugin, DEFAULT_CONFIG_SUFFIX);
    }

    public ConfigManager(Plugin plugin, String suffix) {
        this(plugin, suffix, new YamlConfiguration());
    }

    public ConfigManager(Plugin plugin, String suffix, FileConfiguration configurationHandler) {
        this.plugin = plugin;
        this.suffix = suffix;
        this.configurationHandler = configurationHandler;
        configs = new HashMap<>();
    }

    public void initializeDefaultConfig() {
        InputStream stream = getPlugin().getResource("config.yml");
        if (stream != null) {
            getPlugin().saveDefaultConfig();
        } else {
            File configFile = new File(getPlugin().getDataFolder(), "config." + getSuffix());
            try {
                Files.createFile(configFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException("默认配置文件初始化失败");
            }
        }
    }

    public void loadConfig(String name) {
        loadConfig(name, getConfigurationHandler());
    }

    public Configuration getDefaultConfig() {
        if (this.configs.containsKey("config")) {
            return this.configs.get("config");
        }
        return null;
    }

    public Map<String, Configuration> getConfigs() {
        return configs;
    }

    public void setConfigurationHandler(FileConfiguration configurationHandler) {
        this.configurationHandler = configurationHandler;
    }

    public void clear() {
        this.configs.clear();
    }

    public void loadConfig(String name, FileConfiguration configurationHandler) {
        File file = new File(getPlugin().getDataFolder(), name + "." + getSuffix());
        if (!file.exists()) {
            throw new RuntimeException("配置文件 \"" + name + "." + getSuffix() + "\" 不存在");
        }
        try {
            getConfigurationHandler().load(file);
            this.configs.put(name, getConfigurationHandler());
        } catch (Exception e) {
            throw new RuntimeException("配置文件加载失败");
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getSuffix() {
        return suffix;
    }

    public FileConfiguration getConfigurationHandler() {
        return configurationHandler;
    }
}
