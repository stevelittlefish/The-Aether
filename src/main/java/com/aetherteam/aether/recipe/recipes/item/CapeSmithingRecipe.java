package com.aetherteam.aether.recipe.recipes.item;

import com.aetherteam.aether.item.components.AetherDataComponents;
import com.aetherteam.aether.recipe.AetherRecipeSerializers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleSmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;

import java.util.List;
import java.util.Optional;

/**
 * A chestplate and a cape at the smithing table make the chestplate with the cape on it, the way armour trims work: one
 * recipe for every pairing, the chestplate keeping its enchantments, durability and name. No template: a smithing
 * recipe without one matches an empty template slot. A chestplate already caped takes no other
 * ({@link CapeRemovalRecipe} takes it off).
 */
public class CapeSmithingRecipe extends SimpleSmithingRecipe {
    public static final MapCodec<CapeSmithingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            Ingredient.CODEC.fieldOf("base").forGetter(o -> o.base),
            Ingredient.CODEC.fieldOf("addition").forGetter(o -> o.addition)
    ).apply(i, CapeSmithingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CapeSmithingRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.base,
            Ingredient.CONTENTS_STREAM_CODEC, o -> o.addition,
            CapeSmithingRecipe::new);

    private final Ingredient base;
    private final Ingredient addition;

    public CapeSmithingRecipe(Recipe.CommonInfo commonInfo, Ingredient base, Ingredient addition) {
        super(commonInfo);
        this.base = base;
        this.addition = addition;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input) {
        if (input.base().has(AetherDataComponents.CAPE)) {
            return ItemStack.EMPTY;
        }
        ItemStack caped = input.base().copyWithCount(1);
        caped.set(AetherDataComponents.CAPE, input.addition().typeHolder());
        return caped;
    }

    @Override
    public Optional<Ingredient> templateIngredient() {
        return Optional.empty();
    }

    @Override
    public Ingredient baseIngredient() {
        return this.base;
    }

    @Override
    public Optional<Ingredient> additionIngredient() {
        return Optional.of(this.addition);
    }

    @Override
    public RecipeSerializer<CapeSmithingRecipe> getSerializer() {
        return AetherRecipeSerializers.CAPE_SMITHING;
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(this.base, this.addition));
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new SmithingRecipeDisplay(SlotDisplay.Empty.INSTANCE, this.base.display(), this.addition.display(),
                this.base.display(), new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
    }
}
