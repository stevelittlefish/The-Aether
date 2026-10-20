package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.item.combat.abilities.weapon.WeaponAbilities;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin {
    /**
     * Stops lightning summoned by a weapon from striking its wielder, their mount or their passengers, as NeoForge's
     * {@code EntityStruckByLightningEvent} did. Guarding the call here covers entities whose {@code thunderHit} override
     * doesn't call super (pigs, villagers, creepers).
     *
     * @see WeaponAbilities#lightningTracking(Entity, LightningBolt)
     */
    @WrapWithCondition(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
    private boolean aether$spareWeaponOwner(Entity entity, ServerLevel level, LightningBolt lightning) {
        return !WeaponAbilities.lightningTracking(entity, lightning);
    }
}
