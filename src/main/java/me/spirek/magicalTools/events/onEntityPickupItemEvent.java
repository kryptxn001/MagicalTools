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

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {  // Ensure the entity is a player
            // Check if item needs to be updated based on the update_id

            Tool tool = ToolManager.getToolbyItemStack(event.getItem().getItemStack());


            if (tool != null) {
                PersistentDataContainer container = event.getItem().getItemStack().getItemMeta().getPersistentDataContainer();

                if(container.get(UpdateManager.ITEM_UPDATE_KEY, PersistentDataType.INTEGER)!=UpdateManager.update_id) {
                    int amount = event.getItem().getItemStack().getAmount();
                    ItemStack newItem = tool.getItem();
                    newItem.setAmount(amount);

                    event.getItem().setItemStack(newItem);
                    if(event.getEntity() instanceof Player) {
                        CommandUtils.sendMessageBranded((Player) event.getEntity(), "Item you interacted with was outdated! Item updated now!");
                    }

                }
            }
        }
    }
}
