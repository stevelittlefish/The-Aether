package com.aetherteam.aether.item.tools.zanite;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ZaniteTool;
import net.minecraft.world.item.Item;

/**
 * Zanite mining speed boost behavior is called by {@link com.aetherteam.aether.item.tools.abilities.ToolAbilities#handleZaniteToolAbility}.
 */
public class ZaniteShovelItem extends Item implements ZaniteTool {
    public ZaniteShovelItem(Item.Properties properties) {
        super(properties.shovel(AetherItemTiers.ZANITE, 1.5F, -3.0F));
    }
}
