package com.aetherteam.aether.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;

import java.util.Optional;
import java.util.function.Function;

public final class SurfaceMaterials {
    private SurfaceMaterials() {
    }

    /**
     * Finds the block the dimension's material rules would put at a position if it were the surface, used to match
     * generated blocks to their biome.<br><br>
     * Before 26.3 this was {@code CarvingContext#topMaterial} with the chunk's cached {@code NoiseChunk}; chunks no longer
     * cache one, so the density samplers are built uncached here.
     *
     * @param level       The {@link WorldGenLevel}.
     * @param biomeGetter Finds the biome at a position.
     * @param pos         The {@link BlockPos} to check.
     * @param underFluid  Whether the position is under a fluid.
     * @return The surface {@link BlockState}, or empty if the level doesn't use a noise generator or no rule matches.
     */
    public static Optional<BlockState> topMaterial(WorldGenLevel level, Function<BlockPos, Holder<Biome>> biomeGetter, BlockPos pos, boolean underFluid) {
        if (level.getChunkSource() instanceof ServerChunkCache serverChunkCache && serverChunkCache.getGenerator() instanceof NoiseBasedChunkGenerator generator) {
            ChunkAccess chunk = level.getChunk(pos);
            RandomState randomState = serverChunkCache.randomState();
            MaterialRule rule = generator.generatorSettings().value().materialRule().value();
            WorldGenerationContext context = new WorldGenerationContext(generator, chunk.getHeightAccessorForGeneration());
            return randomState.surfaceSystem().topMaterial(rule, randomState, context, biomeGetter, chunk,
                    randomState.samplersWithContext(SamplerContext.EMPTY_UNCACHED), pos, underFluid);
        }
        return Optional.empty();
    }
}
