package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.tools.Tool;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.Random;

public class RandomSword extends Tool {
    private final static String TOOLID = "randomsword";
    public RandomSword() {
        super(
                TOOLID,
                "§f§k§lkkk §r§4§lGambler's Axe §f§k§lkkk",
                new String[] {
                        "§8Very heavy axe with a strong hit.",
                        "§825% chance to deal damage.",
                        "§81 second cooldown."
                },
                Material.NETHERITE_AXE,
                true,
                true,
                false,
                30,
                1000
        );
        addCustomAttribute("chance",25);
        addCustomAttribute("success_message","§6§lBaaam.... Smash!");
        addCustomAttribute("loose_message", "§7§lWhifff... Missed!");
    }

    public void onAttack(EntityDamageByEntityEvent event){
        event.setCancelled(true);

        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();

        Random random = new Random();
        if(cooldownEnded(player, true)) {
            int chance = random.nextInt(100);
            if(chance < getCustomAttribute("chance",int.class)) {
                event.setCancelled(false);
                player.spawnParticle(Particle.LARGE_SMOKE,entity.getLocation().add(0,1,0),20);
                player.spawnParticle(Particle.SMOKE,entity.getLocation().add(0,1,0),20);
                player.playSound(entity.getLocation(), Sound.ITEM_SHIELD_BREAK, 1f, 1f);
                player.sendMessage(getCustomAttribute("success_message",String.class));
            } else {
                player.sendMessage(getCustomAttribute("loose_message",String.class));
            }
        }
    }
}
