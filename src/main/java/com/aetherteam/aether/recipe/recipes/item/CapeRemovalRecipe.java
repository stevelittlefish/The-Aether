package com.aetherteam.aether.recipe.recipes.item;

import com.aetherteam.aether.item.components.AetherDataComponents;
import com.aetherteam.aether.recipe.AetherRecipeSerializers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * A caped chestplate crafted on its own gives the chestplate back, and leaves its cape in the crafting grid.
 */
public class CapeRemovalRecipe extends CustomRecipe {
    public static final CapeRemovalRecipe INSTANCE = new CapeRemovalRecipe();
    public static final MapCodec<CapeRemovalRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CapeRemovalRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return input.ingredientCount() == 1 && caped(input) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack caped = caped(input);
        if (caped == null) {
            return ItemStack.EMPTY;
        }
        ItemStack plain = caped.copyWithCount(1);
        plain.remove(AetherDataComponents.CAPE);
        return plain;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int slot = 0; slot < input.size(); slot++) {
            Holder<Item> cape = input.getItem(slot).get(AetherDataComponents.CAPE);
            if (cape != null) {
                remaining.set(slot, new ItemStack(cape));
            }
        }
        return remaining;
    }

    private static ItemStack caped(CraftingInput input) {
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.has(AetherDataComponents.CAPE)) {
                return stack;
            }
        }
        return null;
    }

    @Override
    public RecipeSerializer<CapeRemovalRecipe> getSerializer() {
        return AetherRecipeSerializers.CAPE_REMOVAL;
    }
}
