package com.aetherteam.aether.world;

import com.aetherteam.aether.attachment.AetherDataAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;

/**
 * A wrapper for ServerLevelData. This is used to detach the day cycle from the Overworld and to allow the weather to be
 * set from the Aether. It gets applied to any dimension where the effects are equal to the Aether's dimension type ID.
 * A gamerule wrapper is used to prevent the overworld's weather cycle from being affected by the Aether.
 */
public class AetherLevelData extends DerivedLevelData {
    private final ServerLevel level;
    private final ServerLevelData wrapped;

    // The Aether's day time lives in its AETHER_TIME attachment, not in the game time: game time
    // drives scheduled ticks and the client's clocks and clouds, and when the Aether's frozen
    // eternal-day time stood in for it, the client's sky jumped back every second at each time
    // sync (26.3 port, 2026-10-21).
    public AetherLevelData(ServerLevel level, WorldData worldData, ServerLevelData overworldData) {
        super(worldData, overworldData);
        this.level = level;
        this.wrapped = overworldData;
    }

    /**
     * @return The overworld time in ticks.
     */
    public long getOverworldDayTime() {
        return this.wrapped.getGameTime();
    }

    /**
     * @return The Aether's day time in ticks.
     */
    public long getDayTime() {
        return this.level.getAttachedOrCreate(AetherDataAttachments.AETHER_TIME).getDayTime();
    }

    public void setDayTime(long time) {
        this.level.getAttachedOrCreate(AetherDataAttachments.AETHER_TIME).setDayTime(time);
    }
}
