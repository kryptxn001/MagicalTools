package me.spirek.magicalTools.events;

import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class onEntityDamageByEntityEvent implements Listener {
    /**
     * Handles entity damage by players, allowing magical tools to apply their effects.
     *
     * @param event The EntityDamageByEntityEvent triggered when an entity is damaged by another.
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();

            Tool tool = ToolManager.getToolbyItemStack(item);
            if(tool != null) {
                if(!tool.isDisabled() || player.hasPermission("magicaltools.ignoredisabled")) {
                    tool.onAttack(event);
                } else {
                    CommandUtils.sendItemDisabledMessage(player);
                    event.setCancelled(true);
                }
            }
        }
    }
}
