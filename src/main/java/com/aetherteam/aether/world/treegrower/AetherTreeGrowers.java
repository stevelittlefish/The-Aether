package com.aetherteam.aether.world.treegrower;

import com.aetherteam.aether.data.resources.registries.AetherConfiguredFeatures;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;


public class AetherTreeGrowers {
    public static final TreeGrower SKYROOT = new TreeGrower(
            "skyroot",
            WeightedList.of(AetherConfiguredFeatures.SKYROOT_TREE_CONFIGURATION),
            WeightedList.of(),
            WeightedList.of(),
            AetherConfiguredFeatures.SKYROOT_TREE_CONFIGURATION
    );

    public static final TreeGrower GOLDEN_OAK = new TreeGrower(
            "golden_oak",
            WeightedList.of(AetherConfiguredFeatures.GOLDEN_OAK_TREE_CONFIGURATION),
            WeightedList.of(),
            WeightedList.of(),
            AetherConfiguredFeatures.GOLDEN_OAK_TREE_CONFIGURATION
    );
}
