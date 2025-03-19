package me.spirek.magicalTools.events;

import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class onBlockBreakEvent implements Listener {
    /**
     * Handles the BlockBreakEvent to check if the player is using a magical tool.
     * If the tool is found and not disabled (or the player has permission to bypass this restriction),
     * the tool's specific block break behavior is executed.
     *
     * @param event The BlockBreakEvent triggered when a player breaks a block.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        Tool tool = ToolManager.getToolbyItemStack(item);
        if(tool != null) {
            if(!tool.isDisabled() || event.getPlayer().hasPermission("magicaltools.ignoredisabled")) {
                tool.onBlockBreak(event);
            } else {
                CommandUtils.sendItemDisabledMessage(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }
}