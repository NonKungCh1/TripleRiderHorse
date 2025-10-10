package com.nonkungch.triplerider;

import com.nonkungch.triplerider.TripleRiderHorse;
import com.nonkungch.triplerider.MobileSupport; // ⭐️ import ถูกต้อง เพราะ MobileSupport อยู่ใน package หลัก
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
        
        // 1. ตรวจสอบสิทธิ์
        if (!player.hasPermission("triplerider.toggle")) {
            plugin.getLangManager().sendMessage(player, "error_no_permission");
            return true;
        }

        // 2. การจัดการคำสั่งย่อย
        if (args.length == 0 || args[0].equalsIgnoreCase("toggle")) {
            
            // ⭐️ ตรวจสอบผู้เล่นมือถือ: ถ้าเป็น Bedrock และพยายามเปิด GUI (พิมพ์คำสั่งเปล่า)
            if (args.length == 0 && MobileSupport.isBedrockPlayer(player)) {
                plugin.getLangManager().sendMessage(player, "info_use_command_instead");
                return true;
            }

            // **********************************************
            // * ใส่โค้ดเดิมของคุณที่นี่เพื่อสลับโหมดขี่สามคน *
            // **********************************************
            boolean newState = true; 

            if (newState) {
                plugin.getLangManager().sendMessage(player, "command_success_enabled");
            } else {
                plugin.getLangManager().sendMessage(player, "command_success_disabled");
            }
            return true;
        }
        
        // คำสั่งไม่ถูกต้อง
        plugin.getLangManager().sendMessage(player, "error_unknown_command");
        return true;
    }
}