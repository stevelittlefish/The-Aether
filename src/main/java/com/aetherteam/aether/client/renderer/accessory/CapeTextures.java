package com.aetherteam.aether.client.renderer.accessory;

import com.aetherteam.aether.client.AetherClient;
import com.aetherteam.aether.item.EquipmentUtil;
import com.aetherteam.aether.item.accessories.cape.CapeItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Predicate;

public final class CapeTextures {
    private CapeTextures() {
    }

    /**
     * @param chest The chest slot {@link ItemStack}: a cape, or a chestplate with a cape smithed on.
     * @return The cape texture to draw, or null for none.
     */
    @Nullable
    public static Identifier get(ItemStack chest) {
        if (!(EquipmentUtil.getCape(chest) instanceof CapeItem capeItem)) {
            return null;
        }
        if (chest.getItem() instanceof CapeItem) {
            for (Map.Entry<Predicate<ItemStack>, Identifier> entry : AetherClient.CAPE_SECRETS.entrySet()) {
                if (entry.getKey().test(chest)) {
                    return entry.getValue();
                }
            }
        }
        return capeItem.getCapeTexture();
    }
}
