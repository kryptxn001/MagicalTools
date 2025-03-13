package me.spirek.magicalTools.events;

import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlaceEvent implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Tool toolinhand = ToolManager.getToolbyItemStack(event.getItemInHand());
        if(toolinhand != null) {
            event.setCancelled(true);
        }
    }
}
