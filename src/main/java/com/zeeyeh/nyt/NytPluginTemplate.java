package com.zeeyeh.nyt;

import com.zeeyeh.nyt.api.Translator;
import com.zeeyeh.nyt.config.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NytPluginTemplate extends JavaPlugin {
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        languageManager = new LanguageManager(this);
        getLanguageManager().initializeDefaultLanguage();
        Translator.init(languageManager);
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    @Override
    public void onDisable() {
    }
}
