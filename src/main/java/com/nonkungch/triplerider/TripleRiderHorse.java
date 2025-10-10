package com.nonkungch.triplerider;

import org.bukkit.plugin.java.JavaPlugin;

public final class TripleRiderHorse extends JavaPlugin {

    private LangManager langManager;
    private SeatManager seatManager; // เพิ่มตัวแปรสำหรับ SeatManager

    @Override
    public void onEnable() {
        // บันทึก config.yml
        saveDefaultConfig(); 
        
        // 1. โหลด LangManager
        this.langManager = new LangManager(this);
        
        // 2. สร้าง SeatManager
        int maxRiders = getConfig().getInt("max-riders", 3); 
        this.seatManager = new SeatManager(this, maxRiders); // สร้าง SeatManager
        
        // 3. ลงทะเบียน Listener และ Command
        // ลงทะเบียน HorseListener และส่ง plugin instance (this) ไป
        getServer().getPluginManager().registerEvents(new HorseListener(this), this); 
        getCommand("triplerider").setExecutor(new TripleRiderCommand(this)); 
        
        getLogger().info("TripleRiderHorse (v" + getDescription().getVersion() + ") enabled!");
    }

    @Override
    public void onDisable() {
        // ทำความสะอาด Armor Stands เมื่อปิดปลั๊กอิน
        if (this.seatManager != null) {
             this.seatManager.cleanupAllSeats();
        }
        getLogger().info("TripleRiderHorse disabled.");
    }

    public LangManager getLangManager() {
        return langManager;
    }
    
    public SeatManager getSeatManager() {
        return seatManager;
    }
}