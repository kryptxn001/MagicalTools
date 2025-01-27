package me.spirek.magicalTools.tools;

import me.spirek.magicalTools.MagicalTools;
import me.spirek.magicalTools.UpdateManager;
import me.spirek.magicalTools.tools.tools.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.spi.ToolProvider;

public class ToolManager {

    public static ArrayList<Tool> tools = new ArrayList<>();
    private static final NamespacedKey ITEM_UID_KEY = new NamespacedKey("magicaltools", "tool_id");
    private static final NamespacedKey ITEM_UPDATE_KEY = new NamespacedKey("magicaltools", "update_id");
    public static void loadTools() {
        tools.add(new LightningSword());
        tools.add(new RandomSword());
        tools.add(new MobCatcher());
        tools.add(new MagicalPickaxe());
        tools.add(new Raygun());
        tools.add(new GravityTool());
    }

    public static Tool getToolbyID(String name) {
        for (Tool tool : tools) {
            if(Objects.equals(tool.getName(), name)) {
                return tool;
            }
        }
        return null;
    }

    public static Tool getTool(Class<? extends Tool> toolclass) {
        for(Tool tool : tools) {
            if(tool.getClass().equals(toolclass)) {
                return tool;
            }
        }
        return null;
    }


    public static Tool getToolbyItemStack(ItemStack itemStack) {
        String toolid = getToolID(itemStack);

        for(Tool tool : tools) {
            if(Objects.equals(toolid, tool.getName())) {
                return tool;
            }
        }
        return null;
    }

    public static ItemStack createItem(Tool tool) {
        // Create the ItemStack
        ItemStack item = new ItemStack(tool.getMaterial());

        // Get the ItemMeta
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Set the display name

            meta.setDisplayName(tool.getLabel());
            meta.setLore(Arrays.asList(tool.getLore()));

            if(tool.isUnbreakable()) {
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }


            NamespacedKey attackDamageKey = new NamespacedKey(MagicalTools.NAMESPACE, "meleedamage");
            AttributeModifier damageModifier = new AttributeModifier(
                    attackDamageKey,
                    tool.getMeleedamage(),
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );
            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier);

            //Shine effect
            if(tool.isShine()) {
                meta.addEnchant(Enchantment.FROST_WALKER, 1, true);
            }

            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

            // nastavuje id pro rozpoznani
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(MagicalTools.NAMESPACE, "tool_id");
            NamespacedKey updateKey = new NamespacedKey(MagicalTools.NAMESPACE,"update_id");
            container.set(key, PersistentDataType.STRING, tool.getName());
            container.set(updateKey, PersistentDataType.INTEGER, UpdateManager.update_id);

            //meta.setCustomModelData(custom_model_data);
            if(tool.isCustom_model()) {
                meta.setItemModel(new NamespacedKey(MagicalTools.NAMESPACE,tool.getName()));
            }



            item.setItemMeta(meta);
        }

        return item;
    }

    public static String getToolID(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            return container.get(ITEM_UID_KEY, PersistentDataType.STRING);
        }

        return null;
    }

    public static int getToolUpdateID(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return -1;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            try {
                return container.get(ITEM_UPDATE_KEY, PersistentDataType.INTEGER);
            } catch (NullPointerException e) {
                return -1;
            }


        }

        return -1;
    }



}
