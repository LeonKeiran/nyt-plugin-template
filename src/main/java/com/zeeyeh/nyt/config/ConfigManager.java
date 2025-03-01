package com.zeeyeh.nyt.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置管理器类，用于管理插件的配置文件
 * 提供配置文件的加载、保存和访问功能
 */
public class ConfigManager {
    // 插件实例
    private final Plugin plugin;
    // 配置文件后缀
    private final String suffix;
    // 默认配置文件后缀
    public static final String DEFAULT_CONFIG_SUFFIX = "yml";
    // 配置文件处理器
    private FileConfiguration configurationHandler;
    // 存储所有加载的配置文件
    private final Map<String, Configuration> configs;

    /**
     * 构造函数，初始化配置管理器
     *
     * @param plugin 插件实例
     */
    public ConfigManager(Plugin plugin) {
        this(plugin, DEFAULT_CONFIG_SUFFIX);
    }

    /**
     * 构造函数，初始化配置管理器，并指定配置文件后缀
     *
     * @param plugin 插件实例
     * @param suffix 配置文件后缀
     */
    public ConfigManager(Plugin plugin, String suffix) {
        this(plugin, suffix, new YamlConfiguration());
    }

    /**
     * 构造函数，初始化配置管理器，并指定配置文件后缀和配置文件处理器
     *
     * @param plugin            插件实例
     * @param suffix            配置文件后缀
     * @param configurationHandler 配置文件处理器
     */
    public ConfigManager(Plugin plugin, String suffix, FileConfiguration configurationHandler) {
        this.plugin = plugin;
        this.suffix = suffix;
        this.configurationHandler = configurationHandler;
        configs = new HashMap<>();
    }

    /**
     * 初始化默认配置文件
     * 如果插件资源中存在config.yml，则保存为默认配置
     * 否则创建一个新的配置文件
     */
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
        loadConfig("config");
    }

    /**
     * 加载指定名称的配置文件
     *
     * @param name 配置文件名称，不包含后缀
     */
    public void loadConfig(String name) {
        loadConfig(name, getConfigurationHandler());
    }

    /**
     * 获取默认配置文件
     *
     * @return 默认配置文件，如果未加载则返回null
     */
    public Configuration getDefaultConfig() {
        if (this.configs.containsKey("config")) {
            return this.configs.get("config");
        }
        return null;
    }

    /**
     * 获取所有加载的配置文件
     *
     * @return 包含所有配置文件的映射
     */
    public Map<String, Configuration> getConfigs() {
        return configs;
    }

    /**
     * 设置配置文件处理器
     *
     * @param configurationHandler 配置文件处理器
     */
    public void setConfigurationHandler(FileConfiguration configurationHandler) {
        this.configurationHandler = configurationHandler;
    }

    /**
     * 清空所有已加载的配置文件
     */
    public void clear() {
        this.configs.clear();
    }

    /**
     * 使用指定的配置文件处理器加载配置文件
     *
     * @param name 配置文件名称，不包含后缀
     * @param configurationHandler 配置文件处理器
     */
    public void loadConfig(String name, FileConfiguration configurationHandler) {
        File file = new File(getPlugin().getDataFolder(), name + "." + getSuffix());
        if (!file.exists()) {
            throw new RuntimeException("配置文件 \"" + name + "." + getSuffix() + "\" 不存在");
        }
        try {
            configurationHandler.load(file);
            this.configs.put(name, configurationHandler);
        } catch (Exception e) {
            throw new RuntimeException("配置文件加载失败");
        }
    }

    /**
     * 获取插件实例
     *
     * @return 插件实例
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * 获取配置文件后缀
     *
     * @return 配置文件后缀
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 获取配置文件处理器
     *
     * @return 配置文件处理器
     */
    public FileConfiguration getConfigurationHandler() {
        return configurationHandler;
    }
}
