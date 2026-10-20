package com.aetherteam.aether.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record AercloudFeature(int bounds, Holder<BlockStateProvider> block) implements Feature {
    public static final MapCodec<AercloudFeature> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.INT.fieldOf("bounds").forGetter(AercloudFeature::bounds),
            BlockStateProvider.CODEC.fieldOf("blocks").forGetter(AercloudFeature::block)
    ).apply(instance, AercloudFeature::new));

    @Override
    public MapCodec<AercloudFeature> codec() {
        return CODEC;
    }

    /**
     * Randomly places an area blocks in a direction to create a cloud.
     * The code is taken from older versions.
     *
     * @param origin The {@link BlockPos} to place at.
     * @return Whether the placement was successful, as a {@link Boolean}.
     */
    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        boolean direction = random.nextBoolean();
        BlockPos blockPos = origin.offset(-random.nextInt(8), 0, (direction ? 8 : 0) - random.nextInt(8));
        BlockState blockState = this.block.value().getState(level, random, blockPos);

        for (int amount = 0; amount < this.bounds; ++amount) {
            int xOffset = random.nextInt(2);
            int yOffset = (random.nextBoolean() ? random.nextInt(3) - 1 : 0);
            int zOffset = random.nextInt(2);

            if (direction) {
                blockPos = blockPos.offset(xOffset, yOffset, -zOffset);
            } else {
                blockPos = blockPos.offset(xOffset, yOffset, zOffset);
            }

            for (int x = blockPos.getX(); x < blockPos.getX() + random.nextInt(2) + 3; ++x) {
                for (int y = blockPos.getY(); y < blockPos.getY() + random.nextInt(1) + 2; ++y) {
                    for (int z = blockPos.getZ(); z < blockPos.getZ() + random.nextInt(2) + 3; ++z) {
                        BlockPos newPosition = new BlockPos(x, y, z);

                        if (level.isEmptyBlock(newPosition)) {
                            if (Math.abs(x - blockPos.getX()) + Math.abs(y - blockPos.getY()) + Math.abs(z - blockPos.getZ()) < 4 + random.nextInt(2)) {
                                this.setBlock(level, newPosition, blockState);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
