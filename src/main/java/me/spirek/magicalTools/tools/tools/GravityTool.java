package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.tools.Tool;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class GravityTool extends Tool {
    private final static String TOOLID = "gravitytool";
    public GravityTool() {
        super(
                TOOLID,
                "§b§lGravity§6§lPipe",
                new String[] {
                        "§8A pipe that transfers kinetic energy.",
                        "§80 attack damage!",
                        "§81 second cooldown."
                },
                Material.BLAZE_ROD,
                true,
                true,
                false,
                0,
                1000
        );

        addCustomAttribute("gravity_multiplier",10D);
    }

    public void onAttack(EntityDamageByEntityEvent event){
        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();

        event.setCancelled(true);
        if(cooldownEnded(player, true)) {
            double multiplier = getCustomAttribute("gravity_multiplier", double.class);
            entity.setVelocity(new Vector(player.getVelocity().getX()*multiplier, player.getVelocity().getY()*-1, player.getVelocity().getZ()*multiplier));
            player.setVelocity(new Vector(0,0,0));
            if(getMeleedamage()>0 && entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(getMeleedamage(),player);
            }
            player.playSound(entity.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1f, 1f);
            Random random = new Random();
            int chance = random.nextInt(100);
            if(chance < 50) {
                player.spawnParticle(Particle.SCRAPE,entity.getLocation().add(0,1,0),20, 1,1,1);
            } else {
                player.spawnParticle(Particle.WAX_ON,entity.getLocation().add(0,1,0),20,1,1,1);
            }
        }
    }

}
