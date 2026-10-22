package com.aetherteam.aether.loot;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.item.AetherItems;
import com.aetherteam.aether.item.EquipmentUtil;
import com.aetherteam.aether.item.combat.abilities.weapon.SkyrootWeapon;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

/**
 * The global loot modifiers from the NeoForge build ({@code loot/modifiers}), run from Fabric's
 * {@link LootTableEvents#MODIFY_DROPS}. Nothing on Fabric reads {@code data/aether/loot_modifiers}, so their conditions are checked here.
 */
public final class AetherLootModifiers {
    private AetherLootModifiers() {
    }

    public static void register() {
        LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> {
            removeSeeds(context, drops);
            enchantedGrassBerries(context, drops);
            skyrootDoubleDrops(context, drops);
            pigSlayerDrops(context, drops);
        });
    }

    /**
     * Removes Wheat Seeds from grass broken without shears in biomes tagged {@link AetherTags.Biomes#NO_WHEAT_SEEDS}.
     */
    private static void removeSeeds(LootContext context, List<ItemStack> drops) {
        BlockState state = context.getOptional(LootContextParams.BLOCK_STATE);
        Vec3 origin = context.getOptional(LootContextParams.ORIGIN);
        if (state == null || origin == null || !(state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS))) {
            return;
        }
        ItemInstance tool = context.getOptional(LootContextParams.TOOL);
        if (tool != null && tool.is(Items.SHEARS)) {
            return;
        }
        if (context.getLevel().getBiome(BlockPos.containing(origin)).is(AetherTags.Biomes.NO_WHEAT_SEEDS)) {
            drops.removeIf(stack -> stack.is(Items.WHEAT_SEEDS));
        }
    }

    /**
     * Gives a Berry Bush on Enchanted Grass a 50% chance of an extra Blue Berry.
     */
    private static void enchantedGrassBerries(LootContext context, List<ItemStack> drops) {
        BlockState state = context.getOptional(LootContextParams.BLOCK_STATE);
        Vec3 origin = context.getOptional(LootContextParams.ORIGIN);
        if (state == null || origin == null || !state.is(AetherBlocks.BERRY_BUSH)) {
            return;
        }
        if (context.getLevel().getBlockState(BlockPos.containing(origin).below()).is(AetherTags.Blocks.ENCHANTED_GRASS)
                && context.getRandom().nextBoolean()
                && drops.stream().anyMatch(stack -> stack.is(AetherItems.BLUE_BERRY))) {
            drops.add(new ItemStack(AetherItems.BLUE_BERRY));
        }
    }

    /**
     * Doubles mob drops when a mob is killed with a full-strength hit from a {@link SkyrootWeapon}.
     */
    private static void skyrootDoubleDrops(LootContext context, List<ItemStack> drops) {
        Entity attacker = context.getOptional(LootContextParams.DIRECT_ATTACKING_ENTITY);
        Entity target = context.getOptional(LootContextParams.THIS_ENTITY);
        if (attacker instanceof LivingEntity livingAttacker && target != null
                && EquipmentUtil.isFullStrength(livingAttacker)
                && livingAttacker.getMainHandItem().getItem() instanceof SkyrootWeapon
                && !target.getType().builtInRegistryHolder().is(AetherTags.Entities.NO_SKYROOT_DOUBLE_DROPS)) {
            for (ItemStack stack : List.copyOf(drops)) {
                if (!stack.is(AetherTags.Items.NO_SKYROOT_DOUBLE_DROPS)) {
                    drops.add(stack.copy());
                }
            }
        }
    }

    /**
     * Gives a 1 in 4 chance of doubled drops when a pig-type mob is killed with a full-strength hit from a Pig Slayer.
     */
    private static void pigSlayerDrops(LootContext context, List<ItemStack> drops) {
        Entity attacker = context.getOptional(LootContextParams.DIRECT_ATTACKING_ENTITY);
        Entity target = context.getOptional(LootContextParams.THIS_ENTITY);
        if (attacker instanceof LivingEntity livingAttacker && target instanceof LivingEntity livingTarget
                && EquipmentUtil.isFullStrength(livingAttacker)
                && livingAttacker.getMainHandItem().is(AetherItems.PIG_SLAYER)
                && livingTarget.getType().builtInRegistryHolder().is(AetherTags.Entities.PIGS)
                && livingTarget.getRandom().nextInt(4) == 0) {
            for (ItemStack stack : List.copyOf(drops)) {
                if (stack.is(AetherTags.Items.PIG_DROPS)) {
                    drops.add(stack.copy());
                }
            }
        }
    }
}
