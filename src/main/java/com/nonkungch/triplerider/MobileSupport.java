package com.nonkungch.triplerider;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi; // ⭐️ Floodgate จะถูกหาเจอเมื่อแก้ pom.xml

public class MobileSupport {

    public static boolean isBedrockPlayer(Player player) {
        try {
            // ต้องมีการติดตั้ง Floodgate API บนเซิร์ฟเวอร์
            if (!FloodgateApi.getInstance().isEnabled()) {
                return false;
            }
            return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (NoClassDefFoundError | Exception e) {
            // เกิดข้อผิดพลาดหาก Floodgate ไม่ได้ถูกโหลด
            return false;
        }
    }
}