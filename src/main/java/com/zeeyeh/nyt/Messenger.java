package com.zeeyeh.nyt;

import com.zeeyeh.nyt.api.Translator;
import com.zeeyeh.nyt.util.ColorUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LeonKeiran
 * @description 消息发送者
 * @date 2025/3/6 20:16
 */
public class Messenger {

    /**
     * 给所有在线玩家发送通知
     *
     * @param message 消息内容
     */
    public static void broadcast(String message) {
        if (message.isEmpty() || message.equals("none")) {
            return;
        }
        message = ColorUtil.translate(message);
        Bukkit.broadcastMessage(message);
    }

    /**
     * 给所有在线玩家发送通知
     *
     * @param message    消息内容
     * @param permission 玩家必须拥有的权限
     */
    public static void broadcast(String message, String permission) {
        if (message.isEmpty() || message.equals("none")) {
            return;
        }
        message = ColorUtil.translate(message);
        Bukkit.broadcast(message, permission);
    }

    /**
     * 发送插件消息
     */
    public static void sendPluginMessage(Player player, Plugin plugin, String channel, byte[] message) {
        if (message.length == 0) {
            return;
        }
        player.sendPluginMessage(plugin, channel, message);
    }

    /**
     * 发送快捷栏标题
     *
     * @param player 目标玩家
     * @param title  标题内容
     */
    public static void sendActionBar(Player player, String title) {
        title = ColorUtil.translate(title);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(title));
    }

    /**
     * 发送boss血条
     *
     * @param title    文本内容
     * @param barColor 血条颜色
     * @param style    血条样式
     * @param barFlags 额外标签
     */
    public static void sendBossBar(String title, BarColor barColor, BarStyle style, BarFlag... barFlags) {
        title = ColorUtil.translate(title);
        Bukkit.createBossBar(title, barColor, style, barFlags);
    }

    /**
     * 发送控制台消息
     * @param message 消息内容
     */
    public static void sendConsole(String message) {
        send(Bukkit.getConsoleSender(), message);
    }

    /**
     * 给目标发送通知
     *
     * @param sender  目标
     * @param message 消息内容
     */
    public static void send(CommandSender sender, String message) {
        if (message.isEmpty() || message.equals("none")) {
            return;
        }
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.matches()) {
            StringBuilder builder = new StringBuilder();
            while (matcher.find()) {
                String text = matcher.group();
                String langText = getLangText(text);
                matcher.appendReplacement(builder, langText);
            }
            matcher.appendTail(builder);
            message = builder.toString();
        }
        message = ColorUtil.translate(message);
        send(sender, "", message);
    }

    /**
     * 获取指定语言键对应的语言文本。
     * 如果键以括号开头并以括号结尾，则移除括号并尝试从语言文件中翻译该键；
     * 如果键不满足以上条件，则直接返回该键。
     *
     * @param key 语言键，可能包含括号。
     * @return 如果键被括号包围，则返回翻译后的文本；否则返回原始键。
     */
    private static String getLangText(String key) {
        // 检查key是否以括号开头和结尾，如果是，则移除括号并尝试进行翻译
        if (key.startsWith("(") && key.endsWith(")")) {
            // 移除括号
            key = key.substring(1, key.length() - 1);
            // 进行翻译并返回
//            return NytPluginTemplate.getLanguageManager().translate(key);
            return Translator.translate(key);
        }
        // 如果key不包含括号，则直接返回原始key
        return key;
    }

    /**
     * 给目标发送标题
     *
     * @param player   目标玩家
     * @param title    标题内容
     * @param subtitle 副标题内容
     */
    public static void sendTitle(Player player, String title, String subtitle) {
        if (player == null) {
            return;
        }
        title = ColorUtil.translate(title);
        subtitle = ColorUtil.translate(subtitle);
        sendTitle(player, title, subtitle, 10, 70, 20);
    }

    /**
     * 给目标发送标题
     *
     * @param player   目标玩家
     * @param title    标题内容
     * @param subtitle 副标题内容
     * @param fadeIn   淡入时长
     * @param stay     停留时长
     * @param fadeOut  淡出时长
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (player == null) {
            return;
        }
        title = ColorUtil.translate(title);
        subtitle = ColorUtil.translate(subtitle);
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * 重置玩家标题
     *
     * @param player 目标玩家
     */
    public static void resetTitle(Player player) {
        if (player == null) {
            return;
        }
        player.resetTitle();
    }

    /**
     * 给目标发送通知
     *
     * @param sender  目标
     * @param prefix  消息前缀
     * @param message 消息内容
     */
    public static void send(CommandSender sender, String prefix, String message) {
        String content = prefix + message;
        content = ColorUtil.translate(content);
        sender.sendMessage(content);
    }
}
