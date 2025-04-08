package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import me.spirek.magicalTools.tools.ToolManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Represents a tool that captures mobs and turns them into spawn eggs.
 */
public class MobCatcher extends Tool {
    private final static String TOOLID = "mobcatcher";
    public static HashMap<Snowball, Player> mobcatchers = new HashMap<>();
    /**
     * Constructs a MobCatcher with default attributes.
     */
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
                0
        );
        addCustomAttribute("catch_message","§6You've just captured a §l{caught_entity}.");
        addCustomAttribute("can_catch_tamed", false);
    }

    /**
     * Handles player interaction with the Mob Catcher.
     * Throws a snowball when right-clicked.
     *
     * @param event The event triggered by the interaction.
     */
    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK")) {
            if(!cooldownEnded(event.getPlayer(), true)) {
                CommandUtils.sendCooldownMessage(event.getPlayer(),getRemainingCooldown(event.getPlayer()));
                event.setCancelled(true);
                return;
            }
            ItemStack snowballItem = event.getItem();

            Snowball thrownSnowball = event.getPlayer().launchProjectile(Snowball.class);
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_EGG_THROW, 0.5F,1);
            mobcatchers.put(thrownSnowball, event.getPlayer());
            if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && snowballItem != null) {
                snowballItem.setAmount(snowballItem.getAmount()-1);
            }
            event.setCancelled(true);
        }
    }

    /**
     * Handles the event when the thrown snowball hits an entity or surface.
     * Converts the entity into a spawn egg if valid.
     *
     * @param player The player who threw the snowball.
     * @param event The projectile hit event.
     */
    //When snowball hits something.
    public void onProjectileHit(Player player, ProjectileHitEvent event) {
        Projectile snowball = event.getEntity();
        Entity hitEntity = event.getHitEntity();

        if (hitEntity != null && getSpawnEggMaterial(hitEntity) != null && isValid(hitEntity)) {
            Material spawnegg = getSpawnEggMaterial(hitEntity);

            player.sendMessage(getCustomAttribute("catch_message",String.class).replace("{caught_entity}",hitEntity.getName()));
            player.playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F,1);
            snowball.getWorld().dropItem(event.getEntity().getLocation(),new ItemStack(spawnegg));
            player.spawnParticle(Particle.SONIC_BOOM,event.getEntity().getLocation(),1,0,0,0);
            hitEntity.remove();

        } else {
            player.playSound(event.getEntity().getLocation(), Sound.ENTITY_ITEM_BREAK, 0.5F,1);
            snowball.getWorld().dropItem(event.getEntity().getLocation(), ToolManager.getTool(MobCatcher.class).getItem());
            event.setCancelled(true);
        }
    }

    /**
     * Checks if an entity can be captured by the Mob Catcher.
     *
     * @param entity The entity to check.
     * @return True if the entity can be captured, false otherwise.
     */
    private boolean isValid(Entity entity) {
        if(entity instanceof EnderDragon) {
            return false;
        }
        if(entity instanceof Tameable tameable) {
            if(!getCustomAttribute("can_catch_tamed", Boolean.class)) {
                return !tameable.isTamed();
            }
        }
        return true;
    }

    /**
     * Gets the corresponding spawn egg material for a given entity.
     *
     * @param entity The entity to check.
     * @return The spawn egg material or null if unavailable.
     */
    private Material getSpawnEggMaterial(Entity entity) {
        EntityType entityType = entity.getType();

        if (entityType.isSpawnable()) {
            return Material.getMaterial(entityType.name() + "_SPAWN_EGG");
        }
        return null;
    }
}
