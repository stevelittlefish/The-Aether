package com.aetherteam.aether.block.natural;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import com.aetherteam.aether.block.AetherBlockStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class AetherDoubleDropsLeaves extends LeavesBlock {
    public AetherDoubleDropsLeaves(Properties properties) {
        super(0.01F, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AetherBlockStateProperties.DOUBLE_DROPS, false));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AetherBlockStateProperties.DOUBLE_DROPS);
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
    }
}
