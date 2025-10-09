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
    private int maxRiders; // เปลี่ยนเป็น mutable เพื่อให้ปรับได้ในเกม
    
    private static final double SEAT_DISTANCE = 0.5;

    public SeatManager(Plugin plugin, int maxRiders) {
        this.plugin = plugin;
        this.maxRiders = Math.max(1, maxRiders); 
    }
    
    public int getMaxRiders() {
        return maxRiders;
    }
    
    /**
     * ตั้งค่า Max Riders ใหม่
     */
    public void setMaxRiders(int newMax) {
        this.maxRiders = Math.max(1, newMax);
    }

    /**
     * พยายามเพิ่มผู้เล่นเป็นผู้โดยสารบนม้า
     * @return true ถ้าเพิ่มสำเร็จ, false ถ้าที่นั่งเต็ม
     */
    public boolean addPassengerToHorse(AbstractHorse horse, Player newPassenger) {
        
        // ปลั๊กอินปิดใช้งานหาก maxRiders = 1
        if (maxRiders <= 1) return false;
        
        List<Entity> horsePassengers = horse.getPassengers();
        if (horsePassengers.isEmpty()) return false; // ม้าไม่มีคนขับ
        
        int currentRiders = 1; // นับคนขับ (rider1)
        Entity currentCarrier = horsePassengers.get(0); // เริ่มจากคนขับ
        
        // วนลูปเพื่อหา Armor Stand สุดท้าย
        while (currentCarrier != null) {
            List<Entity> passengers = currentCarrier.getPassengers();
            
            if (passengers.isEmpty()) {
                break; // ถึงจุดสุดท้ายของการนั่งซ้อนแล้ว
            }
            
            Entity next = passengers.get(0);
            
            if (!(next instanceof ArmorStand seat) || !createdSeats.contains(seat)) {
                break; // ไม่ใช่ Armor Stand ที่เราสร้าง
            }
            
            // นับผู้โดยสารที่นั่งบน Armor Stand
            if (!seat.getPassengers().isEmpty() && seat.getPassengers().get(0) instanceof Player) {
                currentRiders++;
            }
            
            // ตรวจสอบขีดจำกัด
            if (currentRiders >= maxRiders) {
                return false; // ที่นั่งเต็ม
            }
            
            // ถ้า Armor Stand ตัวนี้ยังไม่มีผู้โดยสารนั่ง
            if (seat.getPassengers().isEmpty()) {
                 // **หมายเหตุ:** ในการติดตั้งแบบซ้อน, ArmorStand จะถูกนั่งโดย Player. 
                 // ถ้า ArmorStand มีผู้โดยสารอยู่แล้ว นั่นคือ Player.
                 // ถ้า ArmorStand ไม่มีผู้โดยสาร นั่นคือมันถูกใช้เป็นที่นั่งเปล่าเพื่อซ้อน Entity ถัดไป
                 // ซึ่งตามลอจิกที่ใช้, เราจะให้ผู้โดยสารนั่งบน ArmorStand ตัวสุดท้าย
            }
            
            currentCarrier = next; // ไปยัง Entity ถัดไป
        }
        
        // ตรวจสอบอีกครั้งก่อนสร้างที่นั่งใหม่
        if (currentRiders < maxRiders) {
            
            // 1. หา Carrier ตัวสุดท้ายที่ไม่มีผู้โดยสาร/Armor Stand นั่งซ้อนอยู่
            Entity lastCarrier = horsePassengers.get(0);
            while (!lastCarrier.getPassengers().isEmpty() && lastCarrier.getPassengers().get(0) instanceof ArmorStand) {
                lastCarrier = lastCarrier.getPassengers().get(0);
            }
            
            // 2. สร้างที่นั่งใหม่บน Entity สุดท้ายที่พบ
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
        
        // 1. Armor Stand ตัวใหม่นั่งบน Entity ตัวก่อนหน้า
        carrier.addPassenger(seat);
        
        // 2. Player นั่งบน Armor Stand ตัวใหม่
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