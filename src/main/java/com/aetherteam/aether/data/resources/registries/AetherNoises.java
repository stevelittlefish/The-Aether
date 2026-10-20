package com.aetherteam.aether.data.resources.registries;

import com.aetherteam.aether.Aether;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class AetherNoises {
    public static final ResourceKey<NormalNoise> TEMPERATURE = createKey("temperature");
    public static final ResourceKey<NormalNoise> VEGETATION = createKey("vegetation");

    private static ResourceKey<NormalNoise> createKey(String name) {
        return ResourceKey.create(Registries.NOISE, Identifier.fromNamespaceAndPath(Aether.MODID, name));
    }

    public static void bootstrap(BootstrapContext<NormalNoise> context) {
        register(context, TEMPERATURE, -8, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
        register(context, VEGETATION, -7, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
    }

    public static void register(BootstrapContext<NormalNoise> context, ResourceKey<NormalNoise> key, int firstOctave, double firstAmplitude, double... amplitudes) {
        double[] allAmplitudes = new double[amplitudes.length + 1];
        allAmplitudes[0] = firstAmplitude;
        System.arraycopy(amplitudes, 0, allAmplitudes, 1, amplitudes.length);
        context.register(key, NormalNoise.createParity(firstOctave, allAmplitudes));
    }
}
