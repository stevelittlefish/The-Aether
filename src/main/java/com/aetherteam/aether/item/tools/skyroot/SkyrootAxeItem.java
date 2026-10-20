package com.aetherteam.aether.item.tools.skyroot;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.SkyrootTool;
import net.minecraft.world.item.Item;

/**
 * Double drops behavior is called by {@link com.aetherteam.aether.loot.functions.DoubleDrops}.
 */
public class SkyrootAxeItem extends Item implements SkyrootTool {
    public SkyrootAxeItem(Item.Properties properties) {
        super(properties.axe(AetherItemTiers.SKYROOT, 6.0F, -3.2F));
    }
}
