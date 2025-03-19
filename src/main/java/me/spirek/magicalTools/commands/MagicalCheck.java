package me.spirek.magicalTools.commands;

import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MagicalCheck implements CommandExecutor {
    /**
     * Handles the "/magicalcheck" command, checking if the item in the player's main hand is a magical tool.
     *
     * @param sender  The entity (player or console) that executed the command.
     * @param command The command that was executed.
     * @param label   The alias of the command used.
     * @param args    Additional arguments provided with the command.
     * @return        True if the command was handled successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(Objects.equals(label, "magicalcheck")) {
            Player player = (Player) sender;
            if(!sender.hasPermission("magicaltools.check")) {
                CommandUtils.sendNotPermissions(player);
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            String uid = ToolManager.getToolID(item);
            if (uid != null) {
                CommandUtils.sendMessageBranded(player, "This item is magical tool. Tool ID: "+uid + ", update id: "+ToolManager.getToolUpdateID(item));
            } else {
                CommandUtils.sendMessageBranded(player, "This item isn't magical tool.");
            }
        }
        return true;
    }
}

