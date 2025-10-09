package com.nonkungch.triplerider;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TripleRiderCommand implements CommandExecutor, TabCompleter {

    private final TripleRiderPlugin plugin;

    public TripleRiderCommand(TripleRiderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("triplerider.admin")) {
            sender.sendMessage(ChatColor.RED + "คุณไม่มีสิทธิ์ใช้คำสั่งนี้");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "--- TripleRiderHorse Command Help ---");
            sender.sendMessage(ChatColor.YELLOW + "/" + label + " reload" + ChatColor.GRAY + " - รีโหลดคอนฟิกปลั๊กอิน");
            sender.sendMessage(ChatColor.YELLOW + "/" + label + " setmax <number>" + ChatColor.GRAY + " - ตั้งจำนวนผู้โดยสารสูงสุด (รวมคนขับ)");
            sender.sendMessage(ChatColor.YELLOW + "จำนวนผู้โดยสารสูงสุดปัจจุบัน: " + ChatColor.AQUA + plugin.getSeatManager().getMaxRiders());
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("reload")) {
            plugin.loadPluginConfig();
            sender.sendMessage(ChatColor.GREEN + "[TripleRider] รีโหลดคอนฟิกสำเร็จ");
            sender.sendMessage(ChatColor.GREEN + "จำนวนผู้โดยสารสูงสุดใหม่: " + ChatColor.AQUA + plugin.getSeatManager().getMaxRiders());
            return true;
        } 
        
        if (subCommand.equals("setmax")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "การใช้งาน: /" + label + " setmax <จำนวน>");
                return true;
            }
            
            try {
                int newMax = Integer.parseInt(args[1]);
                if (newMax < 1) {
                    sender.sendMessage(ChatColor.RED + "จำนวนผู้โดยสารสูงสุดต้องเป็น 1 หรือมากกว่า");
                    return true;
                }
                
                plugin.setMaxRiders(newMax);
                sender.sendMessage(ChatColor.GREEN + "[TripleRider] ตั้งจำนวนผู้โดยสารสูงสุดเป็น " + ChatColor.AQUA + newMax + " สำเร็จ");
                
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "รูปแบบตัวเลขไม่ถูกต้อง. กรุณาใส่จำนวนเต็มที่ถูกต้อง");
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED + "ไม่พบคำสั่งย่อย. ใช้ /" + label + " เพื่อดูคำสั่งทั้งหมด");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("reload", "setmax"), new ArrayList<>());
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("setmax")) {
            return StringUtil.copyPartialMatches(args[1], Collections.singletonList(String.valueOf(plugin.getSeatManager().getMaxRiders())), new ArrayList<>());
        }
        return Collections.emptyList();
    }
}