package com.aetherteam.aether.data.resources.registries;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.data.resources.AetherFeatureRules;
import com.aetherteam.aether.data.resources.AetherFeatureStates;
import com.aetherteam.aether.data.resources.builders.AetherConfiguredFeatureBuilders;
import com.aetherteam.aether.world.feature.CrystalIslandFeature;
import com.aetherteam.aether.world.feature.ShelfFeature;
import com.aetherteam.aether.world.foliageplacer.CrystalFoliagePlacer;
import com.aetherteam.aether.world.foliageplacer.GoldenOakFoliagePlacer;
import com.aetherteam.aether.world.foliageplacer.HolidayFoliagePlacer;
import com.aetherteam.aether.world.treedecorator.HolidayTreeDecorator;
import com.aetherteam.aether.world.trunkplacer.CrystalTreeTrunkPlacer;
import com.aetherteam.aether.world.trunkplacer.GoldenOakTrunkPlacer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.random.WeightedList.Builder;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.OptionalInt;

public class AetherConfiguredFeatures {
    public static final ResourceKey<Feature> COLD_AERCLOUD_CONFIGURATION = createKey("cold_aercloud");
    public static final ResourceKey<Feature> BLUE_AERCLOUD_CONFIGURATION = createKey("blue_aercloud");
    public static final ResourceKey<Feature> GOLDEN_AERCLOUD_CONFIGURATION = createKey("golden_aercloud");
    public static final ResourceKey<Feature> CRYSTAL_ISLAND_CONFIGURATION = createKey("crystal_island");
    public static final ResourceKey<Feature> SKYROOT_TREE_CONFIGURATION = createKey("skyroot_tree");
    public static final ResourceKey<Feature> GOLDEN_OAK_TREE_CONFIGURATION = createKey("golden_oak_tree");
    public static final ResourceKey<Feature> CRYSTAL_TREE_CONFIGURATION = createKey("crystal_tree");
    public static final ResourceKey<Feature> HOLIDAY_TREE_CONFIGURATION = createKey("holiday_tree");
    public static final ResourceKey<Feature> GRASS_PATCH_CONFIGURATION = createKey("grass_patch");
    public static final ResourceKey<Feature> TALL_GRASS_PATCH_CONFIGURATION = createKey("tall_grass_patch");
    public static final ResourceKey<Feature> WHITE_FLOWER_PATCH_CONFIGURATION = createKey("white_flower_patch");
    public static final ResourceKey<Feature> PURPLE_FLOWER_PATCH_CONFIGURATION = createKey("purple_flower_patch");
    public static final ResourceKey<Feature> BERRY_BUSH_PATCH_CONFIGURATION = createKey("berry_bush_patch");
    public static final ResourceKey<Feature> QUICKSOIL_SHELF_CONFIGURATION = createKey("quicksoil_shelf");
    public static final ResourceKey<Feature> WATER_LAKE_CONFIGURATION = createKey("water_lake");
    public static final ResourceKey<Feature> WATER_SPRING_CONFIGURATION = createKey("water_spring");
    public static final ResourceKey<Feature> ORE_AETHER_DIRT_CONFIGURATION = createKey("aether_dirt_ore");
    public static final ResourceKey<Feature> ORE_ICESTONE_CONFIGURATION = createKey("icestone_ore");
    public static final ResourceKey<Feature> ORE_AMBROSIUM_CONFIGURATION = createKey("ambrosium_ore");
    public static final ResourceKey<Feature> ORE_ZANITE_CONFIGURATION = createKey("zanite_ore");
    public static final ResourceKey<Feature> ORE_GRAVITITE_BURIED_CONFIGURATION = createKey("gravitite_ore_buried");
    public static final ResourceKey<Feature> ORE_GRAVITITE_CONFIGURATION = createKey("gravitite_ore");
    public static final ResourceKey<Feature> TREES_SKYROOT_AND_GOLDEN_OAK_CONFIGURATION = createKey("trees_skyroot_and_golden_oak");
    public static final ResourceKey<Feature> SINGLE_GOLD_DUNGEON_FLOWER_CONFIGURATION = createKey("single_gold_dungeon_flower");
    public static final ResourceKey<Feature> GOLD_DUNGEON_ISLAND_FOLIAGE_CONFIGURATION = createKey("gold_dungeon_island_foliage");

    private static ResourceKey<Feature> createKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(Aether.MODID, name));
    }

    /**
     * Warning for "deprecation" is suppressed because {@link Block#builtInRegistryHolder()} is fine to use.
     */
    @SuppressWarnings("deprecation")
    public static void bootstrap(BootstrapContext<Feature> context) {
        HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);
        context.register(COLD_AERCLOUD_CONFIGURATION, AetherConfiguredFeatureBuilders.aercloud(16, AetherFeatureStates.COLD_AERCLOUD));
        context.register(BLUE_AERCLOUD_CONFIGURATION, AetherConfiguredFeatureBuilders.aercloud(8, AetherFeatureStates.BLUE_AERCLOUD));
        context.register(GOLDEN_AERCLOUD_CONFIGURATION, AetherConfiguredFeatureBuilders.aercloud(4, AetherFeatureStates.GOLDEN_AERCLOUD));
        context.register(CRYSTAL_ISLAND_CONFIGURATION, new CrystalIslandFeature());
        context.register(SKYROOT_TREE_CONFIGURATION,
                new TreeFeature.Builder(
                        BlockStateProvider.of(AetherFeatureStates.SKYROOT_LOG),
                        new StraightTrunkPlacer(4, 2, 0),
                        BlockStateProvider.of(AetherFeatureStates.SKYROOT_LEAVES),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1),
                        BlockStateProvider.holderOf(AetherFeatureStates.AETHER_DIRT)
                ).ignoreVines().build());
        context.register(GOLDEN_OAK_TREE_CONFIGURATION,
                new TreeFeature.Builder(
                        BlockStateProvider.of(AetherFeatureStates.GOLDEN_OAK_LOG),
                        new GoldenOakTrunkPlacer(10, 0, 0),
                        BlockStateProvider.of(AetherFeatureStates.GOLDEN_OAK_LEAVES),
                        new GoldenOakFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), ConstantInt.of(7)),
                        new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(10)),
                        BlockStateProvider.holderOf(AetherFeatureStates.AETHER_DIRT)
                ).ignoreVines().build());
        context.register(CRYSTAL_TREE_CONFIGURATION,
                new TreeFeature.Builder(
                        BlockStateProvider.of(AetherFeatureStates.SKYROOT_LOG),
                        new CrystalTreeTrunkPlacer(7, 0, 0),
                        new WeightedStateProvider(new WeightedList.Builder<BlockState>().add(AetherFeatureStates.CRYSTAL_LEAVES, 4).add(AetherFeatureStates.CRYSTAL_FRUIT_LEAVES, 1).build()),
                        new CrystalFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), ConstantInt.of(6)),
                        new TwoLayersFeatureSize(1, 0, 1),
                        BlockStateProvider.holderOf(AetherFeatureStates.AETHER_DIRT)).ignoreVines().build());
        context.register(HOLIDAY_TREE_CONFIGURATION,
                new TreeFeature.Builder(
                        BlockStateProvider.of(AetherFeatureStates.SKYROOT_LOG),
                        new StraightTrunkPlacer(9, 0, 0),
                        new WeightedStateProvider(new WeightedList.Builder<BlockState>().add(AetherFeatureStates.HOLIDAY_LEAVES, 4).add(AetherFeatureStates.DECORATED_HOLIDAY_LEAVES, 1).build()),
                        new HolidayFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), ConstantInt.of(8)),
                        new TwoLayersFeatureSize(1, 0, 1),
                        BlockStateProvider.holderOf(AetherFeatureStates.AETHER_DIRT)).ignoreVines()
                        .decorators(List.of(new HolidayTreeDecorator(new WeightedStateProvider(new WeightedList.Builder<BlockState>().add(AetherFeatureStates.SNOW, 10).add(AetherFeatureStates.PRESENT, 1).build()))))
                        .build());
        context.register(GRASS_PATCH_CONFIGURATION, AetherConfiguredFeatureBuilders.grassPatch(BlockStateProvider.of(Blocks.SHORT_GRASS), 32));
        context.register(TALL_GRASS_PATCH_CONFIGURATION, AetherConfiguredFeatureBuilders.tallGrassPatch(BlockStateProvider.of(Blocks.TALL_GRASS)));
        context.register(WHITE_FLOWER_PATCH_CONFIGURATION,
                AetherConfiguredFeatureBuilders.grassPatch(new WeightedStateProvider(WeightedList.<BlockState>builder().add(AetherFeatureStates.WHITE_FLOWER, 1)), 64));
        context.register(PURPLE_FLOWER_PATCH_CONFIGURATION,
                AetherConfiguredFeatureBuilders.grassPatch(new WeightedStateProvider(WeightedList.<BlockState>builder().add(AetherFeatureStates.PURPLE_FLOWER, 1)), 64));
        context.register(BERRY_BUSH_PATCH_CONFIGURATION,
                AetherConfiguredFeatureBuilders.grassPatch(new WeightedStateProvider(WeightedList.<BlockState>builder().add(AetherFeatureStates.BERRY_BUSH, 1)), 32));
        context.register(QUICKSOIL_SHELF_CONFIGURATION,
                new ShelfFeature(
                        BlockStateProvider.holderOf(AetherFeatureStates.QUICKSOIL),
                        ConstantFloat.of(Mth.sqrt(12)),
                        UniformInt.of(0, 48),
                        HolderSet.direct(Block::builtInRegistryHolder, AetherBlocks.AETHER_GRASS_BLOCK)));
        context.register(WATER_LAKE_CONFIGURATION,
                AetherConfiguredFeatureBuilders.lake(BlockStateProvider.holderOf(Blocks.WATER), BlockStateProvider.holderOf(AetherFeatureStates.AETHER_GRASS_BLOCK)));
        context.register(WATER_SPRING_CONFIGURATION,
                AetherConfiguredFeatureBuilders.spring(Fluids.WATER.defaultFluidState(), true, 4, 1, HolderSet.direct(Block::builtInRegistryHolder, AetherBlocks.HOLYSTONE, AetherBlocks.AETHER_DIRT)));
        context.register(ORE_AETHER_DIRT_CONFIGURATION, new OreFeature(AetherFeatureRules.HOLYSTONE, AetherFeatureStates.AETHER_DIRT, 33));
        context.register(ORE_ICESTONE_CONFIGURATION, new OreFeature(AetherFeatureRules.HOLYSTONE, AetherFeatureStates.ICESTONE, 32));
        context.register(ORE_AMBROSIUM_CONFIGURATION, new OreFeature(AetherFeatureRules.HOLYSTONE, AetherFeatureStates.AMBROSIUM_ORE, 16));
        context.register(ORE_ZANITE_CONFIGURATION, new OreFeature(List.of(new BlockReplacement(AetherFeatureRules.HOLYSTONE, AetherFeatureStates.ZANITE_ORE)), 5, 0.5F));
        context.register(ORE_GRAVITITE_BURIED_CONFIGURATION, new OreFeature(List.of(new BlockReplacement(AetherFeatureRules.HOLYSTONE, AetherFeatureStates.GRAVITITE_ORE)), 3, 0.5F));
        context.register(ORE_GRAVITITE_CONFIGURATION, new OreFeature(AetherFeatureRules.HOLYSTONE, AetherFeatureStates.GRAVITITE_ORE, 4));
        context.register(TREES_SKYROOT_AND_GOLDEN_OAK_CONFIGURATION, new RandomSelectorFeature(List.of(new WeightedPlacedFeature(
                PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(GOLDEN_OAK_TREE_CONFIGURATION), PlacementUtils.filteredByBlockSurvival(AetherBlocks.GOLDEN_OAK_SAPLING)), 0.01F)),
                PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(SKYROOT_TREE_CONFIGURATION), PlacementUtils.filteredByBlockSurvival(AetherBlocks.SKYROOT_SAPLING))));
        context.register(SINGLE_GOLD_DUNGEON_FLOWER_CONFIGURATION,
            new SimpleBlockFeature(new WeightedStateProvider(
                new Builder<BlockState>()
                    .add(Blocks.DANDELION.defaultBlockState())
                    .add(Blocks.POPPY.defaultBlockState()))));
        context.register(GOLD_DUNGEON_ISLAND_FOLIAGE_CONFIGURATION, new RandomSelectorFeature(List.of(new WeightedPlacedFeature(
            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(GOLDEN_OAK_TREE_CONFIGURATION), PlacementUtils.filteredByBlockSurvival(AetherBlocks.GOLDEN_OAK_SAPLING)), 0.66F)),
            PlacementUtils.inlinePlaced(configuredFeatures.getOrThrow(SINGLE_GOLD_DUNGEON_FLOWER_CONFIGURATION), PlacementUtils.isEmpty())));

    }

}
