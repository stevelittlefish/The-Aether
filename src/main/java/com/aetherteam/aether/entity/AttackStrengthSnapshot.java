package com.aetherteam.aether.entity;

/**
 * Holds a player's attack strength from the start of {@link net.minecraft.world.entity.player.Player#attack}.<br><br>
 * Vanilla resets the attack strength ticker before the target is hurt, so reading
 * {@link net.minecraft.world.entity.player.Player#getAttackStrengthScale(float)} from damage and {@code hurtEnemy} paths
 * always sees an empty meter. NeoForge moves the reset to the end of the attack; this snapshot gives Fabric the same value.
 */
public interface AttackStrengthSnapshot {
    /**
     * @return The attack strength scale captured when the current attack began, or {@link Float#NaN} outside an attack.
     */
    float aether$getAttackStrengthSnapshot();
}
