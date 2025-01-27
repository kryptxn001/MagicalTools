package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.tools.Tool;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class LightningSword extends Tool {
    private final static String TOOLID = "lightningsword";
    public LightningSword() {
        super(
                TOOLID,
                "§b§lLightning Sword",
                new String[]{
                        "§850% to strike your enemy with lightning!",
                        "§82 second cooldown on the effect.",
                        "§8Use it carefully."
                },
                Material.DIAMOND_SWORD,
                true,
                true,
                false,
                7,
                0,
                2000
        );
    }

    public void onAttack(EntityDamageByEntityEvent event){
        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();

        Random random = new Random();
        if(cooldownEnded(player, true)) {
            int chance = random.nextInt(100);
            if(chance <= 50) {
                World world = entity.getWorld();
                Location location = entity.getLocation();
                world.spawn(location, LightningStrike.class);
            } else {
                player.playSound(entity.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.2f, 1f);
                player.spawnParticle(Particle.GLOW,entity.getLocation().add(0,1,0),20,1,1,1);
            }
        }
    }
}
