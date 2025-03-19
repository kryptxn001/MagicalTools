package me.spirek.magicalTools.events;

import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onBlockPlaceEvent implements Listener {
    /**
     * Prevents players from placing magical tools as blocks.
     *
     * @param event The BlockPlaceEvent triggered when a player places a block.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Tool toolinhand = ToolManager.getToolbyItemStack(event.getItemInHand());
        if(toolinhand != null) {
            event.setCancelled(true);
        }
    }
}
