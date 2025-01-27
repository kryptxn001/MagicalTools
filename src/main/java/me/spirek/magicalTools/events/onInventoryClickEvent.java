package me.spirek.magicalTools.events;

import me.spirek.magicalTools.UpdateManager;
import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class onInventoryClickEvent implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        ItemStack item = event.getCurrentItem();

        Tool tool = ToolManager.getToolbyItemStack(event.getCurrentItem());

        if (tool != null && item.getItemMeta() != null) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

            if(container.get(UpdateManager.ITEM_UPDATE_KEY, PersistentDataType.INTEGER)!=UpdateManager.update_id) {
                int amount = item.getAmount();
                ItemStack newTool = tool.getItem();
                newTool.setAmount(amount);
                event.setCurrentItem(newTool);
                CommandUtils.sendMessageBranded((Player) event.getWhoClicked(), "Item you interacted with was outdated! Item updated now!");
            }
        }





    }
}
