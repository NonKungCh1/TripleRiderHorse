package com.nonkungch.triplerider;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.Material;

public class HorseListener implements Listener {

    private final TripleRiderHorse plugin; // ⭐️ เปลี่ยนเป็นเก็บ plugin instance

    public HorseListener(TripleRiderHorse plugin) { // ⭐️ เปลี่ยน Constructor
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractHorse horse)) {
            return;
        }

        Player player = event.getPlayer();
        
        if (player.getInventory().getItemInMainHand().getType() == Material.SADDLE) {
            return;
        }
        
        if (horse.getPassengers().isEmpty() || horse.getPassengers().get(0) == player) {
            return;
        }
        
        // ใช้ SeatManager จาก plugin instance
        boolean added = plugin.getSeatManager().addPassengerToHorse(horse, player);
        
        if (added) {
            event.setCancelled(true);
            // ⭐️ ใช้ LangManager สำหรับข้อความสำเร็จ
            plugin.getLangManager().sendMessage(player, "info_rider_success"); 
        } else {
            // ⭐️ ใช้ LangManager สำหรับข้อความเต็ม
            String lang = plugin.getLangManager().getPlayerLangCode(player);
            String messageKey = "error_max_riders_reached";
            
            // ดึงข้อความดิบและแทนที่ {max} ด้วยตัวเลขสูงสุด
            String rawMessage = plugin.getLangManager().getMessage(lang, messageKey);
            rawMessage = rawMessage.replace("{max}", String.valueOf(plugin.getSeatManager().getMaxRiders()));
            
            String prefix = plugin.getLangManager().getMessage(lang, "prefix");
            
            player.sendMessage(prefix + rawMessage);
        }
    }
}