package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.attachment.AetherDataAttachments;
import com.aetherteam.aether.entity.AetherMounting;
import com.aetherteam.aether.entity.AttackStrengthSnapshot;
import com.aetherteam.aether.item.tools.abilities.ToolAbilities;
import com.aetherteam.aether.world.AetherTravelController;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements AttackStrengthSnapshot {
    @Unique
    private float aether$attackStrengthSnapshot = Float.NaN;

    @Shadow
    protected abstract boolean wantsToStopRiding();

    @Shadow
    public abstract float getAttackStrengthScale(float adjustTicks);

    @Override
    public float aether$getAttackStrengthSnapshot() {
        return this.aether$attackStrengthSnapshot;
    }

    /**
     * Records the attack strength before vanilla resets it, so full-strength weapon abilities can read it while the target is hurt.
     *
     * @see AttackStrengthSnapshot
     */
    @Inject(at = @At("HEAD"), method = "attack(Lnet/minecraft/world/entity/Entity;)V")
    private void aether$captureAttackStrength(Entity target, CallbackInfo ci) {
        this.aether$attackStrengthSnapshot = this.getAttackStrengthScale(1.0F);
    }

    @Inject(at = @At("RETURN"), method = "attack(Lnet/minecraft/world/entity/Entity;)V")
    private void aether$releaseAttackStrength(Entity target, CallbackInfo ci) {
        this.aether$attackStrengthSnapshot = Float.NaN;
    }

    /**
     * Preserves crouch input before vanilla mount handling can clear it.
     *
     * @param ci The {@link CallbackInfo} for the void method return.
     */
    @Inject(at = @At(value = "HEAD"), method = "rideTick()V")
    private void rideTickHead(CallbackInfo ci, @Share("wantsToStopRiding") LocalBooleanRef wantsToStopRiding) {
        Player player = (Player) (Object) this;
        wantsToStopRiding.set(this.wantsToStopRiding());
        AetherMounting.handleRideTickStart(player);
    }

    @Inject(at = @At(value = "TAIL"), method = "rideTick()V")
    private void rideTickTail(CallbackInfo ci, @Share("wantsToStopRiding") LocalBooleanRef wantsToStopRiding) {
        AetherMounting.handleRideTickEnd((Player) (Object) this, wantsToStopRiding.get());
    }

    /**
     * Runs Aether player attachment and dimension travel hooks from the vanilla player tick.
     */
    @Inject(at = @At("TAIL"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        player.getAttachedOrCreate(AetherDataAttachments.AETHER_PLAYER).onUpdate(player);
        AetherTravelController.travelling(player);
    }

    @ModifyReturnValue(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", at = @At("RETURN"))
    private float aether$modifyBreakSpeed(float original, BlockState state) {
        return ToolAbilities.modifyBreakSpeed((Player) (Object) this, state, original);
    }
}
