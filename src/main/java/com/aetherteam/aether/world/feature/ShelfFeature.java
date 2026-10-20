package com.aetherteam.aether.world.feature;

import com.aetherteam.aether.world.BlockPlacementUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record ShelfFeature(Holder<BlockStateProvider> block, FloatProvider radius, IntProvider yRange, HolderSet<Block> validBlocks) implements Feature {
    public static final MapCodec<ShelfFeature> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            BlockStateProvider.CODEC.fieldOf("block").forGetter(ShelfFeature::block),
            FloatProviders.CODEC.fieldOf("radius").forGetter(ShelfFeature::radius),
            IntProviders.CODEC.fieldOf("y_range").forGetter(ShelfFeature::yRange),
            RegistryCodecs.holderSet(Registries.BLOCK).fieldOf("valid_blocks").forGetter(ShelfFeature::validBlocks)
    ).apply(instance, ShelfFeature::new));

    @Override
    public MapCodec<ShelfFeature> codec() {
        return CODEC;
    }

    /**
     * Places disks within a y-range and in a 16x16 area, creating continuous shelves along island edges.
     *
     * @param origin The {@link BlockPos} to place at.
     * @return Whether the placement was successful, as a {@link Boolean}.
     */
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {

        for (int x = pos.getX(); x < pos.getX() + 16; ++x) {
            for (int z = pos.getZ(); z < pos.getZ() + 16; ++z) {
                for (int y = this.yRange.minInclusive(); y < this.yRange.maxInclusive(); ++y) {
                    BlockPos placementPos = new BlockPos(x, y, z);
                    if (level.getBlockState(placementPos).isAir() && level.getBlockState(placementPos.above()).is(this.validBlocks) && level.getBlockState(placementPos.above(2)).isAir()) {
                        BlockPlacementUtil.placeDisk(level, this.block.value(), placementPos, this.radius.sample(random), random);
                        break;
                    }
                }
            }
        }
        return true;
    }
}
