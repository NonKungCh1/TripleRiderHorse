package com.nonkungch.triplerider;

import com.nonkungch.triplerider.TripleRiderHorse;
import com.nonkungch.triplerider.MobileSupport; 
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        
        // 3. คำสั่งเริ่มต้น: ตรวจสอบผู้เล่นมือถือ หรือส่งข้อความช่วยเหลือ
        if (args.length == 0) { 
            // ตรวจสอบผู้เล่นมือถือ
            if (MobileSupport.isBedrockPlayer(player)) {
                plugin.getLangManager().sendMessage(player, "info_use_command_instead");
                return true;
            }

            // ส่งข้อความช่วยเหลือ/สถานะปัจจุบัน
            plugin.getLangManager().sendMessage(player, "info_help_command"); 
            return true;
        }

        // คำสั่งไม่ถูกต้อง
        plugin.getLangManager().sendMessage(player, "error_unknown_command");
        return true;
    }
}