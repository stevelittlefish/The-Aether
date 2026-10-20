package com.aetherteam.aether.block.construction;

import com.aetherteam.aether.block.AetherBlocks;
import net.minecraft.world.level.block.FarmlandBlock;

/**
 * Aether Farmland, which reverts to Aether Dirt. Since 26.3 vanilla's {@link FarmlandBlock} takes the block to revert to,
 * replacing the code copied from it.
 */
public class AetherFarmBlock extends FarmlandBlock {
    public AetherFarmBlock(Properties properties) {
        super(AetherBlocks.AETHER_DIRT, properties);
    }
}
