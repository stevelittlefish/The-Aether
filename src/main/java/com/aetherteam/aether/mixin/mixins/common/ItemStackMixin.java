package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.item.combat.abilities.weapon.ZaniteWeapon;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    /**
     * Adds a Zanite weapon's wear-based damage bonus to the modifiers the stack reports, for both combat and tooltips.
     * NeoForge did this with {@code ItemAttributeModifierEvent}; vanilla only has the static component.
     *
     * @see ZaniteWeapon#increaseDamage(ItemAttributeModifiers, ItemStack)
     */
    @ModifyExpressionValue(method = {
            "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V",
            "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object aether$addZaniteDamage(Object original) {
        ItemStack stack = (ItemStack) (Object) this;
        if (original instanceof ItemAttributeModifiers modifiers && stack.getItem() instanceof ZaniteWeapon zaniteWeapon) {
            ItemAttributeModifiers.Entry entry = zaniteWeapon.increaseDamage(modifiers, stack);
            return modifiers.withModifierAdded(entry.attribute(), entry.modifier(), entry.slot());
        }
        return original;
    }
}
