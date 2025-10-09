package com.nonkungch.triplerider;

import org.bukkit.plugin.java.JavaPlugin;

public class TripleRiderPlugin extends JavaPlugin {

    private SeatManager seatManager;

    @Override
    public void onEnable() {
        // 1. โหลดคอนฟิก
        loadPluginConfig(); 
        
        // 2. โหลดค่า Max Riders และ Initialize SeatManager
        int maxRiders = getConfig().getInt("max-riders", 3);
        this.seatManager = new SeatManager(this, maxRiders);
        
        getLogger().info("TripleRiderHorse has been enabled. Max Riders: " + maxRiders);
        
        // 3. ลงทะเบียน Listener
        getServer().getPluginManager().registerEvents(new HorseListener(this.seatManager), this);
        
        // 4. ลงทะเบียน Command Executor (ใช้สำหรับจัดการคำสั่งในเกม)
        getCommand("triplerider").setExecutor(new TripleRiderCommand(this));
        getCommand("triplerider").setTabCompleter(new TripleRiderCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("TripleRiderHorse is shutting down. Cleaning up temporary seats...");
        this.seatManager.cleanupAllSeats();
    }
    
    /**
     * โหลด config.yml ใหม่และอัปเดต SeatManager
     */
    public void loadPluginConfig() {
        // สร้าง config.yml ถ้าไม่มี
        saveDefaultConfig();
        // โหลดค่าจากไฟล์ซ้ำ
        reloadConfig();
        
        if (this.seatManager != null) {
            int newMaxRiders = getConfig().getInt("max-riders", 3);
            this.seatManager.setMaxRiders(newMaxRiders);
            getLogger().info("Configuration reloaded. New Max Riders: " + newMaxRiders);
        }
    }
    
    /**
     * ตั้งค่า Max Riders ใหม่ทั้งใน config และ SeatManager (ใช้สำหรับคำสั่งในเกม)
     */
    public void setMaxRiders(int newMax) {
        // อัปเดตไฟล์ config ในหน่วยความจำและบันทึกลงดิสก์
        getConfig().set("max-riders", newMax);
        saveConfig();
        
        // อัปเดต SeatManager ที่กำลังทำงานอยู่
        if (this.seatManager != null) {
            this.seatManager.setMaxRiders(newMax);
        }
    }
    
    public SeatManager getSeatManager() {
        return seatManager;
    }
}