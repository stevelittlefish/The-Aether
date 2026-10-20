package com.aetherteam.aether.datagen;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.data.resources.registries.AetherBiomes;
import com.aetherteam.aether.data.resources.registries.AetherConfiguredFeatures;
import com.aetherteam.aether.data.resources.registries.AetherDamageTypes;
import com.aetherteam.aether.data.resources.registries.AetherDensityFunctions;
import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.aetherteam.aether.data.resources.registries.AetherJukeboxSongs;
import com.aetherteam.aether.data.resources.registries.AetherMoaTypes;
import com.aetherteam.aether.data.resources.registries.AetherNoiseSettings;
import com.aetherteam.aether.data.resources.registries.AetherNoises;
import com.aetherteam.aether.data.resources.registries.AetherPlacedFeatures;
import com.aetherteam.aether.data.resources.registries.AetherStructureProcessorLists;
import com.aetherteam.aether.data.resources.registries.AetherStructureSets;
import com.aetherteam.aether.data.resources.registries.AetherStructures;
import com.aetherteam.aether.data.resources.registries.AetherTrimMaterials;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Writes the Aether's dynamic registry entries from their bootstraps. Run with {@code gradle runDatagen}.
 */
public class AetherDataGenerator implements DataGeneratorEntrypoint {
    private static final List<ResourceKey<? extends Registry<?>>> REGISTRIES = List.of(
            Registries.DIMENSION_TYPE, Registries.LEVEL_STEM, Registries.FEATURE, Registries.PLACED_FEATURE,
            Registries.DENSITY_FUNCTION, Registries.NOISE, Registries.NOISE_SETTINGS, Registries.BIOME,
            Registries.PROCESSOR_LIST, Registries.STRUCTURE, Registries.STRUCTURE_SET, Registries.DAMAGE_TYPE,
            Registries.TRIM_MATERIAL, Registries.JUKEBOX_SONG, AetherMoaTypes.MOA_TYPE_REGISTRY_KEY);

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder
                .add(Registries.DIMENSION_TYPE, AetherDimensions::bootstrapDimensionType)
                .add(Registries.FEATURE, AetherConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, AetherPlacedFeatures::bootstrap)
                .add(Registries.DENSITY_FUNCTION, AetherDensityFunctions::bootstrap)
                .add(Registries.NOISE, AetherNoises::bootstrap)
                .add(Registries.NOISE_SETTINGS, AetherNoiseSettings::bootstrap)
                .add(Registries.BIOME, AetherBiomes::bootstrap)
                .add(Registries.LEVEL_STEM, AetherDimensions::bootstrapLevelStem)
                .add(Registries.PROCESSOR_LIST, AetherStructureProcessorLists::bootstrap)
                .add(Registries.STRUCTURE, AetherStructures::bootstrap)
                .add(Registries.STRUCTURE_SET, AetherStructureSets::bootstrap)
                .add(Registries.DAMAGE_TYPE, AetherDamageTypes::bootstrap)
                .add(Registries.TRIM_MATERIAL, AetherTrimMaterials::bootstrap)
                .add(Registries.JUKEBOX_SONG, AetherJukeboxSongs::bootstrap)
                .add(AetherMoaTypes.MOA_TYPE_REGISTRY_KEY, AetherMoaTypes::bootstrap);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.createPack().addProvider(AetherRegistryProvider::new);
    }

    private static class AetherRegistryProvider extends FabricDynamicRegistryProvider {
        AetherRegistryProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            for (ResourceKey<? extends Registry<?>> registry : REGISTRIES) {
                addAether(registries, registry, entries);
            }
        }

        private static <T> void addAether(HolderLookup.Provider registries, ResourceKey<? extends Registry<?>> registry, Entries entries) {
            @SuppressWarnings("unchecked")
            ResourceKey<? extends Registry<T>> key = (ResourceKey<? extends Registry<T>>) registry;
            registries.lookupOrThrow(key).listElements()
                    .filter(holder -> holder.key().identifier().getNamespace().equals(Aether.MODID))
                    .forEach(holder -> entries.add(holder.key(), holder.value()));
        }

        @Override
        public String getName() {
            return "Aether dynamic registries";
        }
    }
}
