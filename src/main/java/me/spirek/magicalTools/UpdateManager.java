package me.spirek.magicalTools;

import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UpdateManager {
    public static int update_id;
    public static final NamespacedKey ITEM_UPDATE_KEY = new NamespacedKey("magicaltools", "update_id");

    /**
     * Trigger when the magicalupdate is run.
     * Sets a different update ID by increasing the old one.
     */
    public static void newUpdate() {
        ConfigManager.getConfig().set("update_id", update_id + 1);
        ConfigManager.saveConfig();
        update_id = (byte) (update_id + 1);
    }

}
