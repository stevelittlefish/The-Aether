package com.aetherteam.aether.client.gui;

import com.aetherteam.aether.integration.jei.AetherJeiBridge;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

public final class AetherScreenController {
    private static final boolean JEI_LOADED = FabricLoader.getInstance().isModLoaded("jei");

    private AetherScreenController() {
    }

    public static void afterInit(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
        ScreenEvents.afterExtract(screen).register((currentScreen, guiGraphics, mouseX, mouseY, tickDelta) ->
                renderScreenOverlay(currentScreen, guiGraphics));
    }

    private static void renderScreenOverlay(Screen currentScreen, GuiGraphicsExtractor guiGraphics) {
        logJeiOverlayState(currentScreen);
        AetherScreenMessages.drawAetherTravelMessage(currentScreen, guiGraphics);
    }


    private static void logJeiOverlayState(Screen screen) {
        if (!JEI_LOADED) {
            return;
        }
        AetherJeiBridge.logVisibleOverlayState(screen);
    }
}
