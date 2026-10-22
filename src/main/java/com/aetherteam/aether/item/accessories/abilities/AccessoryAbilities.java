package com.aetherteam.aether.item.accessories.abilities;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.attachment.AetherDataAttachments;
import com.aetherteam.aether.item.AetherItems;
import com.aetherteam.aether.item.EquipmentUtil;
import com.aetherteam.aether.util.EntityMotionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * What capes and charms do. A cape works when worn in the chest slot or smithed onto a chestplate
 * ({@link EquipmentUtil#getCape(LivingEntity)}), a charm when held in the off hand. There are no accessory slots: the
 * base game's inventory stays as it is (Aether lite, 2026-10-22), and nothing here patches vanilla code.
 */
public final class AccessoryAbilities {
    private static final Identifier AGILITY_STEP_HEIGHT = Identifier.fromNamespaceAndPath(Aether.MODID, "agility_cape_step_height");
    private static final Identifier ZANITE_BREAK_SPEED = Identifier.fromNamespaceAndPath(Aether.MODID, "zanite_ring_break_speed");
    private static final FreezingAccessory ICE_RING = new FreezingAccessory() { };

    private AccessoryAbilities() {
    }

    /**
     * Runs each server tick for each player.
     */
    public static void tick(ServerPlayer player) {
        Item cape = EquipmentUtil.getCape(player);
        ItemStack charm = player.getOffhandItem();

        setModifier(player.getAttribute(Attributes.STEP_HEIGHT), AGILITY_STEP_HEIGHT, cape == AetherItems.AGILITY_CAPE && !player.isShiftKeyDown()
                ? new AttributeModifier(AGILITY_STEP_HEIGHT, 0.5, AttributeModifier.Operation.ADD_VALUE) : null);
        // Mines 1.4 times as fast when new, up to about 1.73 times as it wears out.
        setModifier(player.getAttribute(Attributes.BLOCK_BREAK_SPEED), ZANITE_BREAK_SPEED, charm.is(AetherItems.ZANITE_RING)
                ? new AttributeModifier(ZANITE_BREAK_SPEED, 0.4 + charm.getDamageValue() / (charm.getMaxDamage() * 3.0), AttributeModifier.Operation.ADD_MULTIPLIED_BASE) : null);

        if (cape == AetherItems.VALKYRIE_CAPE || charm.is(AetherItems.GOLDEN_FEATHER)) {
            keepEffect(player, MobEffects.SLOW_FALLING);
        }
        if (cape == AetherItems.INVISIBILITY_CLOAK) {
            keepEffect(player, MobEffects.INVISIBILITY);
        }
        if (charm.is(AetherItems.IRON_BUBBLE) && player.isUnderWater()) {
            player.setAirSupply(player.getMaxAirSupply());
        }
        if (charm.is(AetherItems.REGENERATION_STONE) && player.tickCount % 50 == 0 && player.getHealth() < player.getMaxHealth()) {
            player.heal(1.0F);
        }
        if (charm.is(AetherItems.ICE_RING) && !player.isInLiquid()) {
            ICE_RING.freezeTick(player, charm, EquipmentSlot.OFFHAND);
        }
        if (charm.is(AetherItems.SHIELD_OF_REPULSION)) {
            repel(player, charm);
        }
    }

    private static void setModifier(@Nullable AttributeInstance attribute, Identifier id, @Nullable AttributeModifier modifier) {
        if (attribute == null) {
            return;
        }
        if (modifier != null) {
            attribute.addOrUpdateTransientModifier(modifier);
        } else if (attribute.hasModifier(id)) {
            attribute.removeModifier(id);
        }
    }

    // Kept topped up rather than permanent, so it wears off a few seconds after the cape or charm comes off.
    // A potion of the same effect (not ambient) is left alone.
    private static void keepEffect(Player player, Holder<MobEffect> effect) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || (current.isAmbient() && current.getDuration() < 20)) {
            player.addEffect(new MobEffectInstance(effect, 60, 0, true, false, true));
        }
    }

    /**
     * The Shield of Repulsion sends back a projectile about to hit a player standing still with it in their off hand,
     * for 1 durability. It looks a tick ahead (where the projectile will move next) instead of patching vanilla's
     * projectile hit, as the Aether did.
     */
    private static void repel(ServerPlayer player, ItemStack shield) {
        if (player.getAttachedOrCreate(AetherDataAttachments.AETHER_PLAYER).isMoving() && !EntityMotionUtil.isStationary(player.getDeltaMovement())) {
            return;
        }
        AABB body = player.getBoundingBox().inflate(0.5);
        for (Projectile projectile : player.level().getEntitiesOfClass(Projectile.class, body.inflate(8.0),
                p -> p.getType().builtInRegistryHolder().is(AetherTags.Entities.DEFLECTABLE_PROJECTILES) && p.getOwner() != player)) {
            Vec3 from = projectile.position();
            Vec3 to = from.add(projectile.getDeltaMovement());
            if (!body.contains(from) && body.clip(from, to).isEmpty()) {
                continue;
            }
            Entity owner = projectile.getOwner();
            projectile.deflect(ProjectileDeflection.REVERSE, player, owner != null ? EntityReference.of(owner) : EntityReference.of(projectile), false, 1.0);
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(0.25));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, player.getSoundSource(), 1.0F, 1.2F);
            shield.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            if (shield.isEmpty()) {
                return;
            }
        }
    }

    /**
     * The Zanite Ring wears as it mines: 1 durability for one block in six.
     */
    public static void onBlockBroken(Player player, Level level, BlockPos pos, BlockState state) {
        ItemStack charm = player.getOffhandItem();
        if (charm.is(AetherItems.ZANITE_RING) && state.getDestroySpeed(level, pos) > 0 && player.getRandom().nextInt(6) == 0) {
            charm.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
        }
    }

    public static boolean preventMagmaDamage(LivingEntity entity, DamageSource source) {
        return source == entity.level().damageSources().hotFloor() && entity.getOffhandItem().is(AetherItems.ICE_RING);
    }
}
