package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;


public class MobCatcher extends Tool {
    private final static String TOOLID = "mobcatcher";
    public static HashMap<Snowball, Player> mobcatchers = new HashMap<>();
    public MobCatcher() {
        super(
                TOOLID,
                "§f§lMob Catcher",
                new String[] {
                        "§8Turns mob into a spawn egg."
                },
                Material.SNOWBALL,
                true,
                true,
                false,
                0,
                0,
                0
        );
    }


    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK")) {
            ItemStack snowballItem = event.getItem();

            Snowball thrownSnowball = event.getPlayer().launchProjectile(Snowball.class);
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_EGG_THROW, 0.5F,1);
            mobcatchers.put(thrownSnowball, event.getPlayer());
            if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                snowballItem.setAmount(snowballItem.getAmount()-1);
            }
            event.setCancelled(true);
        }
    }

    //When snowball hits something.
    public void onProjectileHit(Player player, ProjectileHitEvent event) {
        Projectile snowball = event.getEntity();
        Entity hitEntity = event.getHitEntity();

        if (hitEntity != null && getSpawnEggMaterial(hitEntity) != null && isCorrect(hitEntity)) {
            Material spawnegg = getSpawnEggMaterial(hitEntity);

            player.sendMessage("§6You've just captured a §l"+hitEntity.getName());
            player.playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F,1);
            snowball.getWorld().dropItem(event.getEntity().getLocation(),new ItemStack(spawnegg));
            hitEntity.remove();

        } else {
            player.playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_BREAK, 0.5F,1);
            snowball.getWorld().dropItem(event.getEntity().getLocation(), ToolManager.getTool(MobCatcher.class).getItem());
            event.setCancelled(true);
        }
    }

    private boolean isCorrect(Entity entity) {
        if(entity instanceof EnderDragon) {
            return false;
        }
        if(entity instanceof Tameable tameable) {
            return !tameable.isTamed();
        }
        return true;
    }

    private Material getSpawnEggMaterial(Entity entity) {
        EntityType entityType = entity.getType();

        if (entityType.isSpawnable()) {
            return Material.getMaterial(entityType.name() + "_SPAWN_EGG");
        }

        return null;
    }
}
