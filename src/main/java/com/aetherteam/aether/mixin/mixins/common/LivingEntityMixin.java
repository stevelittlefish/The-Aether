package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.entity.AetherBossCombatRules;
import com.aetherteam.aether.entity.AetherCombat;
import com.aetherteam.aether.item.EquipmentUtil;
import com.aetherteam.aether.item.combat.abilities.armor.GravititeArmor;
import com.aetherteam.aether.item.combat.abilities.armor.NeptuneArmor;
import com.aetherteam.aether.item.combat.abilities.armor.PhoenixArmor;
import com.aetherteam.aether.item.combat.abilities.armor.ValkyrieArmor;
import com.aetherteam.aether.item.combat.loot.CloudStaffItem;
import com.aetherteam.aether.world.AetherTravelController;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.item.component.SwingAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private boolean aether$trackingDeathDrops;

    /**
     * Handles vertical swimming for Phoenix Armor in lava without being affected by the upwards speed debuff from lava.
     *
     * @param ci The {@link CallbackInfo} for the void method return.
     * @see PhoenixArmor#boostVerticalLavaSwimming(LivingEntity)
     */
    @Inject(
        method = "travelInLava(Lnet/minecraft/world/phys/Vec3;DZD)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            shift = At.Shift.AFTER
        )
    )
    private void aether$boostVerticalLavaSwimming(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        PhoenixArmor.boostVerticalLavaSwimming(livingEntity);
    }

    @WrapWithCondition(
        method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;dealDefaultKnockback(Lnet/minecraft/world/damagesource/DamageSource;FZ)V"
        )
    )
    private boolean aether$shouldApplyDefaultKnockback(LivingEntity instance, DamageSource source, float amount, boolean blocked) {
        return AetherBossCombatRules.shouldApplyKnockback(instance, source);
    }

    @Inject(method = "applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F", at = @At("HEAD"), cancellable = true)
    private void aether$preventSliderShieldBlock(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (AetherBossCombatRules.bypassesShieldBlocking(source)) {
            cir.setReturnValue(amount);
        }
    }

    @Inject(method = "dropAllDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At("HEAD"))
    private void aether$beginTrackingDeathDrops(ServerLevel level, DamageSource source, CallbackInfo ci) {
        this.aether$trackingDeathDrops = true;
    }

    @Inject(method = "dropAllDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At("TAIL"))
    private void aether$stopTrackingDeathDrops(ServerLevel level, DamageSource source, CallbackInfo ci) {
        this.aether$trackingDeathDrops = false;
    }

    // Death drops are created here (by Inventory#dropAll) since 26.3, rather than through drop(ItemStack, boolean, boolean).
    @Inject(method = "createItemStackToDrop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("RETURN"))
    private void aether$trackPlayerDeathDrop(ItemStack stack, boolean randomly, boolean thrownFromHand, CallbackInfoReturnable<ItemEntity> cir) {
        if (this.aether$trackingDeathDrops) {
            AetherTravelController.trackPlayerDeathDrop((LivingEntity) (Object) this, cir.getReturnValue());
        }
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void aether$applyArmorTickAbilities(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        ValkyrieArmor.handleFlight(livingEntity);
        NeptuneArmor.boostWaterSwimming(livingEntity);
        PhoenixArmor.boostLavaSwimming(livingEntity);
        PhoenixArmor.damageArmor(livingEntity);
    }

    @Inject(method = "jumpFromGround()V", at = @At("TAIL"))
    private void aether$boostJump(CallbackInfo ci) {
        GravititeArmor.boostedJump((LivingEntity) (Object) this);
    }

    @Inject(method = "causeFallDamage(DFLnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
    private void aether$cancelFallDamage(double fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (EquipmentUtil.preventsFallDamage((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
    private void aether$beforeHurt(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (AetherCombat.beforeHurt((LivingEntity) (Object) this, source)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float aether$modifyIncomingDamage(float amount, ServerLevel level, DamageSource source) {
        return AetherCombat.modifyIncomingDamage((LivingEntity) (Object) this, source, amount);
    }

    /**
     * Adds weapon bonus damage after armor and enchantment reductions, where NeoForge's {@code LivingDamageEvent.Pre} ran.
     *
     * @see AetherCombat#modifyDamageAfterReductions(LivingEntity, DamageSource, float)
     */
    @ModifyExpressionValue(method = "actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float aether$modifyDamageAfterReductions(float damage, ServerLevel level, DamageSource source, float amount) {
        return AetherCombat.modifyDamageAfterReductions((LivingEntity) (Object) this, source, damage);
    }

    /**
     * Calls {@link CloudStaffItem#onEntitySwing(ItemStack, LivingEntity, InteractionHand)}, which NeoForge called from here,
     * so swinging the Cloud Staff makes its minions shoot.
     */
    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/component/SwingAnimation;Z)Z", at = @At("HEAD"), cancellable = true)
    private void aether$onEntitySwing(InteractionHand hand, SwingAnimation animation, boolean sendToSwingingEntity, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack stack = entity.getItemInHand(hand);
        if (stack.getItem() instanceof CloudStaffItem cloudStaff && cloudStaff.onEntitySwing(stack, entity, hand)) {
            cir.setReturnValue(false);
        }
    }

}
