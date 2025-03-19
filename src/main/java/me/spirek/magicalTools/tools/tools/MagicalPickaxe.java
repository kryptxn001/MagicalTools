package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.MagicalTools;
import me.spirek.magicalTools.commands.CommandUtils;
import me.spirek.magicalTools.tools.Tool;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a special pickaxe that destroys blocks in a defined radius.
 */
public class MagicalPickaxe extends Tool {
    private final static String TOOLID = "magicalpickaxe";

    /**
     * Constructs a MagicalPickaxe with default attributes.
     */
    public MagicalPickaxe() {
        super(
                TOOLID,
                "§6§lFlaming Pickaxe",
                new String[] {
                        "§8Special pickaxe that destroys blocks around it."
                },
                Material.GOLDEN_PICKAXE,
                true,
                true,
                false,
                3,
                0
        );
        addCustomAttribute("mining_radius",1);
        addCustomAttribute("destroy_particle", "LAVA");
    }

    /**
     * Handles block breaking and destroys surrounding blocks based on radius.
     * @param event The block break event.
     */
    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(!cooldownEnded(event.getPlayer(), true)) {
            event.setCancelled(true);
            CommandUtils.sendCooldownMessage(event.getPlayer(),getRemainingCooldown(event.getPlayer()));
            return;
        }

        int centerX = event.getBlock().getX();
        int centerY = event.getBlock().getY();
        int centerZ = event.getBlock().getZ();

        boolean worked = false;

        int radius = getCustomAttribute("mining_radius",int.class);

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    Block currentblock = event.getBlock().getWorld().getBlockAt(x,y,z);
                    if(currentblock.getState() instanceof Container || currentblock.isLiquid() || currentblock.isEmpty()) {
                        continue;
                    }

                    if(isIndestructible(currentblock.getType()) && !(event.getPlayer().getGameMode().equals(GameMode.CREATIVE))) {
                        continue;
                    }

                    if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                        for (ItemStack itemStack : currentblock.getDrops()) {
                            currentblock.getWorld().dropItemNaturally(currentblock.getLocation(), itemStack);
                        }
                    }
                    currentblock.setType(Material.AIR);
                    worked = true;
                }
            }
        }

        if(worked) {
            event.getPlayer().spawnParticle(getValidParticle(),event.getBlock().getLocation(),20,1,1,1);
        }
    }

    /**
     * Retrieves the valid particle effect for block destruction.
     * @return The particle effect.
     */
    private Particle getValidParticle() {
        try {
            Particle.valueOf(getCustomAttribute("destroy_particle", String.class));
        } catch (IllegalArgumentException e) {
            Bukkit.getConsoleSender().sendMessage("§4[MagicalTools] Specified magicalpickaxe destroy particle doesn't exist!");
            return Particle.LAVA;
        }
        return Particle.valueOf(getCustomAttribute("destroy_particle", String.class));
    }

    /**
     * Checks if the given material is indestructible.
     * @param material The material to check.
     * @return True if the block is indestructible, false otherwise.
     */
    private boolean isIndestructible(Material material) {
        // Pokud je blok nezničitelný vrátí true
        return material == Material.BEDROCK ||
                material == Material.BARRIER ||
                material == Material.COMMAND_BLOCK ||
                material == Material.REPEATING_COMMAND_BLOCK ||
                material == Material.CHAIN_COMMAND_BLOCK ||
                material == Material.STRUCTURE_BLOCK ||
                material == Material.END_PORTAL_FRAME ||
                material == Material.END_PORTAL ||
                material == Material.NETHER_PORTAL;
    }
}
