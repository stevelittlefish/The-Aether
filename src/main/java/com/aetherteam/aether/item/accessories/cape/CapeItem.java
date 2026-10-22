package com.aetherteam.aether.item.accessories.cape;

import com.aetherteam.aether.Aether;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import javax.annotation.Nullable;

/**
 * A cape: worn in the chest slot (its {@link net.minecraft.core.component.DataComponents#EQUIPPABLE} says so), or smithed
 * onto a chestplate ({@link com.aetherteam.aether.recipe.recipes.item.CapeSmithingRecipe}). What it does is in
 * {@link com.aetherteam.aether.item.accessories.abilities.AccessoryAbilities}.
 */
public class CapeItem extends Item {
    @Nullable
    private final Identifier texture;

    /**
     * @param capeLocation The name of the cape's texture, or null for a cape that isn't drawn (the Invisibility Cloak).
     */
    public CapeItem(@Nullable String capeLocation, Properties properties) {
        super(properties);
        this.texture = capeLocation == null ? null
                : Identifier.fromNamespaceAndPath(Aether.MODID, "textures/models/accessory/capes/" + capeLocation + "_accessory.png");
    }

    @Nullable
    public Identifier getCapeTexture() {
        return this.texture;
    }
}
