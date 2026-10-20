package com.aetherteam.aether.item.tools.skyroot;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.SkyrootTool;
import net.minecraft.world.item.Item;

/**
 * Double drops behavior is called by {@link com.aetherteam.aether.loot.functions.DoubleDrops}.
 */
public class SkyrootShovelItem extends Item implements SkyrootTool {
    public SkyrootShovelItem(Item.Properties properties) {
        super(properties.shovel(AetherItemTiers.SKYROOT, 1.5F, -3.0F));
    }
}
