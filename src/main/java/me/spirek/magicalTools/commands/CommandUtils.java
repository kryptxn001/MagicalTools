package me.spirek.magicalTools.commands;

import me.spirek.magicalTools.MagicalTools;
import org.bukkit.entity.Player;

public class CommandUtils {
    public static void sendMessageBranded(Player player, String text) {
        player.sendMessage("§b§l[MagicalTools] §r§b"+text);
    }

    public static void sendNegativeMessageBranded(Player player, String text) {
        player.sendMessage("§c§l[MagicalTools] §r§c"+text);
    }

    public static void sendNotPermissions(Player player) {
        player.sendMessage(MagicalTools.Settings.getPermissionsText());
    }

    public static void sendCooldownMessage(Player player, Double time) {
        if(MagicalTools.Settings.shouldShowCooldown()) {
            player.sendMessage(MagicalTools.Settings.getCooldownMessage().replace("{time}",time.toString()));
            //player.sendMessage("§c§l[MagicalTools] §r§cYou can't use it! Cooldown remaining: "+time+"s.");
        }
    }

    public static void sendItemDisabledMessage(Player player) {
        player.sendMessage(MagicalTools.Settings.getItemDisabledMessage());
    }

    public static final String resetformat = "§r§b";
}
