package com.nonkungch.triplerider;

import org.bukkit.Location;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.HashSet;
import java.util.Set;

public class SeatManager {

    // เก็บ Armor Stands ทั้งหมดที่เราสร้างไว้ในหน่วยความจำ
    private final Set<ArmorStand> createdSeats = new HashSet<>();
    private final Plugin plugin;
    
    // ระยะห่างระหว่างผู้โดยสารแต่ละคน
    private static final double SEAT_DISTANCE = 0.5; 

    public SeatManager(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * พยายามเพิ่มผู้เล่นเป็นผู้โดยสารบนม้า
     * @return true ถ้าเพิ่มสำเร็จ, false ถ้าที่นั่งเต็ม (3 คน)
     */
    public boolean addPassengerToHorse(AbstractHorse horse, Player newPassenger) {
        
        // ผู้เล่นคนที่ 1 (คนขับ)
        Entity rider1 = horse.getPassengers().get(0); 
        
        // 1. ตรวจสอบ "ที่นั่ง 2" (Armor Stand ที่อยู่บน Rider1)
        Entity seat2 = rider1.getPassengers().isEmpty() ? null : rider1.getPassengers().get(0);
        if (seat2 == null) {
            // ที่นั่ง 2 ว่าง!
            createSeatAndBoard(rider1, newPassenger, 1);
            return true;
        }
        
        // 2. ตรวจสอบ "ที่นั่ง 3" (Armor Stand ที่อยู่บน Seat2)
        Entity seat3 = seat2.getPassengers().isEmpty() ? null : seat2.getPassengers().get(0);
        if (seat3 == null) {
            // ที่นั่ง 3 ว่าง!
            createSeatAndBoard(seat2, newPassenger, 2);
            return true;
        }
        
        // ที่นั่งเต็ม
        return false;
    }

    /**
     * สร้าง Armor Stand และให้ผู้เล่นนั่งบนมัน
     * @param carrier Entity ที่เป็นตัวหลัก (ม้า, Armor Stand อื่น)
     * @param passenger ผู้เล่นที่จะนั่ง
     * @param seatIndex ตำแหน่งที่นั่ง (1: หลัง Rider1, 2: หลัง Seat2)
     */
    private void createSeatAndBoard(Entity carrier, Player passenger, int seatIndex) {
        Location loc = carrier.getLocation();
        
        // คำนวณตำแหน่งด้านหลัง Entity หลัก โดยใช้ทิศทางที่ Entity หลักหันหน้าไป
        // เพื่อให้ที่นั่งตามไปด้านหลังเสมอ
        loc = loc.subtract(carrier.getLocation().getDirection().multiply(SEAT_DISTANCE * seatIndex));
        
        // ตำแหน่งผู้โดยสารต้องอยู่ต่ำลงเล็กน้อยเพื่อให้ดูสมจริง
        loc.setY(loc.getY() + 0.1); 

        // สร้าง Armor Stand
        ArmorStand seat = (ArmorStand) loc.getWorld().spawn(loc, ArmorStand.class);
        
        // ตั้งค่าให้ Armor Stand เป็นที่นั่งที่เหมาะสม
        seat.setVisible(false);     // มองไม่เห็น
        seat.setGravity(false);     // ไม่มีแรงโน้มถ่วง
        seat.setBasePlate(false);   // ไม่มีฐาน
        seat.setMarker(true);       // ป้องกันการชน (Collision)
        
        // ผูก Armor Stand เข้ากับ Entity ตัวหลัก
        carrier.addPassenger(seat);
        
        // ให้ผู้เล่นนั่งบน Armor Stand
        seat.addPassenger(passenger);
        
        // บันทึกไว้เพื่อทำความสะอาดในภายหลัง
        createdSeats.add(seat);
    }
    
    /**
     * ทำความสะอาด (ลบ) Armor Stands ที่สร้างขึ้นทั้งหมด
     * เมื่อปลั๊กอินปิดตัวลงหรือเซิร์ฟเวอร์ปิด
     */
    public void cleanupAllSeats() {
        createdSeats.forEach(seat -> {
            if (seat.isValid()) {
                seat.remove();
            }
        });
        createdSeats.clear();
    }
                               }
