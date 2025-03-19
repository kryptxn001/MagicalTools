package me.spirek.magicalTools.events;

import me.spirek.magicalTools.UpdateManager;
import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class onEntityPickupItemEvent implements Listener {
    /**
     * Prevents players from picking up disabled magical tools and updates outdated items.
     *
     * @param event The EntityPickupItemEvent triggered when an entity picks up an item.
     */
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {  // Ensure the entity is a player
            // Check if item needs to be updated based on the update_id
            Tool tool = ToolManager.getToolbyItemStack(event.getItem().getItemStack());

            if (tool != null) {
                if(tool.isDisabled() || !player.hasPermission("magicaltools.ignoredisabled")) {
                    event.setCancelled(true);
                }
                PersistentDataContainer container = event.getItem().getItemStack().getItemMeta().getPersistentDataContainer();

                if(container.get(UpdateManager.ITEM_UPDATE_KEY, PersistentDataType.INTEGER)!=UpdateManager.update_id) {
                    int amount = event.getItem().getItemStack().getAmount();
                    ItemStack newItem = tool.getItem();
                    newItem.setAmount(amount);

                    event.getItem().setItemStack(newItem);
                    CommandUtils.sendMessageBranded(player, "Item you interacted with was outdated! Item updated now!");
                }
            }
        }
    }
}
