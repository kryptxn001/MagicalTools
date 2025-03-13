package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

/**
 * A special tool that has a chance to strike enemies with lightning upon attack.
 */
public class LightningSword extends Tool {
    private final static String TOOLID = "lightningsword";

    /**
     * Constructs a Lightning Sword with default attributes.
     */
    public LightningSword() {
        super(
                TOOLID,
                "§b§lLightning Sword",
                new String[]{
                        "§850% to strike your enemy with lightning!",
                        "§82 second cooldown on the effect.",
                        "§8Use it carefully."
                },
                Material.IRON_SWORD,
                true,
                true,
                false,
                7,
                2000
        );
        addCustomAttribute("chance",50);
    }

    /**
     * Triggers when the player attacks an entity.
     * Has a chance to summon lightning at the target's location.
     *
     * @param event The event triggered by the attack.
     */
    public void onAttack(EntityDamageByEntityEvent event){
        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();

        Random random = new Random();
        if(cooldownEnded(player, true)) {
            int chance = random.nextInt(100);
            if(chance <= getCustomAttribute("chance",int.class)) {
                World world = entity.getWorld();
                Location location = entity.getLocation();
                world.spawn(location, LightningStrike.class);
            } else {
                player.playSound(entity.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.2f, 1f);
                player.spawnParticle(Particle.GLOW,entity.getLocation().add(0,1,0),20,1,1,1);
            }
        } else {
            CommandUtils.sendCooldownMessage(player,getRemainingCooldown(player));
        }
    }
}
