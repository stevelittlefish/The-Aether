package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.item.accessories.abilities.AccessoryAbilities;
import com.aetherteam.aether.item.tools.abilities.ToolAbilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockPlayerDestroyMixin {
    // Fabric's after-break event does not expose the original tool stack required by the Holystone ability.
    @Inject(method = "playerDestroy", at = @At("TAIL"))
    private void aether$onPlayerDestroy(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if (!level.isClientSide()) {
            AccessoryAbilities.damageZaniteRing(player, level, state, pos);
            AccessoryAbilities.damageZanitePendant(player, level, state, pos);
            ToolAbilities.handleHolystoneToolAbility(player, level, pos, stack, state);
        }
    }
}
