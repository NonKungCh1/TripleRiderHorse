package com.nonkungch.triplerider;

import org.bukkit.plugin.java.JavaPlugin;

public class TripleRiderPlugin extends JavaPlugin {
    private SeatManager seatManager;

    @Override
    public void onEnable() {
        getLogger().info("TripleRiderHorse has been enabled and ready for 1.21+ servers!");

        // 1. สร้าง SeatManager
        this.seatManager = new SeatManager(this);
        
        // 2. ลงทะเบียน Event Listener
        getServer().getPluginManager().registerEvents(new HorseListener(this.seatManager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("TripleRiderHorse is shutting down. Cleaning up temporary seats...");
        
        // *สำคัญ* ลบ Armor Stands ทั้งหมดที่เราสร้างขึ้น
        this.seatManager.cleanupAllSeats();
    }
}
