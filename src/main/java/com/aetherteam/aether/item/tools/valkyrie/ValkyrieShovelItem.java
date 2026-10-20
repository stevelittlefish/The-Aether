package com.aetherteam.aether.item.tools.valkyrie;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ValkyrieTool;
import net.minecraft.world.item.Item;

public class ValkyrieShovelItem extends Item implements ValkyrieTool {
    public ValkyrieShovelItem(Item.Properties properties) {
        super(properties.shovel(AetherItemTiers.VALKYRIE, 1.5F, -3.3F));
    }
}
