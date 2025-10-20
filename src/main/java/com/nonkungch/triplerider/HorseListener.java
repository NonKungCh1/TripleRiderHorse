package com.nonkungch.triplerider;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.Material;

public class HorseListener implements Listener {

    private final TripleRiderHorse plugin;

    public HorseListener(TripleRiderHorse plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractHorse horse)) {
            return;
        }

        Player player = event.getPlayer();
        
        // ⭐️ แก้ไข: ผู้เล่น Bedrock (มือถือ) ต้องใช้คำสั่ง /triplerider เพื่อขึ้นม้าซ้อน
        if (MobileSupport.isBedrockPlayer(player)) {
            // หยุดการประมวลผลการคลิกขวา เพื่อบังคับให้ผู้เล่นมือถือใช้คำสั่งแทน
            // ข้อความแจ้งจะไปอยู่ใน TripleRiderCommand
            return; 
        }
        
        // ไม่ทำงานหากผู้เล่นถืออานม้า
        if (player.getInventory().getItemInMainHand().getType() == Material.SADDLE) {
            return;
        }
        
        // ไม่ทำงานหากม้าไม่มีผู้ขี่ หรือผู้เล่นคนแรกคือผู้ที่คลิกเอง
        if (horse.getPassengers().isEmpty() || horse.getPassengers().get(0) == player) {
            return;
        }
        
        // ใช้ SeatManager จาก plugin instance
        boolean added = plugin.getSeatManager().addPassengerToHorse(horse, player);
        
        if (added) {
            event.setCancelled(true);
            // ใช้ LangManager สำหรับข้อความสำเร็จ
            plugin.getLangManager().sendMessage(player, "info_rider_success"); 
        } else {
            // ใช้ LangManager สำหรับข้อความเต็ม (แจ้งจำนวนผู้ขี่สูงสุด)
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
