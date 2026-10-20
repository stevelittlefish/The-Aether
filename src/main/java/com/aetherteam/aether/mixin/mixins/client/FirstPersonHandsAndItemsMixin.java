package com.aetherteam.aether.mixin.mixins.client;

import com.aetherteam.aether.accessories.impl.AccessoryUsingEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class FirstPersonHandsAndItemsMixin {
    @WrapOperation(method = "tick(Lnet/minecraft/client/player/LocalPlayer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getOffhandItem()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack aether$showUsedAccessoryShield(LocalPlayer player, Operation<ItemStack> original) {
        if (player instanceof AccessoryUsingEntity usingEntity && usingEntity.aether$isUsingAccessory()) {
            return player.getUseItem();
        }
        return original.call(player);
    }
}
