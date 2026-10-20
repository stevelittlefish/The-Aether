package com.aetherteam.aether.world.feature;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.data.resources.AetherFeatureStates;
import com.aetherteam.aether.data.resources.registries.AetherConfiguredFeatures;
import com.aetherteam.aether.world.BlockLogicUtil;
import com.aetherteam.aether.world.SurfaceMaterials;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Arrays;
import java.util.Optional;

public record CrystalIslandFeature() implements Feature {
    public static final MapCodec<CrystalIslandFeature> CODEC = MapCodec.unit(CrystalIslandFeature::new);

    @Override
    public MapCodec<CrystalIslandFeature> codec() {
        return CODEC;
    }

    /**
     * Places a Crystal Tree, then creates a small island around it if the placement was successful.
     *
     * @param origin The {@link BlockPos} to place at.
     * @return Whether the placement was successful, as a {@link Boolean}.
     */
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
        PlacedFeature feature = PlacementUtils.inlinePlaced(level.registryAccess().lookupOrThrow(Registries.FEATURE).getOrThrow(AetherConfiguredFeatures.CRYSTAL_TREE_CONFIGURATION)).value();
        if (feature.place(level, chunkGenerator, random, pos.above())) {
            for (int i = 0; i < 3; i++) {
                BlockState state;
                if (i == 0) {
                    state = AetherFeatureStates.AETHER_GRASS_BLOCK;
                } else {
                    state = AetherFeatureStates.HOLYSTONE;
                }
                int offset = i;
                this.setIslandBlock(level, pos.below(offset), state);
                Arrays.stream(Direction.values()).toList().subList(2, 6).forEach(direction -> {
                    this.setIslandBlock(level, pos.relative(direction).below(offset), state);
                    if (offset != 2) {
                        this.setIslandBlock(level, pos.relative(direction, 2).below(offset), state);
                        this.setIslandBlock(level, pos.relative(direction).relative(direction.getClockWise()).below(offset), state);
                    }
                });
            }
            return true;
        } else {
            return false;
        }
    }

    private void setIslandBlock(WorldGenLevel level, BlockPos pos, BlockState testState) {
        // If the processor is running outside the center chunk, return immediately.
        if (!(level instanceof WorldGenRegion region) || !BlockLogicUtil.isOutOfBounds(pos, region.getCenter())) {
            BlockState newState = testState;
            Optional<BlockState> state = SurfaceMaterials.topMaterial(level, level.getBiomeManager()::getNoiseBiomeAtPosition, pos, false);
            if (state.isPresent()) {
                if (testState.is(AetherTags.Blocks.AETHER_DIRT) && !testState.is(AetherBlocks.AETHER_DIRT) && state.get().is(AetherTags.Blocks.AETHER_DIRT)) {
                    newState = state.get();
                }
            }
            this.setBlock(level, pos, newState);
        }
    }
}
