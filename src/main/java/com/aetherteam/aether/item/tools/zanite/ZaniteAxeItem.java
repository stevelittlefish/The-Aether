package com.aetherteam.aether.item.tools.zanite;

import com.aetherteam.aether.item.combat.AetherItemTiers;
import com.aetherteam.aether.item.tools.abilities.ZaniteTool;
import net.minecraft.world.item.Item;

/**
 * Zanite mining speed boost behavior is called by {@link com.aetherteam.aether.item.tools.abilities.ToolAbilities#handleZaniteToolAbility}.
 */
public class ZaniteAxeItem extends Item implements ZaniteTool {
    public ZaniteAxeItem(Item.Properties properties) {
        super(properties.axe(AetherItemTiers.ZANITE, 6.0F, -3.1F));
    }
}
