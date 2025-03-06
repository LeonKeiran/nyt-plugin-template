package com.zeeyeh.nyt.api;

import com.zeeyeh.nyt.config.LanguageManager;

/**
 * @author LeonKeiran
 * @description 翻译器
 * @date 2025/3/1 22:00
 */
public class Translator {
    private static LanguageManager languageManager;
    public static String translate(String content, String... params) {
        return languageManager.translate(content, params);
    }

    public static void init(LanguageManager languageManager) {
        Translator.languageManager = languageManager;
    }
}
