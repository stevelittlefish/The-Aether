package com.aetherteam.aether.data.resources.builders;

import com.aetherteam.aether.data.resources.AetherMobCategory;
import com.aetherteam.aether.data.resources.registries.AetherBiomes;
import com.aetherteam.aether.data.resources.registries.AetherPlacedFeatures;
import com.aetherteam.aether.entity.AetherEntityTypes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class AetherBiomeBuilders {
    public static Biome skyrootMeadowBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<WorldCarver> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.SKYROOT_MEADOW_TREES_PLACEMENT));
    }

    public static Biome skyrootGroveBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<WorldCarver> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.SKYROOT_GROVE_TREES_PLACEMENT));
    }

    public static Biome skyrootWoodlandBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<WorldCarver> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.SKYROOT_WOODLAND_TREES_PLACEMENT));
    }

    public static Biome skyrootForestBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<WorldCarver> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.SKYROOT_FOREST_TREES_PLACEMENT));
    }

    public static Biome makeDefaultBiome(BiomeGenerationSettings.Builder builder) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder()
                .addMobSpawnCost(AetherEntityTypes.COCKATRICE, 0.5, 0.15)
                .addMobSpawnCost(AetherEntityTypes.AECHOR_PLANT, 0.4, 0.11)
                .addMobSpawnCost(AetherEntityTypes.BLUE_SWET, 0.5, 0.1)
                .addMobSpawnCost(AetherEntityTypes.GOLDEN_SWET, 0.5, 0.1)
                .addMobSpawnCost(AetherEntityTypes.WHIRLWIND, 0.4, 0.1)
                .addMobSpawnCost(AetherEntityTypes.EVIL_WHIRLWIND, 0.4, 0.1)

                .addSpawn(AetherEntityTypes.COCKATRICE, AetherMobCategory.AETHER_DARKNESS_MONSTER, 8, count(1, 1))
                .addSpawn(AetherEntityTypes.AECHOR_PLANT, AetherMobCategory.AETHER_SURFACE_MONSTER, 7, count(1, 1))
                .addSpawn(AetherEntityTypes.BLUE_SWET, AetherMobCategory.AETHER_SURFACE_MONSTER, 6, count(1, 1))
                .addSpawn(AetherEntityTypes.GOLDEN_SWET, AetherMobCategory.AETHER_SURFACE_MONSTER, 6, count(1, 1))
                .addSpawn(AetherEntityTypes.WHIRLWIND, AetherMobCategory.AETHER_SURFACE_MONSTER, 3, count(1, 1))
                .addSpawn(AetherEntityTypes.EVIL_WHIRLWIND, AetherMobCategory.AETHER_SURFACE_MONSTER, 1, count(1, 1))

                .addSpawn(AetherEntityTypes.PHYG, MobCategory.CREATURE, 10, count(3, 4))
                .addSpawn(AetherEntityTypes.SHEEPUFF, MobCategory.CREATURE, 12, count(3, 4))
                .addSpawn(AetherEntityTypes.FLYING_COW, MobCategory.CREATURE, 12, count(2, 5))
                .addSpawn(AetherEntityTypes.AERBUNNY, MobCategory.CREATURE, 11, count(3, 3))
                .addSpawn(AetherEntityTypes.MOA, MobCategory.CREATURE, 8, count(1, 3));

        if (AetherMobCategory.hasCustomSkyMonsterCategory()) {
            spawnSettings.addMobSpawnCost(AetherEntityTypes.ZEPHYR, 0.6, 0.16)
                    .addSpawn(AetherEntityTypes.ZEPHYR, AetherMobCategory.AETHER_SKY_MONSTER, 20, count(1, 1));
        }
        if (AetherMobCategory.hasCustomAerwhaleCategory()) {
            spawnSettings.addMobSpawnCost(AetherEntityTypes.AERWHALE, 0.5, 0.11)
                    .addSpawn(AetherEntityTypes.AERWHALE, AetherMobCategory.AETHER_AERWHALE, 10, count(1, 1));
        }

        return fullDefinition(
                false,
                0.8F,
                0.0F,
                0.25F,
                new BiomeSpecialEffects.Builder()
                        .waterColor(0x3f_76_e4)
                        .grassColorOverride(0xb1_ff_cb)
                        .foliageColorOverride(0xb1_ff_cb)
                        .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE)
                        .build(),
                spawnSettings.build(),
                builder
                        .addFeature(GenerationStep.Decoration.RAW_GENERATION, AetherPlacedFeatures.QUICKSOIL_SHELF_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.LAKES, AetherPlacedFeatures.WATER_LAKE_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AetherPlacedFeatures.ORE_AETHER_DIRT_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AetherPlacedFeatures.ORE_ICESTONE_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AetherPlacedFeatures.ORE_AMBROSIUM_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AetherPlacedFeatures.ORE_ZANITE_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AetherPlacedFeatures.ORE_GRAVITITE_BURIED_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AetherPlacedFeatures.ORE_GRAVITITE_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.FLUID_SPRINGS, AetherPlacedFeatures.WATER_SPRING_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.HOLIDAY_TREE_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.GRASS_PATCH_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.TALL_GRASS_PATCH_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.WHITE_FLOWER_PATCH_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.PURPLE_FLOWER_PATCH_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AetherPlacedFeatures.BERRY_BUSH_PATCH_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, AetherPlacedFeatures.CRYSTAL_ISLAND_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, AetherPlacedFeatures.COLD_AERCLOUD_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, AetherPlacedFeatures.BLUE_AERCLOUD_PLACEMENT)
                        .addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, AetherPlacedFeatures.GOLDEN_AERCLOUD_PLACEMENT)
                        .build(),
                Biome.TemperatureModifier.NONE
        );
    }

    public static Biome fullDefinition(boolean precipitation, float temperature, float downfall, float creatureGenerationProbability, BiomeSpecialEffects effects, MobSpawnSettings spawnSettings, BiomeGenerationSettings generationSettings, Biome.TemperatureModifier temperatureModifier) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(precipitation)
                .temperature(temperature)
                .downfall(downfall)
                .setAttribute(EnvironmentAttributes.CREATURE_WORLD_GEN_SPAWN_PROBABILITY, creatureGenerationProbability)
                .specialEffects(effects)
                .mobSpawnSettings(spawnSettings)
                .generationSettings(generationSettings)
                .temperatureAdjustment(temperatureModifier)
                .build();
    }

    private static IntProvider count(int min, int max) {
        return min == max ? ConstantInt.of(min) : UniformInt.of(min, max);
    }

    public static BiomeSource buildAetherBiomeSource(HolderGetter<Biome> biomes) {
        Climate.Parameter fullRange = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter temps1 = Climate.Parameter.span(-1.0F, -0.8F);
        Climate.Parameter temps2 = Climate.Parameter.span(-0.8F, 0.0F);
        Climate.Parameter temps3 = Climate.Parameter.span(0.0F, 0.4F);
        Climate.Parameter temps4 = Climate.Parameter.span(0.4F, 0.93F);
        Climate.Parameter temps5 = Climate.Parameter.span(0.93F, 0.94F);
        Climate.Parameter temps6 = Climate.Parameter.span(0.94F, 1.0F);
        return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(List.of(
                // Row 1
                Pair.of(new Climate.ParameterPoint(temps1, fullRange, fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_MEADOW)),
                // Row 2
                Pair.of(new Climate.ParameterPoint(temps2, Climate.Parameter.span(-1.0F, 0.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_MEADOW)),
                Pair.of(new Climate.ParameterPoint(temps2, Climate.Parameter.span(0.0F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_FOREST)),
                // Row 3
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(-1.0F, 0.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_GROVE)),
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(0.0F, 0.8F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_FOREST)),
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(0.8F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_GROVE)),
                // Row 4
                Pair.of(new Climate.ParameterPoint(temps4, Climate.Parameter.span(-1.0F, -0.1F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_GROVE)),
                Pair.of(new Climate.ParameterPoint(temps4, Climate.Parameter.span(-0.1F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_FOREST)),
                // Row 5
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-1.0F, -0.6F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_MEADOW)),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-0.6F, -0.3F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_GROVE)),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-0.3F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_FOREST)),
                // Row 6
                Pair.of(new Climate.ParameterPoint(temps6, Climate.Parameter.span(-1.0F, -0.1F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_MEADOW)),
                Pair.of(new Climate.ParameterPoint(temps6, Climate.Parameter.span(-0.1F, 0.8F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_WOODLAND)),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(0.8F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AetherBiomes.SKYROOT_FOREST))
        )));
    }
}
