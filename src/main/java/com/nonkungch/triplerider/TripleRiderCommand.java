package com.nonkungch.triplerider;

import com.nonkungch.triplerider.TripleRiderHorse;
import com.nonkungch.triplerider.MobileSupport; 
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class TripleRiderCommand implements CommandExecutor {

    private final TripleRiderHorse plugin;

    public TripleRiderCommand(TripleRiderHorse plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // ตรวจสอบว่าเป็นผู้เล่น
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getLangManager().getMessage("en", "prefix") + plugin.getLangManager().getMessage("en", "error_player_only"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // 1. ตรวจสอบสิทธิ์สำหรับคำสั่งทั่วไป
        if (args.length == 0 && !player.hasPermission("triplerider.use")) { 
             plugin.getLangManager().sendMessage(player, "error_no_permission");
             return true;
        }

        // 2. การจัดการคำสั่งย่อย (Set Max Riders)
        if (args.length >= 1 && args[0].equalsIgnoreCase("set")) {
            
            if (!player.hasPermission("triplerider.admin")) { 
                plugin.getLangManager().sendMessage(player, "error_no_permission");
                return true;
            }

            if (args.length != 2) {
                // แจ้งวิธีใช้
                plugin.getLangManager().sendMessage(player, "info_set_usage"); 
                return true;
            }

            try {
                int newMax = Integer.parseInt(args[1]);
                if (newMax < 1) {
                    plugin.getLangManager().sendMessage(player, "error_min_riders"); 
                    return true;
                }
                
                // ⭐️ ตั้งค่าใน SeatManager และบันทึกใน config
                plugin.getSeatManager().setMaxRiders(newMax);
                plugin.getConfig().set("max-riders", newMax);
                plugin.saveConfig();
                
                plugin.getLangManager().sendMessage(player, "command_success_set"); 
                
            } catch (NumberFormatException e) {
                plugin.getLangManager().sendMessage(player, "error_invalid_number"); 
            }
            return true;
        }
        
        // 3. คำสั่งเริ่มต้น: พยายามขึ้นม้า (สำหรับผู้เล่นมือถือ) หรือส่งข้อความช่วยเหลือ
        if (args.length == 0) { 
            
            // ⭐️⭐️ ส่วนแก้ไข: ตรรกะการขึ้นม้าผ่านคำสั่ง
            // ตรวจสอบว่าผู้เล่นมองม้าอยู่หรือไม่ (ระยะ 5 บล็อก)
            if (player.getTargetEntity(5) instanceof AbstractHorse horse) { 
                
                // ต้องมีผู้ขี่อยู่ก่อนแล้ว และผู้ขี่คนแรกต้องไม่ใช่ตัวผู้เล่นเอง
                if (horse.getPassengers().isEmpty() || horse.getPassengers().get(0) == player) {
                    // ใช้ข้อความแจ้งสำหรับกรณีไม่มีผู้ขี่หลัก (ถ้ามี)
                    // เนื่องจากไม่มี message key 'error_must_have_rider' เราจะใช้ข้อความช่วยเหลือแทน
                    plugin.getLangManager().sendMessage(player, "info_help_command"); 
                    return true;
                }
                
                // พยายามเพิ่มผู้โดยสาร
                boolean added = plugin.getSeatManager().addPassengerToHorse(horse, player);
                
                if (added) {
                    plugin.getLangManager().sendMessage(player, "info_rider_success"); 
                } else {
                    // แจ้งว่าที่นั่งเต็ม
                    String lang = plugin.getLangManager().getPlayerLangCode(player);
                    String messageKey = "error_max_riders_reached";
                    String rawMessage = plugin.getLangManager().getMessage(lang, messageKey);
                    rawMessage = rawMessage.replace("{max}", String.valueOf(plugin.getSeatManager().getMaxRiders()));
                    String prefix = plugin.getLangManager().getMessage(lang, "prefix");
                    player.sendMessage(prefix + rawMessage);
                }
                return true; // จบการทำงานเมื่อพยายามขึ้นม้าแล้ว
            }
            // ⭐️⭐️ สิ้นสุดส่วนแก้ไข

            // ตรวจสอบผู้เล่นมือถือ (ข้อความแจ้งใช้คำสั่ง)
            if (MobileSupport.isBedrockPlayer(player)) {
                // ถ้ามองไม่เห็นม้า ให้ส่งข้อความแจ้งการใช้งาน
                plugin.getLangManager().sendMessage(player, "info_use_command_instead");
                return true;
            }

            // ส่งข้อความช่วยเหลือ/สถานะปัจจุบัน (สำหรับ Java/มองไม่เห็นม้า)
            plugin.getLangManager().sendMessage(player, "info_help_command"); 
            return true;
        }

        // คำสั่งไม่ถูกต้อง
        plugin.getLangManager().sendMessage(player, "error_unknown_command");
        return true;
    }
}
