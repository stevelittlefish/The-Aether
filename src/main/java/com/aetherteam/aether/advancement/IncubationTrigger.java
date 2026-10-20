package com.aetherteam.aether.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

/**
 * Criterion trigger used for checking when an item has finished incubating.
 */
public class IncubationTrigger extends SimpleCriterionTrigger<IncubationTrigger.Instance> {
    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, (instance) -> instance.test(stack));
    }

    public record Instance(Optional<Holder<LootItemCondition>> player, Optional<ItemPredicate> item) implements SimpleInstance {
        public static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(IncubationTrigger.Instance::player),
                        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(IncubationTrigger.Instance::item))
                .apply(instance, IncubationTrigger.Instance::new));

        public static Criterion<Instance> forItem(ItemPredicate item) {
            return AetherAdvancementTriggers.INCUBATION_TRIGGER.createCriterion(new IncubationTrigger.Instance(Optional.empty(), Optional.of(item)));
        }

        public boolean test(ItemStack stack) {
            return this.item.isEmpty() || this.item.get().test(stack);
        }
    }
}
