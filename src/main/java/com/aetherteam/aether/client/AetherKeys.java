package com.aetherteam.aether.client;

import com.aetherteam.aether.Aether;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class AetherKeys {
    private static final KeyMapping.Category AETHER_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(Aether.MODID, "category"));
    public final static KeyMapping GRAVITITE_JUMP_ABILITY = new KeyMapping("key.aether.gravitite_jump_ability.desc", InputConstants.KEY_SPACE, AETHER_CATEGORY);

    public static void registerKeyMappings() {
        KeyMappingHelper.registerKeyMapping(GRAVITITE_JUMP_ABILITY);
    }
}
