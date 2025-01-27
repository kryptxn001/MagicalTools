package me.spirek.magicalTools.tools;

import me.spirek.magicalTools.ConfigManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Tool {

    private final String name;
    private String label;
    private String[] lore;
    private double meleedamage;
    private double specialdamage;
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

    public String getName() {
        return name;
    }

    public Tool(String name, String label, String[] lore, Material material, boolean unbreakable, boolean shine, boolean custom_model, double meleedamage, double specialdamage, long cooldown) {
        this.name = name;
        this.label = label;
        this.lore = lore;
        this.material = material;
        this.unbreakable = unbreakable;
        this.shine = shine;
        this.custom_model = custom_model;
        this.meleedamage = meleedamage;
        this.specialdamage = specialdamage;
        this.cooldown = cooldown;

        loadConfigValues();
    }

    private void loadConfigValues() {
        FileConfiguration config = ConfigManager.getConfig();

        String path = "tools."+name;

        if (config.contains(path) && ConfigManager.configNotEmpty(config,path)) {
            String label = config.getString(path+".label");
            List<String> lore = config.getStringList(path+".lore");
            Material material = Material.valueOf(config.getString(path+".material"));
            boolean unbreakable = config.getBoolean(path+".unbreakable");
            boolean shine = config.getBoolean(path+".shine");
            boolean custom_model = config.getBoolean(path+".custom_model");
            double meleedamage = config.getDouble(path+".meleedamage");
            double specialdamage = config.getDouble(path+".specialdamage");
            long cooldown = config.getLong(path+".cooldown");

            this.label = label;
            this.lore = lore.toArray(new String[0]);
            this.material = material;
            this.unbreakable = unbreakable;
            this.shine = shine;
            this.custom_model = custom_model;
            this.meleedamage = meleedamage;
            this.specialdamage = specialdamage;
            this.cooldown = cooldown;
        }
    }

    public double getSpecialdamage() {
        return specialdamage;
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
