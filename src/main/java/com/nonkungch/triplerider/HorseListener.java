package com.nonkungch.triplerider;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.Material;

public class HorseListener implements Listener {

    private final SeatManager seatManager;

    public HorseListener(SeatManager seatManager) {
        this.seatManager = seatManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        // 1. ตรวจสอบว่าเป็น Horse, Donkey หรือ Mule (รวมอยู่ใน AbstractHorse)
        if (!(event.getRightClicked() instanceof AbstractHorse horse)) {
            return;
        }

        Player player = event.getPlayer();
        
        // 2. ป้องกันการคลิกถ้าผู้เล่นกำลังถืออาน (SADDLE) อยู่ เพื่อไม่ให้รบกวนการใส่/ถอดอาน
        if (player.getInventory().getItemInMainHand().getType() == Material.SADDLE) {
            return;
        }
        
        // 3. ม้าต้องมีคนขี่อยู่แล้ว (ผู้เล่นคนแรก) และคนขี่ต้องไม่ใช่ผู้เล่นที่คลิก
        if (horse.getPassengers().isEmpty() || horse.getPassengers().get(0) == player) {
            return;
        }
        
        // 4. ลองเพิ่มผู้เล่นเป็นผู้โดยสาร
        boolean added = seatManager.addPassengerToHorse(horse, player);
        
        if (added) {
            event.setCancelled(true); // ยกเลิกการคลิกมาตรฐาน
            player.sendMessage("§a[TripleRider] You have joined the ride!");
        } else {
            // ถ้าที่นั่งเต็ม
            player.sendMessage("§c[TripleRider] The horse is full! Maximum 3 riders.");
        }
    }
}
