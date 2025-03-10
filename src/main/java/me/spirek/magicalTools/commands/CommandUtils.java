package me.spirek.magicalTools.commands;

import org.bukkit.entity.Player;

public class CommandUtils {
    public static void sendMessageBranded(Player player, String text) {
        player.sendMessage("§b§l[MagicalTools] §r§b"+text);
    }
    public static final String resetformat = "§r§b";
}
