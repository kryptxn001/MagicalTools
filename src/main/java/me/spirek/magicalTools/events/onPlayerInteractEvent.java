package me.spirek.magicalTools.events;

import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class onPlayerInteractEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Tool tool = ToolManager.getToolbyItemStack(event.getItem());

        if(tool != null) {
            tool.onInteract(event);
        }
    }
}
