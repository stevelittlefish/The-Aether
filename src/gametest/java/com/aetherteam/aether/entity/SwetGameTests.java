package com.aetherteam.aether.entity;

import com.aetherteam.aether.entity.monster.Swet;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public final class SwetGameTests {
    /**
     * A Swet that has swallowed a player keeps them while they sneak, but lets go when it dissolves.
     * Holding on to a dissolved Swet left a player stuck in the air, shaking (2026-10-23).
     */
    @GameTest(maxTicks = 40)
    public void dissolvedSwetLetsGoOfSneakingPrey(GameTestHelper helper) {
        Swet swet = helper.spawn(AetherEntityTypes.BLUE_SWET, new BlockPos(1, 1, 1));
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(swet.position());
        swet.consumePassenger(player);
        helper.assertTrue(player.getVehicle() == swet, "the swet did not swallow the player");

        player.setShiftKeyDown(true);
        player.stopRiding();
        helper.assertTrue(player.getVehicle() == swet, "sneaking got the player out of a hostile swet");

        swet.discard();
        helper.assertTrue(player.getVehicle() == null, "the player still rides a dissolved swet");
        helper.succeed();
    }
}
