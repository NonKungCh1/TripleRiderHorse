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
        
        boolean added = seatManager.addPassengerToHorse(horse, player);
        
        if (added) {
            event.setCancelled(true);
            player.sendMessage("§a[TripleRider] คุณได้เข้าร่วมการเดินทาง!");
        } else {
            player.sendMessage("§c[TripleRider] ม้าเต็ม! จำนวนสูงสุด: " + seatManager.getMaxRiders() + " คน");
        }
    }
}