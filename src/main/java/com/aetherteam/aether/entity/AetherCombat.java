package com.aetherteam.aether.entity;

import com.aetherteam.aether.item.accessories.abilities.AccessoryAbilities;
import com.aetherteam.aether.item.combat.abilities.armor.PhoenixArmor;
import com.aetherteam.aether.item.combat.abilities.weapon.WeaponAbilities;
import com.aetherteam.aether.item.combat.loot.HolySwordItem;
import com.aetherteam.aether.item.combat.loot.PigSlayerItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Coordinates Aether combat effects around vanilla living entity damage.
 */
public final class AetherCombat {
    private AetherCombat() {
    }

    public static boolean beforeHurt(LivingEntity target, DamageSource source) {
        WeaponAbilities.stickDart(target, source);
        return AccessoryAbilities.preventMagmaDamage(target, source) || PhoenixArmor.extinguishUser(target, source);
    }

    public static float modifyIncomingDamage(LivingEntity target, DamageSource source, float amount) {
        Entity direct = source.getDirectEntity();
        float weaponAdjusted = WeaponAbilities.reduceWeaponEffectiveness(target, direct, amount);
        return WeaponAbilities.reduceArmorEffectiveness(target, direct, weaponAdjusted);
    }

    public static float modifyDamageAfterReductions(LivingEntity target, DamageSource source, float damage) {
        float holyAdjusted = HolySwordItem.onLivingDamage(target, source, damage);
        return PigSlayerItem.onLivingDamage(target, source, holyAdjusted);
    }
}
