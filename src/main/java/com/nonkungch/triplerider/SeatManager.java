package com.nonkungch.triplerider;

import org.bukkit.Location;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatManager {

    private final Set<ArmorStand> createdSeats = new HashSet<>();
    private final Plugin plugin;
    private int maxRiders; 
    
    private static final double SEAT_DISTANCE = 0.5;

    public SeatManager(Plugin plugin, int maxRiders) {
        this.plugin = plugin;
        this.maxRiders = Math.max(1, maxRiders); 
    }
    
    public int getMaxRiders() {
        return maxRiders;
    }
    
    public void setMaxRiders(int newMax) {
        this.maxRiders = Math.max(1, newMax);
    }

    public boolean addPassengerToHorse(AbstractHorse horse, Player newPassenger) {
        
        if (maxRiders <= 1) return false;
        
        List<Entity> horsePassengers = horse.getPassengers();
        if (horsePassengers.isEmpty()) return false; 
        
        int currentRiders = 1; 
        Entity lastCarrier = horsePassengers.get(0); 
        
        // วนลูปเพื่อหา Entity สุดท้าย (Armor Stand) ที่ผู้เล่นคนใหม่จะไปนั่ง
        while (!lastCarrier.getPassengers().isEmpty() && lastCarrier.getPassengers().get(0) instanceof ArmorStand seat) {
            
            // นับผู้โดยสารที่นั่งบน Armor Stand
            if (!seat.getPassengers().isEmpty() && seat.getPassengers().get(0) instanceof Player) {
                currentRiders++;
            }
            
            // ตรวจสอบขีดจำกัด
            if (currentRiders >= maxRiders) {
                return false; 
            }

            // ถ้าถึงจุดนี้ แสดงว่ายังมีที่ว่างและต้องไปนั่งซ้อนต่อจาก Armor Stand ตัวนี้
            lastCarrier = seat;
        }
        
        // สร้างที่นั่งใหม่บน Entity สุดท้ายที่ไม่มีผู้โดยสาร/Armor Stand นั่งซ้อนอยู่
        if (currentRiders < maxRiders) {
            createSeatAndBoard(lastCarrier, newPassenger);
            return true;
        }
        
        return false;
    }

    private void createSeatAndBoard(Entity carrier, Player passenger) {
        Location loc = carrier.getLocation();
        
        // คำนวณตำแหน่งที่นั่งด้านหลัง carrier
        loc = loc.subtract(carrier.getLocation().getDirection().multiply(SEAT_DISTANCE));
        loc.setY(loc.getY() + 0.1); 

        ArmorStand seat = (ArmorStand) loc.getWorld().spawn(loc, ArmorStand.class);
        
        seat.setVisible(false);
        seat.setGravity(false);
        seat.setBasePlate(false);
        seat.setMarker(true);
        
        carrier.addPassenger(seat);
        seat.addPassenger(passenger);
        
        createdSeats.add(seat);
    }
    
    public void cleanupAllSeats() {
        createdSeats.forEach(seat -> {
            if (seat.isValid()) {
                seat.remove();
            }
        });
        createdSeats.clear();
    }
}