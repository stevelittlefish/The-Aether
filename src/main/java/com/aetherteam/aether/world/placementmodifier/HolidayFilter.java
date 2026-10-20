package com.aetherteam.aether.world.placementmodifier;

import com.aetherteam.aether.AetherConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;

import java.util.Calendar;

/**
 * A {@link PlacementFilter} to prevent limit generation of this feature to the holidays, dependent on certain configs.
 */
public class HolidayFilter implements PlacementFilter {
    public static final MapCodec<HolidayFilter> CODEC = MapCodec.unit(HolidayFilter::new);

    @Override
    public boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        Calendar calendar = Calendar.getInstance();
        boolean isChristmas = calendar.get(Calendar.MONTH) == Calendar.DECEMBER || calendar.get(Calendar.MONTH) == Calendar.JANUARY;
        return AetherConfig.SERVER.generate_holiday_tree_always.get() || (AetherConfig.SERVER.generate_holiday_tree_seasonally.get() && isChristmas);
    }

    @Override
    public MapCodec<HolidayFilter> codec() {
        return CODEC;
    }
}
