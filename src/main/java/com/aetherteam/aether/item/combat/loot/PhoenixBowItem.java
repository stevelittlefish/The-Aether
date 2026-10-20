package com.aetherteam.aether.item.combat.loot;

import com.aetherteam.aether.attachment.AetherDataAttachments;
import com.aetherteam.aether.attachment.PhoenixArrowAttachment;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class PhoenixBowItem extends BowItem {
    public PhoenixBowItem(Item.Properties properties) {
        super(properties);
    }

    /**
     * Vanilla has no equivalent of NeoForge's {@code customArrow} hook, so arrows are marked as they're created.
     */
    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean isCrit) {
        Projectile projectile = super.createProjectile(level, shooter, weaponStack, projectileStack, isCrit);
        return projectile instanceof AbstractArrow arrow ? this.customArrow(arrow, projectileStack, weaponStack) : projectile;
    }

    /**
     * Marks any Arrow shot from the bow as a Phoenix Arrow with a default fire infliction time of 20 seconds, and 40 seconds if the bow has Flame.<br><br>
     * This uses {@link PhoenixArrowAttachment#setPhoenixArrow(boolean)} and {@link PhoenixArrowAttachment#setFireTime(int)} to track these values.
     *
     * @param arrow The {@link AbstractArrow} created by the Bow.
     * @return The original {@link AbstractArrow} (the Phoenix Bow doesn't modify it).
     */
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        var data = arrow.getAttachedOrCreate(AetherDataAttachments.PHOENIX_ARROW);
        data.setPhoenixArrow(true);
        int defaultTime = 20;
        if (arrow.getOwner() instanceof LivingEntity livingEntity && EnchantmentHelper.getEnchantmentLevel(livingEntity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FLAME), livingEntity) > 0) {
            defaultTime = 40;
        }
        data.setFireTime(defaultTime);
        return arrow;
    }
}
