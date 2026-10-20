package com.aetherteam.aether.item.tools.valkyrie;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ValkyrieTool;
import net.minecraft.world.item.Item;

public class ValkyrieAxeItem extends Item implements ValkyrieTool {
    public ValkyrieAxeItem(Item.Properties properties) {
        super(properties.axe(AetherItemTiers.VALKYRIE, 5.0F, -3.3F));
    }
}
