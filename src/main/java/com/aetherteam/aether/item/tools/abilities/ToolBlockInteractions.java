package com.aetherteam.aether.item.tools.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public final class ToolBlockInteractions {
    private static final ToolInteraction AXE_STRIP = new ToolInteraction(ToolAbilities.ToolAction.AXE_STRIP, SoundEvents.AXE_STRIP.value());
    private static final ToolInteraction SHOVEL_FLATTEN = new ToolInteraction(ToolAbilities.ToolAction.SHOVEL_FLATTEN, SoundEvents.SHOVEL_FLATTEN.value());
    private static final ToolInteraction HOE_TILL = new ToolInteraction(ToolAbilities.ToolAction.HOE_TILL, SoundEvents.HOE_TILL.value());

    private ToolBlockInteractions() {
    }

    public static InteractionResult interact(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        ToolInteraction interaction = getInteraction(stack);
        if (interaction == null) {
            return InteractionResult.PASS;
        }

        UseOnContext context = new UseOnContext(player, hand, hitResult);
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockState modified = ToolAbilities.setupItemAbilities(level, pos, state, interaction.action());
        if (modified == state) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            level.setBlock(pos, modified, 11);
            level.playSound(null, pos, interaction.sound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            if (interaction.action() == ToolAbilities.ToolAction.AXE_STRIP) {
                ToolAbilities.stripGoldenOak(level, state, stack, interaction.action(), context);
            }
            stack.hurtAndBreak(1, player, hand);
        }

        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, modified));
        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    private static ToolInteraction getInteraction(ItemStack stack) {
        // Tools are told apart by the vanilla block transformer they carry: axes, shovels and hoes are no longer classes.
        Holder<BlockTransformer> transformer = stack.get(DataComponents.BLOCK_TRANSFORMER);
        if (transformer == null) {
            return null;
        }
        if (transformer.is(BlockTransformers.AXE)) {
            return AXE_STRIP;
        }
        if (transformer.is(BlockTransformers.SHOVEL)) {
            return SHOVEL_FLATTEN;
        }
        if (transformer.is(BlockTransformers.HOE)) {
            return HOE_TILL;
        }
        return null;
    }

    private record ToolInteraction(ToolAbilities.ToolAction action, SoundEvent sound) {
    }
}
