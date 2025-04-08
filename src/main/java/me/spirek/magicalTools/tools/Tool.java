package me.spirek.magicalTools.tools;

import me.spirek.magicalTools.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
/**
 * Represents an abstract tool.
 * Manages attributes, cooldowns, and interactions.
 */
public abstract class Tool {
    private final String name;
    private String label;
    private String[] lore;
    private double meleedamage;
    private long cooldown;
    public String[] getLore() {
        return lore;
    }
    public double getMeleedamage() {
        return meleedamage;
    }
    public boolean isUnbreakable() {
        return unbreakable;
    }
    public boolean isShine() {
        return shine;
    }
    public boolean isCustom_model() {
        return custom_model;
    }
    public Material getMaterial() {
        return material;
    }
    private boolean unbreakable, shine, custom_model;
    private Material material;

    private boolean disabled;
    protected HashMap<Player, Long> cooldowns = new HashMap<>();
    protected HashMap<String, Object> custom_attributes = new HashMap<>();

    public String getName() {
        return name;
    }

    /**
     * Creates a new tool with specified properties.
     *
     * @param name        The internal tool ID.
     * @param label       The display name of the tool.
     * @param lore        The description of the tool.
     * @param material    The material used for the tool.
     * @param unbreakable Whether the tool is unbreakable.
     * @param shine       Whether the tool has a glowing effect.
     * @param custom_model Whether the tool uses a custom model.
     * @param meleedamage The melee damage of the tool.
     * @param cooldown    The cooldown time in milliseconds.
     */
    public Tool(String name, String label, String[] lore, Material material, boolean unbreakable, boolean shine, boolean custom_model, double meleedamage, long cooldown) {
        this.name = name;
        this.label = label;
        this.lore = lore;
        this.material = material;
        this.unbreakable = unbreakable;
        this.shine = shine;
        this.custom_model = custom_model;
        this.meleedamage = meleedamage;
        this.cooldown = cooldown;

        loadConfigValues();
    }

    /**
     * Loads configuration values from the plugin's config file.
     * Updates tool properties based on the config.
     */
    private void loadConfigValues() {
        FileConfiguration config = ConfigManager.getConfig();

        String path = "tools."+name;
        this.disabled = config.getBoolean(path + ".disabled",false);

        if (config.contains(path)) {
            String label = config.getString(path + ".label");
            if (label != null && !label.isEmpty()) {
                this.label = label;
            }

            List<String> lore = config.getStringList(path + ".lore");
            if (config.isSet(path + ".lore")) {
                if(!lore.isEmpty()) {
                    this.lore = lore.toArray(new String[0]);
                } else {
                    Bukkit.getConsoleSender().sendMessage("§c[MagicalTools] "+name+": Lore specified isn't a list! Use \"- \" before every line.");
                }
            }

            String materialStr = config.getString(path + ".material");
            if (materialStr != null && !materialStr.isEmpty()) {
                try {
                    this.material = Material.valueOf(materialStr);
                } catch (IllegalArgumentException exception) {
                    Bukkit.getConsoleSender().sendMessage("§c[MagicalTools] "+name+": Material specified doesn't exist!");
                }
            }

            String unbreakableStr = config.getString(path + ".unbreakable");
            if (unbreakableStr != null && !unbreakableStr.isEmpty()) {
                this.unbreakable = Boolean.parseBoolean(unbreakableStr);
            }

            String shineStr = config.getString(path + ".shine");
            if (shineStr != null && !shineStr.isEmpty()) {
                this.shine = Boolean.parseBoolean(shineStr);
            }

            String customModelStr = config.getString(path + ".custom_model");
            if (customModelStr != null && !customModelStr.isEmpty()) {
                this.custom_model = Boolean.parseBoolean(customModelStr);
            }

            String meleeDamageStr = config.getString(path + ".meleedamage");
            if (meleeDamageStr != null && !meleeDamageStr.isEmpty()) {
                try {
                    this.meleedamage = Double.parseDouble(meleeDamageStr);
                } catch (Exception exception) {
                    Bukkit.getConsoleSender().sendMessage("§c[MagicalTools] "+name+": Melee Damage specified isn't a valid number!");
                }
            }

            String cooldownStr = config.getString(path + ".cooldown");
            if (cooldownStr != null && !cooldownStr.isEmpty()) {
                try {
                    this.cooldown = Long.parseLong(cooldownStr);
                } catch (Exception exception) {
                    Bukkit.getConsoleSender().sendMessage("§c[MagicalTools] "+name+": Cooldown specified isn't a valid number!");
                }
            }
        }
    }

    /**
     * Adds a custom attribute to the tool.
     *
     * @param name     The attribute name.
     * @param defvalue The default value of the attribute.
     */
    protected void addCustomAttribute(String name, Object defvalue) {
        FileConfiguration config = ConfigManager.getConfig();
        String path = "tools."+this.name+".";

        if(defvalue instanceof Double) {
            custom_attributes.put(name,config.getDouble(path+name,(double) defvalue));
        } else if (defvalue instanceof Integer) {
            custom_attributes.put(name,config.getInt(path+name, (int) defvalue));
        } else if (defvalue instanceof Boolean) {
            custom_attributes.put(name,config.getBoolean(path+name, (Boolean) defvalue));
        } else if (defvalue instanceof String) {
            custom_attributes.put(name,config.getString(path+name, (String) defvalue));
        } else {
            custom_attributes.put(name,defvalue);
        }

    }

    /**
     * Retrieves a custom attribute value.
     *
     * @param key  The attribute key.
     * @param type The expected type of the value.
     * @return The attribute value.
     * @throws IllegalArgumentException If the attribute is missing or has a type mismatch.
     */
    public <T> T getCustomAttribute(String key, Class<T> type) {
        Object value = custom_attributes.get(key);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Type mismatch: original: " + type.getName() + " but got: " + value.getClass().getName());
            }
        } else {
            throw new IllegalArgumentException("Attribute not found.");
        }
    }

    /**
     * Triggers when the tool is used to attack an entity.
     *
     * @param event The event triggered by the attack.
     */
    public void onAttack(EntityDamageByEntityEvent event) { //kdyz predmet uderi
    }

    /**
     * Triggers when the player interacts with an object using the tool.
     *
     * @param event The event triggered by the interaction.
     */
    public void onInteract(PlayerInteractEvent event) { //kdyz predmet uderi
    }

    /**
     * Triggers when the player breaks a block using the tool.
     *
     * @param event The event triggered by the block break.
     */
    public void onBlockBreak(BlockBreakEvent event) { }
    /**
     * Retrieves the tool's disabled boolean.
     *
     * @return The tool's disabled.
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Retrieves the tool's label (display name).
     *
     * @return The tool's label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Retrieves the item representation of the tool.
     *
     * @return The ItemStack representing the tool.
     */
    public ItemStack getItem() {
        return ToolManager.createItem(this);
    }

    /**
     * Checks if the tool's cooldown has ended for a player.
     *
     * @param player    The player using the tool.
     * @param autoReset Whether to reset the cooldown if it has ended.
     * @return True if the cooldown has ended, otherwise false.
     */
    public boolean cooldownEnded(Player player, boolean autoReset) {
        long lastUsed = cooldowns.getOrDefault(player, 0L);
        long currentTime = System.currentTimeMillis();

        if(player.getGameMode().equals(GameMode.CREATIVE)) {
            return true;
        }

        if (currentTime-lastUsed > cooldown) {
            if(autoReset) {
                cooldowns.put(player, System.currentTimeMillis());
            }
            return true;
        }
        return false;
    }

    /**
     * Retrieves the remaining cooldown time.
     *
     * @return The remaining cooldown in seconds.
     */
    public double getRemainingCooldown(Player player) {
        long lastUsed = cooldowns.getOrDefault(player, 0L);
        long currentTime = System.currentTimeMillis();
        long cooldownRemaining = cooldown - (currentTime-lastUsed);

        return Math.round((cooldownRemaining / 1000.0) * 10) / 10.0;
    }

    /**
     * Resets the cooldown for a player.
     *
     * @param player The player whose cooldown should be reset.
     */
    public void resetCooldown(Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }
}
