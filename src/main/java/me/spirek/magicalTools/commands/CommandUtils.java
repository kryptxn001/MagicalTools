package me.spirek.magicalTools.commands;

import me.spirek.magicalTools.MagicalTools;
import org.bukkit.entity.Player;

public class CommandUtils {
    /**
     * Sends a branded message to the player with a "MagicalTools" prefix.
     *
     * @param player The player who will receive the message.
     * @param text   The message content to send.
     */
    public static void sendMessageBranded(Player player, String text) {
        player.sendMessage("§b§l[MagicalTools] §r§b"+text);
    }

    /**
     * Sends a no-permission message to the player, defined in the plugin settings.
     *
     * @param player The player who attempted an action without the required permission.
     */
    public static void sendNotPermissions(Player player) {
        player.sendMessage(MagicalTools.Settings.getPermissionsText());
    }

    /**
     * Sends a cooldown message to the player, indicating remaining cooldown time.
     *
     * @param player The player who triggered the cooldown message.
     * @param time   The remaining cooldown time in seconds.
     */
    public static void sendCooldownMessage(Player player, Double time) {
        if(MagicalTools.Settings.shouldShowCooldown()) {
            player.sendMessage(MagicalTools.Settings.getCooldownMessage().replace("{time}",time.toString()));
            //player.sendMessage("§c§l[MagicalTools] §r§cYou can't use it! Cooldown remaining: "+time+"s.");
        }
    }

    /**
     * Sends a message to the player indicating that the item they tried to use is disabled.
     *
     * @param player The player who attempted to use a disabled item.
     */
    public static void sendItemDisabledMessage(Player player) {
        player.sendMessage(MagicalTools.Settings.getItemDisabledMessage());
    }

    public static final String resetformat = "§r§b";
}
