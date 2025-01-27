package me.spirek.magicalTools;


import me.spirek.magicalTools.commands.MagicalCheck;
import me.spirek.magicalTools.commands.MagicalGive;
import me.spirek.magicalTools.commands.MagicalUpdate;
import me.spirek.magicalTools.events.*;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MagicalTools extends JavaPlugin {
    public static final String NAMESPACE = "magicaltools";

    @Override
    public void onEnable() {
        ConfigManager.initialize(this);

        //Registrace vlastních příkazů
        getCommand("magicalgive").setExecutor(new MagicalGive());
        getCommand("magicalcheck").setExecutor(new MagicalCheck());
        getCommand("magicalupdate").setExecutor(new MagicalUpdate());

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
        Bukkit.getConsoleSender().sendMessage("§3Magical Tools is initialized!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("§3Magical Tools is shutting down!");
    }

}