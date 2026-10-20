package com.aetherteam.aether.client.renderer.accessory;

import com.aetherteam.aether.client.AetherClient;
import com.aetherteam.aether.accessories.api.AccessoriesAPI;
import com.aetherteam.aether.accessories.api.AccessoriesContainer;
import com.aetherteam.aether.item.accessories.cape.CapeItem;
import com.aetherteam.aether.accessories.api.slot.SlotTypeReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.palette.PalettedTextureManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public final class AccessoryRendering {
    private AccessoryRendering() {
    }

    public static ItemStack getVisibleCapeAccessory(LivingEntity livingEntity) {
        ItemStack stack = getVisibleAccessory(livingEntity, CapeItem.getStaticSlotType(), 0);
        return getCapeTexture(stack) != null ? stack : ItemStack.EMPTY;
    }

    public static ItemStack getVisibleWingsAccessory(LivingEntity livingEntity) {
        ItemStack stack = getVisibleAccessory(livingEntity, com.aetherteam.aether.inventory.AetherAccessorySlots.getBackSlotType(), 0);
        return stack.is(Items.ELYTRA) ? stack : ItemStack.EMPTY;
    }

    /**
     * Gets the cape texture from a {@link CapeItem}.
     *
     * @param stack The {@link ItemStack}.
     * @return The {@link Identifier} texture from the cape.
     */
    public static Identifier getCapeTexture(ItemStack stack) {
        if (stack.getItem() instanceof CapeItem capeItem) {
            for (Map.Entry<Predicate<ItemStack>, Identifier> entry : AetherClient.CAPE_SECRETS.entrySet()) {
                if (entry.getKey().test(stack)) {
                    return entry.getValue();
                }
            }
            return capeItem.getCapeTexture();
        }
        return null;
    }

    /**
     * The humanoid armor trim texture for trimmed gloves, in the trim material's palette. Since 26.3 trims are paletted on
     * demand rather than baked into an atlas; vanilla's per-equipment palette overrides (the darker variants) aren't applied.
     */
    @Nullable
    public static PalettedTextureManager.Handle getHumanoidArmorTrimTexture(ItemStack stack) {
        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim == null) {
            return null;
        }
        Identifier baseTexture = trim.pattern().value().assetId().withPath(path -> EquipmentClientInfo.LayerType.HUMANOID.trimAssetPrefix() + "/" + path);
        return Minecraft.getInstance().getPalettedTextureManager().getOrPrepare(baseTexture, trim.material().value().paletteId());
    }

    public static RenderType getArmorTrimRenderType(ItemStack stack, PalettedTextureManager.Handle texture) {
        ArmorTrim trim = stack.get(DataComponents.TRIM);
        return RenderTypes.armorTrim(texture.textureLocation(), trim != null && trim.pattern().value().decal());
    }

    public static ItemStack getVisibleAccessory(LivingEntity livingEntity, SlotTypeReference slotType, int slotIndex) {
        var accessories = AccessoriesAPI.getAccessories(livingEntity);
        if (accessories != null) {
            AccessoriesContainer accessoriesContainer = accessories.getContainer(slotType);
            return getVisibleAccessory(accessoriesContainer, slotIndex);
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getVisibleAccessory(AccessoriesContainer accessoriesContainer, int slotIndex) {
        if (accessoriesContainer != null && accessoriesContainer.shouldRender(slotIndex)) {
            ItemStack stack = accessoriesContainer.getAccessories().getItem(slotIndex);
            ItemStack cosmeticStack = accessoriesContainer.getCosmeticAccessories().getItem(slotIndex);
            if (!cosmeticStack.isEmpty()) {
                stack = cosmeticStack;
            }
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
