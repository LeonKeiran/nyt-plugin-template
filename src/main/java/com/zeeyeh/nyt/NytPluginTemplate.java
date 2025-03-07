package com.zeeyeh.nyt;

import com.zeeyeh.nyt.api.Translator;
import com.zeeyeh.nyt.config.LanguageManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class NytPluginTemplate extends JavaPlugin {
    @Getter
    private static NytPluginTemplate instance;
    @Getter
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        instance = this;
        languageManager = new LanguageManager(this);
        getLanguageManager().initializeDefaultLanguage();
        Translator.init(languageManager);
    }

    @Override
    public void onDisable() {
    }
}