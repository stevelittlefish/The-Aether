package com.aetherteam.aether.world.feature;

import com.aetherteam.aether.Aether;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class AetherFeatures {
    public static final MapCodec<ShelfFeature> SHELF = register("shelf", ShelfFeature.CODEC);
    public static final MapCodec<AercloudFeature> AERCLOUD = register("aercloud", AercloudFeature.CODEC);
    public static final MapCodec<CrystalIslandFeature> CRYSTAL_ISLAND = register("crystal_island", CrystalIslandFeature.CODEC);
    public static final MapCodec<AetherLakeFeature> LAKE = register("lake", AetherLakeFeature.CODEC);

    private AetherFeatures() {
    }

    public static void bootstrap() {
    }

    private static <F extends Feature> MapCodec<F> register(String name, MapCodec<F> codec) {
        return Registry.register(BuiltInRegistries.FEATURE_TYPE, Identifier.fromNamespaceAndPath(Aether.MODID, name), codec);
    }
}
