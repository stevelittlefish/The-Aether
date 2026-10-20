package com.aetherteam.aether.data.resources.builders;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.block.AetherBlockStateProperties;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.data.resources.registries.AetherNoises;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.Optional;

public class AetherNoiseBuilders {
    private static final MaterialRule GRASS_BLOCK = MaterialRules.state(AetherBlocks.AETHER_GRASS_BLOCK.defaultBlockState().setValue(AetherBlockStateProperties.DOUBLE_DROPS, true));
    private static final MaterialRule DIRT = MaterialRules.state(AetherBlocks.AETHER_DIRT.defaultBlockState().setValue(AetherBlockStateProperties.DOUBLE_DROPS, true));

    public static NoiseGeneratorSettings skylandsNoiseSettings(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise> noise) {
        BlockState holystone = AetherBlocks.HOLYSTONE.defaultBlockState().setValue(AetherBlockStateProperties.DOUBLE_DROPS, true);
        return new NoiseGeneratorSettings(
                NoiseSettings.create(0, 128), // noiseSettings (the 2x1 noise cell size is now in the final density's interpolation)
                holystone, // defaultBlock
                Blocks.WATER.defaultBlockState(), // defaultFluid
                makeNoiseRouter(densityFunctions, noise), // noiseRouter
                Holder.direct(aetherSurfaceRules()), // materialRule
                List.of(), // spawnTarget
                -64, // seaLevel
                false, // disableMobGeneration
                Optional.empty(), // aquifers (disabled)
                false, // useLegacyRandomSource
                NoiseGeneratorSettings.DebugFunctions.EMPTY // debugFunctions
        );
    }

    public static MaterialRule aetherSurfaceRules() {
        MaterialRule surface = MaterialRules.sequence(MaterialRules.ifTrue(MaterialRules.waterBlockCheck(-1, 0), GRASS_BLOCK), DIRT);
        return MaterialRules.sequence(
                MaterialRules.ifTrue(MaterialRules.stoneDepthCheck(0, false, CaveSurface.FLOOR), surface), // on floor
                MaterialRules.ifTrue(MaterialRules.stoneDepthCheck(0, true, CaveSurface.FLOOR), DIRT)); // under floor
    }

    private static NoiseRouter makeNoiseRouter(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise> noise) {
        return createNoiseRouter(densityFunctions, noise, buildFinalDensity(densityFunctions));
    }

    private static DensityFunction buildFinalDensity(HolderGetter<DensityFunction> densityFunctions) {
        DensityFunction density = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.fromNamespaceAndPath(Aether.MODID, "base_3d_noise_aether")));
        density = DensityFunctions.add(density, DensityFunctions.constant(-0.13F));
        density = slide(density, 0, 128, 72, 0, -0.2F, 8, 40, -0.1F);
        density = DensityFunctions.add(density, DensityFunctions.constant(-0.05F));
        density = DensityFunctions.blendDensity(density);
        density = DensityFunctions.interpolated(density, 8, 4); // The cell size, 2x1 in quarts before 26.3.
        density = density.squeeze();
        // Before 26.3 the noise chunk added the beardifier (terrain adapting to structures) to every final density.
        return DensityFunctions.add(density, DensityFunctions.beardifier());
    }

    private static DensityFunction slide(DensityFunction density, int minY, int maxY, int fromYTop, int toYTop, float offset1, int fromYBottom, int toYBottom, float offset2) {
        DensityFunction topSlide = DensityFunctions.yClampedGradient(minY + maxY - fromYTop, minY + maxY - toYTop, 1, 0);
        density = DensityFunctions.lerp(topSlide, offset1, density);
        DensityFunction bottomSlide = DensityFunctions.yClampedGradient(minY + fromYBottom, minY + toYBottom, 0, 1);
        return DensityFunctions.lerp(bottomSlide, offset2, density);
    }

    private static NoiseRouter createNoiseRouter(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise> noise, DensityFunction finalDensity) {
        DensityFunction shiftX = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("shift_x")));
        DensityFunction shiftZ = getFunction(densityFunctions, ResourceKey.create(Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("shift_z")));
        DensityFunction temperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noise.getOrThrow(AetherNoises.TEMPERATURE));
        DensityFunction vegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25, noise.getOrThrow(AetherNoises.VEGETATION));
        return new NoiseRouter(
                temperature, // temperature
                vegetation, // vegetation
                DensityFunctions.zero(), // continents
                DensityFunctions.zero(), // erosion
                DensityFunctions.zero(), // depth
                DensityFunctions.zero(), // ridges
                DensityFunctions.zero(), // chunkSurfaceLevel
                finalDensity); // finalDensity
    }

    private static DensityFunction getFunction(HolderGetter<DensityFunction> densityFunctions, ResourceKey<DensityFunction> key) {
        return new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(key));
    }
}
