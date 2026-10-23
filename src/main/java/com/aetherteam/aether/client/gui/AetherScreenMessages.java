package com.aetherteam.aether.client.gui;

import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.world.AetherTravelState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class AetherScreenMessages {
    private AetherScreenMessages() {
    }

    public static void drawAetherTravelMessage(Screen screen, GuiGraphicsExtractor guiGraphics) {
        if (!(screen instanceof LevelLoadingScreen || screen instanceof ProgressScreen)) {
            AetherTravelState.displayAetherTravel = false;
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !AetherTravelState.displayAetherTravel) {
            return;
        }

        Component message = AetherTravelState.playerLeavingAether
                ? Component.translatable("gui.aether.descending")
                : Component.translatable("gui.aether.ascending");
        guiGraphics.centeredText(minecraft.font, message, screen.width / 2, AetherConfig.CLIENT.portal_text_y.get(), 0xFFFFFFFF);
    }
}
