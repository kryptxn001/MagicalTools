package me.spirek.magicalTools.tools.tools;

import me.spirek.magicalTools.tools.Tool;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MagicalPickaxe extends Tool {
    private final static String TOOLID = "magicalpickaxe";

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
                1,
                0
        );
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }
        int centerX = event.getBlock().getX();
        int centerY = event.getBlock().getY();
        int centerZ = event.getBlock().getZ();

        boolean worked = false;

        for (int x = centerX - 1; x <= centerX + 1; x++) {
            for (int y = centerY - 1; y <= centerY + 1; y++) {
                for (int z = centerZ - 1; z <= centerZ + 1; z++) {
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
            event.getPlayer().spawnParticle(Particle.LAVA,event.getBlock().getLocation(),20,1,1,1);
        }
    }

    public boolean isIndestructible(Material material) {
        // List of indestructible blocks
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
