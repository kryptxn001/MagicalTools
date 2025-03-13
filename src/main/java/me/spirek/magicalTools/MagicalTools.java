package me.spirek.magicalTools;


import me.spirek.magicalTools.commands.MagicalCheck;
import me.spirek.magicalTools.commands.MagicalGive;
import me.spirek.magicalTools.commands.MagicalHelp;
import me.spirek.magicalTools.commands.MagicalUpdate;
import me.spirek.magicalTools.events.*;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MagicalTools extends JavaPlugin {
    public static final String NAMESPACE = "magicaltools";
    public static final String VERSION = "1.0";
    public static final String WEBSITE = "https://github.com/kryptxn001";

    @Override
    public void onEnable() {
        ConfigManager.initialize(this);
        Settings.showCooldown = ConfigManager.getConfig().getBoolean("settings.showCooldownTimeRemaining", true);
        Settings.notpermissions = ConfigManager.getConfig().getString("settings.NoPermissionsMessage", "§c§l[MagicalTools] §r§cYou don't have permissions to execute this command!");
        Settings.cooldownmessage = ConfigManager.getConfig().getString("settings.CooldownRemainingMessage", "§c§l[MagicalTools] §r§cYou can't use it! Cooldown remaining: {time}.");
        Settings.disabledmessage = ConfigManager.getConfig().getString("settings.ToolDisabledMessage", "§c§l[MagicalTools] §r§cThis Magical Tool is disabled. You can't use it!");

        //Registrace vlastních příkazů
        getCommand("magicalgive").setExecutor(new MagicalGive());
        getCommand("magicalcheck").setExecutor(new MagicalCheck());
        getCommand("magicalupdate").setExecutor(new MagicalUpdate());
        getCommand("magicalhelp").setExecutor(new MagicalHelp());

        //Registrace eventů
        getServer().getPluginManager().registerEvents(new onEntityDamageByEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new onBlockBreakEvent(), this);
        getServer().getPluginManager().registerEvents(new onEntityPickupItemEvent(), this);
        getServer().getPluginManager().registerEvents(new onInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new onBlockPlaceEvent(), this);
        getServer().getPluginManager().registerEvents(new onCraftingEvent(), this);
        getServer().getPluginManager().registerEvents(new onProjectileHitEvent(), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteractEvent(), this);

        ToolManager.loadTools();
        Bukkit.getConsoleSender().sendMessage("§3Magical Tools is initialized! Made by Kryštof Špírek");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("§3Magical Tools is shutting down!");
    }

    public static class Settings {
        protected static boolean showCooldown;
        public static boolean shouldShowCooldown() {
            return showCooldown;
        }
        protected static String notpermissions;
        public static String getPermissionsText() {
            return notpermissions;
        }
        protected static String cooldownmessage;
        public static String getCooldownMessage() {
            return cooldownmessage;
        }
        protected static String disabledmessage;
        public static String getItemDisabledMessage() {
            return disabledmessage;
        }
    }

}