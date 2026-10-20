package com.aetherteam.aether.block.construction;

import com.aetherteam.aether.block.AetherBlocks;
import net.minecraft.world.level.block.PathBlock;

/**
 * Aether Dirt Path, which reverts to Aether Dirt. Since 26.3 vanilla's {@link PathBlock} takes the block to revert to,
 * replacing the code copied from {@code DirtPathBlock}.
 */
public class AetherDirtPathBlock extends PathBlock {
    public AetherDirtPathBlock(Properties properties) {
        super(AetherBlocks.AETHER_DIRT, properties);
    }
}
