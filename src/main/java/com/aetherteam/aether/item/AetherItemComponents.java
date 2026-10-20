package com.aetherteam.aether.item;

import com.aetherteam.aether.block.AetherBlocks;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.ItemLike;

import java.util.Map;

/**
 * Furnace fuels and composter inputs. The NeoForge build had these as data maps ({@code neoforge:furnace_fuels} and
 * {@code neoforge:compostables}), which the Fabric port dropped; since 26.3 vanilla has them as item components.
 */
public final class AetherItemComponents {
    private AetherItemComponents() {
    }

    public static void register() {
        Map<ItemLike, ResourceKey<ContextIntProvider>> fuels = Map.ofEntries(
                Map.entry(AetherBlocks.AMBROSIUM_BLOCK, ContextIntProviders.COOKING_TIME_COAL_BLOCK), // 16000
                Map.entry(AetherItems.AMBROSIUM_SHARD, ContextIntProviders.COOKING_TIME_COAL), // 1600
                Map.entry(AetherBlocks.SKYROOT_PLANKS, ContextIntProviders.COOKING_TIME_WOOD_BLOCKS), // 300
                Map.entry(AetherBlocks.SKYROOT_BOOKSHELF, ContextIntProviders.COOKING_TIME_WOOD_BLOCKS), // 300
                Map.entry(AetherItems.SKYROOT_AXE, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE), // 200
                Map.entry(AetherItems.SKYROOT_HOE, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE),
                Map.entry(AetherItems.SKYROOT_PICKAXE, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE),
                Map.entry(AetherItems.SKYROOT_SHOVEL, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE),
                Map.entry(AetherItems.SKYROOT_SWORD, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE),
                Map.entry(AetherItems.SKYROOT_BUCKET, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE),
                Map.entry(AetherItems.SKYROOT_STICK, ContextIntProviders.COOKING_TIME_WOOD_ITEMS_EXTRA_SMALL)); // 100
        // Chances 0.3, 0.5 and 0.65 in the NeoForge data map.
        Map<ItemLike, ResourceKey<ContextIntProvider>> compostables = Map.ofEntries(
                Map.entry(AetherBlocks.BERRY_BUSH, ContextIntProviders.COMPOSTABLE_LOW_MEDIUM),
                Map.entry(AetherBlocks.BERRY_BUSH_STEM, ContextIntProviders.COMPOSTABLE_LOW_MEDIUM),
                Map.entry(AetherItems.ENCHANTED_BERRY, ContextIntProviders.COMPOSTABLE_LOW_MEDIUM),
                Map.entry(AetherItems.BLUE_BERRY, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.SKYROOT_LEAVES, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.GOLDEN_OAK_LEAVES, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.CRYSTAL_LEAVES, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.CRYSTAL_FRUIT_LEAVES, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.HOLIDAY_LEAVES, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.DECORATED_HOLIDAY_LEAVES, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.SKYROOT_SAPLING, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.GOLDEN_OAK_SAPLING, ContextIntProviders.COMPOSTABLE_LOW),
                Map.entry(AetherBlocks.PURPLE_FLOWER, ContextIntProviders.COMPOSTABLE_MEDIUM),
                Map.entry(AetherBlocks.WHITE_FLOWER, ContextIntProviders.COMPOSTABLE_MEDIUM),
                Map.entry(AetherItems.WHITE_APPLE, ContextIntProviders.COMPOSTABLE_MEDIUM));
        DefaultItemComponentEvents.MODIFY.register(context -> {
            fuels.forEach((item, burnTime) -> context.modify(item.asItem(), builder -> builder.set(DataComponents.COOKING_FUEL,
                    new CookingFuel(burnTime, ContextFloatProviders.COOKING_DEFAULT_SPEED_MULTIPLIER))));
            compostables.forEach((item, layers) -> context.modify(item.asItem(), builder -> builder.set(DataComponents.COMPOSTABLE,
                    new Compostable(layers))));
        });
    }
}
