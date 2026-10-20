package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.attachment.AetherTimeAttachment;
import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.aetherteam.aether.world.ScaledClockInstance;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.clock.ClockInstance;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnvironmentAttributeSystem.class)
public class EnvironmentAttributeSystemMixin {
    /**
     * Slows the Aether's timelines (sky, light) by its day length multiplier, so a day there lasts longer than the clock it follows.
     */
    @Redirect(
        method = "addDynamicLayers(Lnet/minecraft/world/attribute/EnvironmentAttributeSystem$Builder;Lnet/minecraft/world/level/Level;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;clockManager()Lnet/minecraft/world/clock/ClockManager;")
    )
    private static ClockManager aether$useScaledClockForAetherTimelines(Level level) {
        ClockManager clockManager = level.clockManager();
        if (!level.dimension().equals(AetherDimensions.AETHER_LEVEL)) {
            return clockManager;
        }
        return clock -> {
            ClockInstance instance = clockManager.getInstance(clock);
            int multiplier = AetherTimeAttachment.getTicksPerDayMultiplier();
            return multiplier > 1 ? new ScaledClockInstance(instance, multiplier) : instance;
        };
    }
}
