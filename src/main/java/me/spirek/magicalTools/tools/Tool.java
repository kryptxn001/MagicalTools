package me.spirek.magicalTools.tools;

import me.spirek.magicalTools.ConfigManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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

    public long getCooldown() {
        return cooldown;
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
    protected HashMap<Player, Long> cooldowns = new HashMap<>();

    protected HashMap<String, Object> custom_attributes = new HashMap<>();

    public String getName() {
        return name;
    }

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

    private void loadConfigValues() {
        FileConfiguration config = ConfigManager.getConfig();

        String path = "tools."+name;
            /*
        if (config.contains(path) && ConfigManager.configNotEmpty(config,path)) {

            String label = config.getString(path+".label");
            List<String> lore = config.getStringList(path+".lore");
            Material material = Material.valueOf(config.getString(path+".material"));
            boolean unbreakable = config.getBoolean(path+".unbreakable");
            boolean shine = config.getBoolean(path+".shine");
            boolean custom_model = config.getBoolean(path+".custom_model");
            double meleedamage = config.getDouble(path+".meleedamage");
            long cooldown = config.getLong(path+".cooldown");

            this.label = label;
            this.lore = lore.toArray(new String[0]);
            this.material = material;
            this.unbreakable = unbreakable;
            this.shine = shine;
            this.custom_model = custom_model;
            this.meleedamage = meleedamage;
            this.cooldown = cooldown;



        }**/
        if (config.contains(path)) {
            String label = config.getString(path + ".label");
            if (label != null && !label.isEmpty()) {
                this.label = label;
            }

            List<String> lore = config.getStringList(path + ".lore");
            if (!lore.isEmpty()) {
                this.lore = lore.toArray(new String[0]);
            }

            String materialStr = config.getString(path + ".material");
            if (materialStr != null && !materialStr.isEmpty()) {
                this.material = Material.valueOf(materialStr);
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
                this.meleedamage = Double.parseDouble(meleeDamageStr);
            }

            String cooldownStr = config.getString(path + ".cooldown");
            if (cooldownStr != null && !cooldownStr.isEmpty()) {
                this.cooldown = Long.parseLong(cooldownStr);
            }
        }
    }
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


    public void onAttack(EntityDamageByEntityEvent event) { //kdyz predmet uderi

    }

    public void onInteract(PlayerInteractEvent event) { //kdyz predmet uderi

    }

    public String getLabel() {
        return label;
    }

    public void onBlockBreak(BlockBreakEvent event) {

    }

    public ItemStack getItem() {
        return ToolManager.createItem(this);
    }

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

    public double getCooldownInSeconds() {
        return 0;
    }

    public void resetCooldown(Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }
}
