package com.aetherteam.aether.item;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.entity.AttackStrengthSnapshot;
import com.aetherteam.aether.item.accessories.cape.CapeItem;
import com.aetherteam.aether.item.components.AetherDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.loader.api.FabricLoader;

import javax.annotation.Nullable;

public final class EquipmentUtil {
    /**
     * An attack is full strength if either it's a player that attacked with full attack strength, or if the entity is not a player.
     *
     * @param attacker The attacking {@link LivingEntity}.
     * @return Whether the attack was full strength, as a {@link Boolean}.
     */
    public static boolean isFullStrength(LivingEntity attacker) {
        if (!(attacker instanceof Player player)) {
            return true;
        }
        // During an attack the live meter has already been reset by vanilla; use the value from when the attack began.
        float strength = player instanceof AttackStrengthSnapshot snapshot && !Float.isNaN(snapshot.aether$getAttackStrengthSnapshot())
                ? snapshot.aether$getAttackStrengthSnapshot()
                : player.getAttackStrengthScale(1.0F);
        boolean combatifyLoaded = FabricLoader.getInstance().isModLoaded("combatify");
        return combatifyLoaded ? strength >= 1.95F : strength >= 1.0F;
    }

    /**
     * Calculates damage increase based on a base value, the amount of damage taken (maximum durability - current durability), and the stack's maximum durability.<br><br>
     * <a href="https://www.desmos.com/calculator/gmaj2zduis">See math visually.</a>
     *
     * @param stack     A {@link ItemStack} to check the durability of.
     * @param baseValue A base {@link Double} value.
     * @return The buffed {@link Double} value.
     */
    public static double calculateZaniteBuff(ItemStack stack, double baseValue) {
        return baseValue * (2.0 * ((double) stack.getDamageValue()) / ((double) stack.getMaxDamage()) + 0.5);
    }

    /**
     * The cape an entity has on: a cape in its chest slot, or the cape smithed onto its chestplate.
     *
     * @param entity The {@link LivingEntity} wearer.
     * @return The cape's {@link Item}, or null.
     */
    @Nullable
    public static Item getCape(LivingEntity entity) {
        return getCape(entity.getItemBySlot(EquipmentSlot.CHEST));
    }

    /**
     * @param stack A chest slot {@link ItemStack}.
     * @return The cape it is or carries, or null.
     */
    @Nullable
    public static Item getCape(ItemStack stack) {
        if (stack.getItem() instanceof CapeItem) {
            return stack.getItem();
        }
        Holder<Item> cape = stack.get(AetherDataComponents.CAPE);
        return cape != null ? cape.value() : null;
    }

    public static boolean hasSwetPacifyingAccessory(LivingEntity entity) {
        Item cape = getCape(entity);
        return cape != null && cape.builtInRegistryHolder().is(AetherTags.Items.PACIFIES_SWETS);
    }

    public static boolean hasSwetCape(LivingEntity entity) {
        return getCape(entity) == AetherItems.SWET_CAPE;
    }

    public static boolean hasSentryBoots(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET).is(AetherItems.SENTRY_BOOTS);
    }

    /**
     * Checks whether the entity's equipped Aether armor prevents fall damage.
     *
     * @param entity The {@link LivingEntity} wearer.
     * @return Whether fall damage should be prevented, as a {@link Boolean}.
     */
    public static boolean preventsFallDamage(LivingEntity entity) {
        return hasSentryBoots(entity) || hasFullGravititeSet(entity) || hasFullValkyrieSet(entity);
    }

    /**
     * Checks if the player is wearing the Gravitite Armor set.
     *
     * @param entity The {@link LivingEntity} wearer.
     * @return The result of the check, as a {@link Boolean}.
     */
    public static boolean hasFullGravititeSet(LivingEntity entity) {
        return hasArmorSet(entity, AetherItems.GRAVITITE_HELMET, AetherItems.GRAVITITE_CHESTPLATE, AetherItems.GRAVITITE_LEGGINGS, AetherItems.GRAVITITE_BOOTS);
    }

    /**
     * Checks if the player is wearing the Valkyrie Armor set.
     *
     * @param entity The {@link LivingEntity} wearer.
     * @return The result of the check, as a {@link Boolean}.
     */
    public static boolean hasFullValkyrieSet(LivingEntity entity) {
        return hasArmorSet(entity, AetherItems.VALKYRIE_HELMET, AetherItems.VALKYRIE_CHESTPLATE, AetherItems.VALKYRIE_LEGGINGS, AetherItems.VALKYRIE_BOOTS);
    }

    /**
     * Checks if the player is wearing the Neptune Armor set.
     *
     * @param entity The {@link LivingEntity} wearer.
     * @return The result of the check, as a {@link Boolean}.
     */
    public static boolean hasFullNeptuneSet(LivingEntity entity) {
        return hasArmorSet(entity, AetherItems.NEPTUNE_HELMET, AetherItems.NEPTUNE_CHESTPLATE, AetherItems.NEPTUNE_LEGGINGS, AetherItems.NEPTUNE_BOOTS);
    }

    /**
     * Checks if the player is wearing the Phoenix Armor set.
     *
     * @param entity The {@link LivingEntity} wearer.
     * @return The result of the check, as a {@link Boolean}.
     */
    public static boolean hasFullPhoenixSet(LivingEntity entity) {
        return hasArmorSet(entity, AetherItems.PHOENIX_HELMET, AetherItems.PHOENIX_CHESTPLATE, AetherItems.PHOENIX_LEGGINGS, AetherItems.PHOENIX_BOOTS);
    }

    /**
     * Checks if the player is wearing any Phoenix Armor.
     *
     * @param entity The {@link LivingEntity} wearer.
     * @return The result of the check, as a {@link Boolean}.
     */
    public static boolean hasAnyPhoenixArmor(LivingEntity entity) {
        return hasAnyArmor(entity, AetherItems.PHOENIX_HELMET, AetherItems.PHOENIX_CHESTPLATE, AetherItems.PHOENIX_LEGGINGS, AetherItems.PHOENIX_BOOTS);
    }

    /**
     * Checks if the player is wearing all armor pieces out of a set.
     *
     * @param entity     The {@link LivingEntity} wearer.
     * @param helmet     The helmet {@link Item}.
     * @param chestplate The chestplate {@link Item}.
     * @param leggings   The leggings {@link Item}.
     * @param boots      The boots {@link Item}.
     * @return The result of the check, as a {@link Boolean}.
     */
    private static boolean hasArmorSet(LivingEntity entity, Item helmet, Item chestplate, Item leggings, Item boots) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(helmet)
                && entity.getItemBySlot(EquipmentSlot.CHEST).is(chestplate)
                && entity.getItemBySlot(EquipmentSlot.LEGS).is(leggings)
                && entity.getItemBySlot(EquipmentSlot.FEET).is(boots);
    }

    /**
     * Checks if the player is wearing any armor piece out of a set.
     *
     * @param entity     The {@link LivingEntity} wearer.
     * @param helmet     The helmet {@link Item}.
     * @param chestplate The chestplate {@link Item}.
     * @param leggings   The leggings {@link Item}.
     * @param boots      The boots {@link Item}.
     * @return The result of the check, as a {@link Boolean}.
     */
    private static boolean hasAnyArmor(LivingEntity entity, Item helmet, Item chestplate, Item leggings, Item boots) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(helmet)
                || entity.getItemBySlot(EquipmentSlot.CHEST).is(chestplate)
                || entity.getItemBySlot(EquipmentSlot.LEGS).is(leggings)
                || entity.getItemBySlot(EquipmentSlot.FEET).is(boots);
    }
}
