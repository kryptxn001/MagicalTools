package me.spirek.magicalTools.events;


import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class onCraftingEvent implements Listener {
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        ItemStack[] matrix = inventory.getMatrix();

        for (ItemStack item : matrix) {
            if (item != null && ToolManager.getToolbyItemStack(item) != null) {
                inventory.setResult(null);
                event.getViewers().forEach(viewer -> {
                    if (viewer instanceof Player player) {
                        CommandUtils.sendMessageBranded(player,"You cannot use "+ToolManager.getToolbyItemStack(item).getLabel()+CommandUtils.resetformat+" for crafting!");
                    }
                });
                return;
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();

        for (ItemStack item : matrix) {
            if (item != null && ToolManager.getToolbyItemStack(item) != null) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                CommandUtils.sendMessageBranded(player,"You cannot use "+ToolManager.getToolbyItemStack(item).getLabel()+CommandUtils.resetformat+" for crafting!");
                return;
            }
        }
    }


}
