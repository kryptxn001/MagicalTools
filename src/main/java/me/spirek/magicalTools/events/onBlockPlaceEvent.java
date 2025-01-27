package me.spirek.magicalTools.events;

import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(ToolManager.getToolbyItemStack(event.getItemInHand()) != null) {
            event.setCancelled(true);
        }
    }
}
