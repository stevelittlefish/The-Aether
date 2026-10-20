package com.aetherteam.aether.world;

import net.minecraft.world.clock.ClockInstance;

/**
 * A clock running {@code multiplier} times slower than the one it wraps, for the Aether's longer days.
 */
public record ScaledClockInstance(ClockInstance instance, int multiplier) implements ClockInstance {
    @Override
    public long totalTicks() {
        return Math.floorDiv(this.instance.totalTicks(), this.multiplier);
    }

    @Override
    public float partialTick() {
        // The ticks lost to the division above, plus this tick's fraction, as a fraction of one scaled tick.
        return (Math.floorMod(this.instance.totalTicks(), this.multiplier) + this.instance.partialTick()) / this.multiplier;
    }

    @Override
    public float rate() {
        return this.instance.rate() / this.multiplier;
    }

    @Override
    public boolean isPaused() {
        return this.instance.isPaused();
    }
}
