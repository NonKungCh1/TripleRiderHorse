package com.nonkungch.triplerider;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LangManager {
    private final TripleRiderHorse plugin;
    private final Map<String, YamlConfiguration> langFiles = new HashMap<>();
    private final String defaultLang;

    public LangManager(TripleRiderHorse plugin) {
        this.plugin = plugin;
        this.defaultLang = plugin.getConfig().getString("default-lang", "en");
        loadLanguages();
    }

    private void loadLanguages() {
        loadLangFile("en"); 
        loadLangFile("th");
    }

    private void loadLangFile(String lang) {
        File langFile = new File(plugin.getDataFolder(), "messages_" + lang + ".yml");
        
        if (!langFile.exists()) {
            try {
                plugin.saveResource("messages_" + lang + ".yml", false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Could not find messages_" + lang + ".yml in the JAR. Skipping language.");
                return;
            }
        }
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(langFile);
        langFiles.put(lang, config);
        plugin.getLogger().info("Loaded language file: messages_" + lang + ".yml");
    }
    
    // ดึงรหัสภาษาของผู้เล่น (แก้ไข: เปลี่ยนเป็น public)
    public String getPlayerLangCode(Player player) { 
        String locale = player.getLocale();
        if (locale != null) {
            String langCode = locale.toLowerCase(Locale.ROOT).substring(0, 2);
            if (langFiles.containsKey(langCode)) {
                return langCode;
            }
        }
        return defaultLang; // ใช้ภาษาเริ่มต้นตาม config.yml
    }

    // ดึงข้อความดิบและแปลงโค้ดสี
    public String getMessage(String lang, String key) {
        YamlConfiguration config = langFiles.getOrDefault(lang, langFiles.get(defaultLang));
        
        String message = config.getString(key, "§c[ERROR] Missing message key: " + key);
        
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // ส่งข้อความพร้อม Prefix ให้ผู้เล่น โดยใช้ภาษาของผู้เล่น
    public void sendMessage(Player player, String key) {
        String lang = getPlayerLangCode(player);
        String prefix = getMessage(lang, "prefix");
        String message = getMessage(lang, key);
        
        player.sendMessage(prefix + message);
    }
}