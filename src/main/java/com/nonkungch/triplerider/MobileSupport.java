package com.nonkungch.triplerider;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class MobileSupport {

    public static boolean isBedrockPlayer(Player player) {
        try {
            // ไม่ต้องตรวจสอบ isEnabled() อีกต่อไป
            // ถ้า FloodgateApi.getInstance() ไม่เป็น null ก็ให้ตรวจสอบผู้เล่น
            FloodgateApi api = FloodgateApi.getInstance();
            if (api == null) {
                 return false;
            }
            
            return api.isFloodgatePlayer(player.getUniqueId());
            
        } catch (NoClassDefFoundError | Exception e) {
            // เกิดข้อผิดพลาดหาก Floodgate ไม่ได้ถูกโหลด/ติดตั้ง
            return false;
        }
    }
}