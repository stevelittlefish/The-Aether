package com.aetherteam.aether.data.resources.builders;

import com.aetherteam.aether.block.AetherBlockStateProperties;
import com.aetherteam.aether.world.feature.AercloudFeature;
import com.aetherteam.aether.world.feature.AetherLakeFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.SpringFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.FluidState;

public class AetherConfiguredFeatureBuilders {
    public static AercloudFeature aercloud(int bounds, BlockState blockState) {
        return new AercloudFeature(bounds, BlockStateProvider.holderOf(blockState.setValue(AetherBlockStateProperties.DOUBLE_DROPS, true)));
    }

    public static AetherLakeFeature lake(Holder<BlockStateProvider> fluid, Holder<BlockStateProvider> top) {
        return new AetherLakeFeature(fluid, top);
    }

    public static SimpleBlockFeature grassPatch(BlockStateProvider block, int tries) {
        return new SimpleBlockFeature(block);
    }

    public static SimpleBlockFeature tallGrassPatch(BlockStateProvider block) {
        return new SimpleBlockFeature(block);
    }

    public static SpringFeature spring(FluidState fluid, boolean requiresBlocksBelow, int rockCount, int holeCount, HolderSet<Block> validBlocks) {
        return new SpringFeature(fluid, requiresBlocksBelow, rockCount, holeCount, validBlocks);
    }
}
