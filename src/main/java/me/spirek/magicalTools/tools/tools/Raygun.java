package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/**
 * Represents a tool that fires a laser beam.
 */
public class Raygun extends Tool {
    private final static String TOOLID = "raygun";
    /**
     * Constructs a Raygun with default attributes.
     */
    public Raygun() {
        super(
                TOOLID,
                "§2§lRaygun",
                new String[] {
                        "§8Shoots laser with powerful gamma rays.",
                        "§8Can't penetrate any solid blocks."
                },
                Material.ECHO_SHARD,
                true,
                true,
                false,
                1,
                2000
        );

        addCustomAttribute("laserdamage",10D);
    }

    /**
     * Handles the interaction event when the player uses the Raygun.
     * Shoots a laser beam in the direction the player is facing.
     *
     * @param event The event triggered by the interaction.
     */
    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().name().contains("RIGHT_CLICK")) {
            return;
        }

        Player player = event.getPlayer();

        if(!cooldownEnded(player, true)) {
            CommandUtils.sendCooldownMessage(player,getRemainingCooldown(player));
            return;
        }

        player.playSound(player, Sound.ENTITY_ARMADILLO_BRUSH,2,1);

        //Normalized directional vector of where the player is looking.
        Vector dirvec = player.getLocation().getDirection().normalize();
        Location eyeloc = player.getLocation().add(0,player.getEyeHeight(),0);
        Location offsetloc = eyeloc.add(dirvec.multiply(2));

        double blockdistance = 100;
        Block hitblock = null;

        //Getting the raytrace block result
        RayTraceResult rayTraceBlockResult = player.rayTraceBlocks(32,FluidCollisionMode.NEVER);
        if(rayTraceBlockResult != null && rayTraceBlockResult.getHitBlock() != null) {
            hitblock = rayTraceBlockResult.getHitBlock();
            blockdistance = eyeloc.distance(hitblock.getLocation());
        }

        //Getting the raytrace entity result
        RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(offsetloc,dirvec,32);
        if(rayTraceResult != null && rayTraceResult.getHitEntity() != null) {
            //If there is an entity.
            double entitydistance = eyeloc.distance(rayTraceResult.getHitEntity().getLocation());
            //If the block raytrace result isn't empty and the distance to the block is closer.
            if(blockdistance < entitydistance && hitblock != null) {
                if(!hitblock.isPassable()) {
                    //If the block isn't passable, make the impact effect.
                    drawRay(blockdistance-blockdistance*0.5, player);
                    Impact(player, rayTraceBlockResult.getHitPosition());
                    return;
                }
            }
            if(rayTraceResult.getHitEntity() instanceof LivingEntity entity) {
                //Damage the entity.
                entity.damage(getCustomAttribute("laserdamage",double.class),player);
                player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_HIT,2,1);
            }
            //Impact effect on the block/entity position and draw ray.
            drawRay(entitydistance*0.5, player);
            Impact(player, rayTraceResult.getHitPosition());
            return;
        }

        drawRay(Math.clamp(blockdistance-blockdistance*0.5,0,32), player);
        if(blockdistance != 100) {
            Impact(player, rayTraceBlockResult.getHitPosition());
        }
    }

    /**
     * Draws a visual laser effect from the player's location.
     *
     * @param distance The distance of the laser.
     * @param player The player using the Raygun.
     */
    private void drawRay(double distance, Player player) {
        double increment = 0.5;
        double max = distance/increment;
        for(double i = 0; i <= max; i += increment) {
            Vector dirvec = player.getLocation().getDirection().normalize();
            Location location = player.getLocation().add(0,player.getEyeHeight(),0).add(dirvec.multiply(i));
            player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location,1,0,0,0);
        }
    }

    /**
     * Creates an impact effect at the laser's endpoint.
     *
     * @param player The player using the Raygun.
     * @param pos The position where the laser hits.
     */
    private void Impact(Player player, Vector pos) {
        player.getWorld().spawnParticle(Particle.GLOW_SQUID_INK, pos.toLocation(player.getWorld()),50,1,1,1);
        player.getWorld().playSound(pos.toLocation(player.getWorld()), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR,0.5f,1f);
    }
}
