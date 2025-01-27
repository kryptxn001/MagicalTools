package me.spirek.magicalTools.events;


import me.spirek.magicalTools.tools.ToolManager;
import me.spirek.magicalTools.tools.tools.MobCatcher;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class onProjectileHitEvent implements Listener {


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball snowball) {
            if(MobCatcher.mobcatchers.containsKey(snowball)) {
                MobCatcher mobCatcher = (MobCatcher) ToolManager.getTool(MobCatcher.class);
                mobCatcher.onProjectileHit(MobCatcher.mobcatchers.get(snowball), event);
                MobCatcher.mobcatchers.remove(snowball);
            }
        }
    }
}
