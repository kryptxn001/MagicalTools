package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.tools.Tool;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Raygun extends Tool {
    private final static String TOOLID = "raygun";
    public Raygun() {
        super(
                TOOLID,
                "§2§lRaygun",
                new String[] {
                        "§8Shoots things with powerful gamma rays.",
                        "§8Can't penetrate any solid blocks."
                },
                Material.ECHO_SHARD,
                true,
                true,
                false,
                1,
                500
        );

        addCustomAttribute("laserdamage",10D);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        //Zkontrolovat tlačátko zmáčknuté
        if (!event.getAction().name().contains("RIGHT_CLICK")) {
            return;
        }

        Player player = event.getPlayer();

        if(!cooldownEnded(player, true)) {
            return;
        }

        player.playSound(player, Sound.ENTITY_ARMADILLO_BRUSH,2,1);

        //Získat normalizovaný směrový vector (kam hráč kouká)
        Vector dirvec = player.getLocation().getDirection().normalize();
        Location eyeloc = player.getLocation().add(0,player.getEyeHeight(),0);

        Location offsetloc = eyeloc.add(dirvec.multiply(2));

        double blockdistance = 100;

        RayTraceResult rayTraceBlockResult = player.rayTraceBlocks(32,FluidCollisionMode.NEVER);
        if(rayTraceBlockResult != null && rayTraceBlockResult.getHitBlock() != null) {
            Block hitblock = rayTraceBlockResult.getHitBlock();
            blockdistance = eyeloc.distance(hitblock.getLocation());
        }

        RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(offsetloc,dirvec,32);
        if(rayTraceResult != null && rayTraceResult.getHitEntity() != null) {
            //Pokud raycast něco vrátil.
            double entitydistance = eyeloc.distance(rayTraceResult.getHitEntity().getLocation());
            if(blockdistance < entitydistance) {
                if(!rayTraceBlockResult.getHitBlock().isPassable()) {
                    //Pokud byl block blíž než entita (znamená že blok byl před entitou a tudiž nemůže být zasažena)
                    drawRay(blockdistance-blockdistance*0.5, player);
                    Impact(player, rayTraceBlockResult.getHitPosition());
                    return;
                }
            }
            if(rayTraceResult.getHitEntity() instanceof LivingEntity entity) {
                //Zásah entity

                entity.damage(getCustomAttribute("laserdamage",double.class),player);
                player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_HIT,2,1);
            }
            //Jen effekt zasažení
            drawRay(entitydistance*0.5, player);
            Impact(player, rayTraceResult.getHitPosition());
            return;
        }

        drawRay(Math.clamp(blockdistance-blockdistance*0.5,0,32), player);
        if(blockdistance != 100) {
            //Jen effekt zasažení
            Impact(player, rayTraceBlockResult.getHitPosition());
        }
    }

    private void drawRay(double distance, Player player) {
        double increment = 0.5;
        double max = distance/increment;
        for(double i = 0; i <= max; i += increment) {
            Vector dirvec = player.getLocation().getDirection().normalize();
            Location location = player.getLocation().add(0,player.getEyeHeight(),0).add(dirvec.multiply(i));
            player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, location,1,0,0,0);
        }
    }

    private void Impact(Player player, Vector pos) {
        player.getWorld().spawnParticle(Particle.GLOW_SQUID_INK, pos.toLocation(player.getWorld()),50,1,1,1);
        //player.getWorld().playSound(pos.toLocation(player.getWorld()), Sound.ENTITY_ALLAY_HURT,0.5f,1f);
        player.getWorld().playSound(pos.toLocation(player.getWorld()), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR,0.5f,1f);
    }
}
