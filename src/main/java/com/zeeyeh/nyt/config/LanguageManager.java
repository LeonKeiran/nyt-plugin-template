package com.zeeyeh.nyt.config;

import com.google.common.base.Preconditions;
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
 * 语言管理器类，用于处理和加载不同语言的配置文件
 */
public class LanguageManager {
    private final Plugin plugin;
    private final String suffix;
    private final String currentLanguage;
    private FileConfiguration configuration;
    public static final String DEFAULT_LANGUAGE_SUFFIX = "yml";
    public static final String DEFAULT_LANGUAGE_NAME = "zh_CN";
    private final Map<String, Configuration> langConfigs;

    /**
     * 构造函数，使用默认的语言后缀和名称初始化语言管理器
     *
     * @param plugin 插件实例
     */
    public LanguageManager(Plugin plugin) {
        this(DEFAULT_LANGUAGE_SUFFIX, plugin);
    }

    /**
     * 构造函数，指定语言后缀初始化语言管理器
     *
     * @param suffix 语言文件的后缀
     * @param plugin 插件实例
     */
    public LanguageManager(String suffix, Plugin plugin) {
        this(DEFAULT_LANGUAGE_NAME, suffix, plugin);
    }

    /**
     * 构造函数，指定当前语言和后缀初始化语言管理器
     *
     * @param currentLanguage 当前语言名称
     * @param suffix          语言文件的后缀
     * @param plugin          插件实例
     */
    public LanguageManager(String currentLanguage, String suffix, Plugin plugin) {
        this(new YamlConfiguration(), currentLanguage, suffix, plugin);
    }

    /**
     * 构造函数，使用已有的配置文件初始化语言管理器
     *
     * @param configuration   配置文件
     * @param currentLanguage 当前语言名称
     * @param suffix          语言文件的后缀
     * @param plugin          插件实例
     */
    public LanguageManager(FileConfiguration configuration, String currentLanguage, String suffix, Plugin plugin) {
        this(configuration, plugin, suffix, currentLanguage, new HashMap<>());
    }

    /**
     * 构造函数，用于内部使用，初始化语言管理器的所有属性
     *
     * @param configuration   配置文件
     * @param plugin          插件实例
     * @param suffix          语言文件的后缀
     * @param currentLanguage 当前语言名称
     * @param langConfigs     语言配置的映射
     */
    public LanguageManager(FileConfiguration configuration, Plugin plugin, String suffix, String currentLanguage, Map<String, Configuration> langConfigs) {
        this.configuration = configuration;
        this.plugin = plugin;
        this.suffix = suffix;
        this.currentLanguage = currentLanguage;
        this.langConfigs = langConfigs;
    }

    /**
     * 初始化默认语言文件，如果不存在则从插件资源中复制
     */
    public void initializeDefaultLanguage() {
        String defaultLanguage = DEFAULT_LANGUAGE_NAME + "." + DEFAULT_LANGUAGE_SUFFIX;
        InputStream stream = getPlugin().getResource("locales/" + defaultLanguage);
        File file = new File(getPlugin().getDataFolder(), "locales/" + defaultLanguage);
        if (file.exists()) {
            return;
        }
        if (stream != null) {
            try {
                Files.copy(stream, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException("语言文件加载失败");
            }
        }
    }

    /**
     * 加载当前语言的配置文件
     */
    public void loadLanguage() {
        loadLanguage(getCurrentLanguage(), getConfiguration());
    }

    /**
     * 加载指定语言的配置文件
     *
     * @param language 语言名称
     */
    public void loadLanguage(String language) {
        loadLanguage(language, getConfiguration());
    }

    /**
     * 使用给定的配置文件处理器加载指定语言的配置文件
     *
     * @param language        语言名称
     * @param configurationHandler 配置文件处理器
     */
    public void loadLanguage(String language, FileConfiguration configurationHandler) {
        File file = new File(getPlugin().getDataFolder(), "locales/" + language + "." + getSuffix());
        Preconditions.checkArgument(!file.exists(), new RuntimeException("语言文件 \"" + language + "." + getSuffix() + "\" 不存在"));
        try {
            getConfiguration().load(file);
            this.langConfigs.put(language, getConfiguration());
        } catch (Exception e) {
            throw new RuntimeException("语言文件加载失败");
        }
    }

    /**
     * 根据给定的路径在当前语言配置中获取字符串
     *
     * @param path 配置路径
     * @return 对应路径的字符串
     */
    public String translate(String path) {
        Preconditions.checkArgument(!this.langConfigs.containsKey(getCurrentLanguage()), new RuntimeException("语言文件加载失败"));
        Configuration currentConfiguration = this.langConfigs.get(getCurrentLanguage());
        return currentConfiguration.getString(path);
    }

    /**
     * 解析语言内容中的参数
     *
     * @param content 语言内容
     * @param params   参数数组
     * @return 替换参数后的字符串
     */
    public String parseLanguageParams(String content, String... params) {
        Preconditions.checkNotNull(content, new RuntimeException("语言内容不能为空"));
        if (params == null || params.length == 0) {
            throw new IllegalArgumentException("值不能为空");
        }
        StringBuilder builder = new StringBuilder(content);
        for (int i = 0; i < params.length; i++) {
            builder.replace(
                    builder.indexOf("{" + i + "}"),
                    builder.indexOf("{" + i + "}") + ("{" + i + " }").length(),
                    params[i]);
        }
        return builder.toString();
    }

    /**
     * 获取当前语言名称
     *
     * @return 当前语言名称
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * 获取配置文件
     *
     * @return 配置文件
     */
    public FileConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * 获取语言文件的后缀
     *
     * @return 语言文件的后缀
     */
    public String getSuffix() {
        return suffix;
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
     * 设置配置文件
     *
     * @param configuration 配置文件
     */
    public void setConfiguration(FileConfiguration configuration) {
        this.configuration = configuration;
    }
}
