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
        
        // 4. ลงทะเบียน Command Executor
        getCommand("triplerider").setExecutor(new TripleRiderCommand(this));
        getCommand("triplerider").setTabCompleter(new TripleRiderCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("TripleRiderHorse is shutting down. Cleaning up temporary seats...");
        this.seatManager.cleanupAllSeats();
    }
    
    public void loadPluginConfig() {
        saveDefaultConfig();
        reloadConfig();
        
        if (this.seatManager != null) {
            int newMaxRiders = getConfig().getInt("max-riders", 3);
            this.seatManager.setMaxRiders(newMaxRiders);
            getLogger().info("Configuration reloaded. New Max Riders: " + newMaxRiders);
        }
    }
    
    public void setMaxRiders(int newMax) {
        getConfig().set("max-riders", newMax);
        saveConfig();
        
        if (this.seatManager != null) {
            this.seatManager.setMaxRiders(newMax);
        }
        getLogger().info("Max Riders set to " + newMax + " via command.");
    }
    
    public SeatManager getSeatManager() {
        return seatManager;
    }
}