package me.spirek.magicalTools.events;

import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class onBlockBreakEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        Tool tool = ToolManager.getToolbyItemStack(item);
        if(tool != null) {
            tool.onBlockBreak(event);
        }
    }
}