package com.aetherteam.aether.item.tools.valkyrie;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ValkyrieTool;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ValkyrieHoeItem extends Item implements ValkyrieTool {
    public ValkyrieHoeItem(Item.Properties properties) {
        super(properties.hoe(AetherItemTiers.VALKYRIE, -3.0F, -0.3F));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }
}
