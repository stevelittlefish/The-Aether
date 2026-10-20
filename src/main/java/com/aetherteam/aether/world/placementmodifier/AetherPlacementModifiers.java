package com.aetherteam.aether.world.placementmodifier;

import com.aetherteam.aether.Aether;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import com.mojang.serialization.MapCodec;

public final class AetherPlacementModifiers {
    public static final MapCodec<ConfigFilter> CONFIG_FILTER = Registry.register(
            BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
            Identifier.fromNamespaceAndPath(Aether.MODID, "config_filter"),
            ConfigFilter.CODEC);
    public static final MapCodec<HolidayFilter> HOLIDAY_FILTER = Registry.register(
            BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
            Identifier.fromNamespaceAndPath(Aether.MODID, "holiday_filter"),
            HolidayFilter.CODEC);
    public static final MapCodec<ImprovedLayerPlacementModifier> IMPROVED_LAYER_PLACEMENT = Registry.register(
            BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
            Identifier.fromNamespaceAndPath(Aether.MODID, "improved_layer_placement"),
            ImprovedLayerPlacementModifier.CODEC);
    public static final MapCodec<DungeonBlacklistFilter> DUNGEON_BLACKLIST_FILTER = Registry.register(
            BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
            Identifier.fromNamespaceAndPath(Aether.MODID, "dungeon_blacklist_filter"),
            DungeonBlacklistFilter.CODEC);

    private AetherPlacementModifiers() {
    }

    public static void bootstrap() {
    }
}
