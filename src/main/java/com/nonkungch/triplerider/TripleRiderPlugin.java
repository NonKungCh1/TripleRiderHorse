package com.nonkungch.triplerider;

import com.nonkungch.triplerider.TripleRiderCommand; // ⭐️ ต้องมีโฟลเดอร์ commands/
import org.bukkit.plugin.java.JavaPlugin;

public final class TripleRiderHorse extends JavaPlugin {

    private LangManager langManager;

    @Override
    public void onEnable() {
        // บันทึก config.yml
        saveDefaultConfig(); 
        
        // 1. โหลด LangManager (อยู่ใน package หลักแล้ว)
        this.langManager = new LangManager(this);
        
        // 2. ตั้งค่า Command
        getCommand("triplerider").setExecutor(new TripleRiderCommand(this)); 
        
        getLogger().info("TripleRiderHorse (v" + getDescription().getVersion() + ") enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TripleRiderHorse disabled.");
    }

    public LangManager getLangManager() {
        return langManager;
    }
}