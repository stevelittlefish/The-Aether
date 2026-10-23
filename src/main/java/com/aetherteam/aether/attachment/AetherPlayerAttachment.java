package com.aetherteam.aether.attachment;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.block.portal.PortalClientUtil;
import com.aetherteam.aether.effect.AetherEffects;
import com.aetherteam.aether.entity.AetherEntityTypes;
import com.aetherteam.aether.entity.miscellaneous.CloudMinion;
import com.aetherteam.aether.entity.miscellaneous.Parachute;
import com.aetherteam.aether.entity.passive.Aerbunny;
import com.aetherteam.aether.item.AetherItems;
import com.aetherteam.aether.item.EquipmentUtil;
import com.aetherteam.aether.item.miscellaneous.ParachuteItem;
import com.aetherteam.aether.network.packet.AetherPlayerSyncPacket;
import com.aetherteam.aether.network.packet.clientbound.CloudMinionPacket;
import com.aetherteam.aether.network.packet.clientbound.RemountAerbunnyPacket;
import com.aetherteam.aether.network.packet.SyncPacket;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import com.aetherteam.aether.network.AetherPacketSender;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Fabric attachment state for handling {@link Player} behavior for the Aether.
 */
public class AetherPlayerAttachment implements AttachmentSyncable {
    public static final String HITTING_SYNC_KEY = "setHitting";
    public static final String MOVING_SYNC_KEY = "setMoving";
    public static final String JUMPING_SYNC_KEY = "setJumping";
    public static final String GRAVITITE_JUMP_ACTIVE_SYNC_KEY = "setGravititeJumpActive";
    public static final String GOLDEN_DART_COUNT_SYNC_KEY = "setGoldenDartCount";
    public static final String POISON_DART_COUNT_SYNC_KEY = "setPoisonDartCount";
    public static final String ENCHANTED_DART_COUNT_SYNC_KEY = "setEnchantedDartCount";
    public static final String REMEDY_START_DURATION_SYNC_KEY = "setRemedyStartDuration";
    public static final String LIFE_SHARD_COUNT_SYNC_KEY = "setLifeShardCount";
    public static final String LAST_RIDDEN_MOA_SYNC_KEY = "setLastRiddenMoa";

    private static final Identifier LIFE_SHARD_HEALTH_ID = Identifier.fromNamespaceAndPath(Aether.MODID, "life_shard_max_health");

    private boolean canGetPortal = true;
    private boolean canSpawnInAether = true;

    public float portalIntensity;
    public float oPortalIntensity;

    private boolean isHitting;
    private boolean isMoving;
    private boolean isJumping;
    private boolean isGravititeJumpActive;
    private boolean seenSunSpiritDialogue;

    private int goldenDartCount;
    private int poisonDartCount;
    private int enchantedDartCount;
    private int removeGoldenDartTime;
    private int removePoisonDartTime;
    private int removeEnchantedDartTime;

    private int remedyStartDuration;

    private boolean performVampireHealing;

    @Nullable
    private Aerbunny mountedAerbunny;
    private Optional<CompoundTag> mountedAerbunnyTag = Optional.empty();

    private Optional<UUID> lastRiddenMoa = Optional.empty();

    private final List<CloudMinion> cloudMinions = new ArrayList<>(2);

    private int wingRotationO;
    private int wingRotation;

    private static final int FLIGHT_TIMER_MAX = 52;
    private static final float FLIGHT_MODIFIER_MAX = 15.0F;
    private int flightTimer;
    private float flightModifier = 1.0F;

    private double neptuneSubmergeLength;
    private double phoenixSubmergeLength;

    private static final int OBSIDIAN_TIMER_MAX = 20;
    private int obsidianConversionTime;

    private float savedHealth = 0.0F;
    private int lifeShards;

    /**
     * Stores the following methods as able to be synced between client and server and vice-versa.
     */
    private final Map<String, SyncField> syncFields = Map.ofEntries(
        Map.entry(HITTING_SYNC_KEY, new SyncField(ValueType.BOOLEAN, (object) -> this.setHitting((boolean) object), this::isHitting)),
        Map.entry(MOVING_SYNC_KEY, new SyncField(ValueType.BOOLEAN, (object) -> this.setMoving((boolean) object), this::isMoving)),
        Map.entry(JUMPING_SYNC_KEY, new SyncField(ValueType.BOOLEAN, (object) -> this.setJumping((boolean) object), this::isJumping)),
        Map.entry(GRAVITITE_JUMP_ACTIVE_SYNC_KEY, new SyncField(ValueType.BOOLEAN, (object) -> this.setGravititeJumpActive((boolean) object), this::isGravititeJumpActive)),
        Map.entry(GOLDEN_DART_COUNT_SYNC_KEY, new SyncField(ValueType.INT, (object) -> this.setGoldenDartCount((int) object), this::getGoldenDartCount)),
        Map.entry(POISON_DART_COUNT_SYNC_KEY, new SyncField(ValueType.INT, (object) -> this.setPoisonDartCount((int) object), this::getPoisonDartCount)),
        Map.entry(ENCHANTED_DART_COUNT_SYNC_KEY, new SyncField(ValueType.INT, (object) -> this.setEnchantedDartCount((int) object), this::getEnchantedDartCount)),
        Map.entry(REMEDY_START_DURATION_SYNC_KEY, new SyncField(ValueType.INT, (object) -> this.setRemedyStartDuration((int) object), this::getRemedyStartDuration)),
        Map.entry(LIFE_SHARD_COUNT_SYNC_KEY, new SyncField(ValueType.INT, (object) -> this.setLifeShardCount((int) object), this::getLifeShardCount)),
        Map.entry(LAST_RIDDEN_MOA_SYNC_KEY, new SyncField(ValueType.UUID, (object) -> this.setLastRiddenMoa((UUID) object), this::getLastRiddenMoa))
    );
    private boolean shouldSyncAfterJoin;

    public static final Codec<AetherPlayerAttachment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("can_get_portal").forGetter(AetherPlayerAttachment::canGetPortal),
        Codec.BOOL.fieldOf("can_spawn_in_aether").forGetter(AetherPlayerAttachment::canSpawnInAether),
        Codec.FLOAT.fieldOf("saved_health").forGetter(AetherPlayerAttachment::getSavedHealth),
        Codec.INT.fieldOf("life_shard_count").forGetter(AetherPlayerAttachment::getLifeShardCount),
        Codec.BOOL.fieldOf("seen_sun_spirit").forGetter(AetherPlayerAttachment::hasSeenSunSpiritDialogue),
        Codec.INT.fieldOf("remedy_start_duration").forGetter(AetherPlayerAttachment::getRemedyStartDuration),
        CompoundTag.CODEC.optionalFieldOf("mounted_aerbunny").forGetter(AetherPlayerAttachment::getMountedAerbunnyTag),
        UUIDUtil.CODEC.optionalFieldOf("last_ridden_moa").forGetter(o -> o.lastRiddenMoa)
    ).apply(instance, AetherPlayerAttachment::new));

    public AetherPlayerAttachment() {

    }

    public AetherPlayerAttachment(boolean portal, boolean spawnInAether, float savedHealth, int lifeShards, boolean seenSunSpirit, int remedyDuration, Optional<CompoundTag> mountedAerbunnyTag, Optional<UUID> lastRiddenMoa) {
        this.setCanGetPortal(portal);
        this.setCanSpawnInAether(spawnInAether);
        this.setSavedHealth(savedHealth);
        this.setLifeShardCount(lifeShards);
        this.setSeenSunSpiritDialogue(seenSunSpirit);
        this.setRemedyStartDuration(remedyDuration);
        this.setMountedAerbunnyTag(mountedAerbunnyTag);
        this.setLastRiddenMoa(lastRiddenMoa.orElse(null));
    }

    public void copyPersistentStateFrom(AetherPlayerAttachment source) {
        this.setCanGetPortal(source.canGetPortal());
        this.setCanSpawnInAether(source.canSpawnInAether());
        this.setSavedHealth(source.getSavedHealth());
        this.setLifeShardCount(source.getLifeShardCount());
        this.setSeenSunSpiritDialogue(source.hasSeenSunSpiritDialogue());
        this.setRemedyStartDuration(source.getRemedyStartDuration());
        this.setMountedAerbunnyTag(source.getMountedAerbunnyTag().map(CompoundTag::copy));
        this.setLastRiddenMoa(source.getLastRiddenMoa());
    }

    public Map<String, SyncField> getSyncFields() {
        return this.syncFields;
    }

    /**
     * Handles functions when the player logs out of a world.
     */
    public void onLogout(Player player) {
        this.removeAerbunny();
        this.handleLogoutSavedHealth(player);
    }

    /**
     * Handles functions when the player logs in to a world.
     */
    public void onLogin(Player player) {
        this.handleGivePortal(player);
        this.remountAerbunny(player);
        this.shouldSyncAfterJoin = true;
    }

    /**
     * Used to correct data between instances of the attachment after player cloning. <br>
     * This attachment copies all data by default, this is here to reset certain values that dont need to persist on death.
     *
     * @param wasDeath A {@link Boolean} for whether this copying is from death. If false, the copying is from entering the End Portal.
     */
    public void handleRespawn(boolean wasDeath) {
        if (wasDeath) {
            this.setRemedyStartDuration(0);
        }
        this.shouldSyncAfterJoin = true;
    }

    /**
     * Handles per-tick player update functions.
     */
    public void onUpdate(Player player) {
        this.syncAfterJoin(player);
        this.handleAetherPortal(player);
        this.activateParachute(player);
        this.handleRemoveDarts(player);
        this.removeRemedyDuration(player);
        this.handleWingRotation(player);
        this.handleVampireHealing(player);
        this.checkToRemoveAerbunny(player);
        this.checkToRemoveCloudMinions();
        this.handleSavedHealth(player);
        this.handleLifeShardModifier(player);
    }

    private void syncAfterJoin(Player player) {
        if (this.shouldSyncAfterJoin) {
            this.forceSyncToClients(player);
            this.shouldSyncAfterJoin = false;
        }
    }

    /**
     * Increments or decrements the Aether portal timer depending on if the player is inside an Aether portal.
     * On the client, this will also help to set the portal overlay.
     */
    private void handleAetherPortal(Player player) {
        if (player.level().isClientSide()) {
            PortalClientUtil.handleAetherPortal(player, this);
        }
    }

    public void setCanSpawnInAether(boolean canSpawnInAether) {
        this.canSpawnInAether = canSpawnInAether;
    }

    /**
     * @return Whether the player will spawn in the Aether dimension on first join, as a {@link Boolean}.
     */
    public boolean canSpawnInAether() {
        return this.canSpawnInAether;
    }

    public float getPortalIntensity() {
        return this.portalIntensity;
    }

    public float getOldPortalIntensity() {
        return this.oPortalIntensity;
    }

    /**
     * Gives the player an Aether Portal Frame item on login if the {@link AetherConfig.Common#start_with_portal} config is enabled.
     */
    private void handleGivePortal(Player player) {
        if (AetherConfig.COMMON.start_with_portal.get()) {
            this.givePortalItem(player);
        } else {
            this.setCanGetPortal(false);
        }
    }

    /**
     * Activates any parachute that the player has in their inventory when falling fast enough.
     * Checks for deployable parachutes from {@link AetherTags.Items#DEPLOYABLE_PARACHUTES}.
     */
    private void activateParachute(Player player) {
        Inventory inventory = player.getInventory();
        Level level = player.level();
        if (!player.isCreative() && !player.isShiftKeyDown() && !player.isFallFlying() && !player.isPassenger()) {
            if (player.getDeltaMovement().y() < -1.5) {
                if (inventory.contains(AetherTags.Items.DEPLOYABLE_PARACHUTES)) {
                    for (ItemStack stack : inventory.getNonEquipmentItems()) {
                        if (stack.getItem() instanceof ParachuteItem parachuteItem) {
                            Parachute parachute = parachuteItem.getParachuteEntity().get().create(level, EntitySpawnReason.TRIGGERED);
                            if (parachute != null) {
                                parachute.setPos(player.getX(), player.getY() - 1.0, player.getZ());
                                parachute.setDeltaMovement(player.getDeltaMovement());
                                if (!level.isClientSide()) {
                                    level.addFreshEntity(parachute);
                                    player.startRiding(parachute);
                                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                                }
                                parachute.spawnExplosionParticle();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Slowly removes darts that are rendered as stuck on the player by {@link com.aetherteam.aether.client.renderer.player.layer.DartLayer}.
     */
    private void handleRemoveDarts(Player player) {
        if (!player.level().isClientSide()) {
            if (this.getGoldenDartCount() > 0) {
                if (this.removeGoldenDartTime <= 0) {
                    this.removeGoldenDartTime = 20 * (30 - this.getGoldenDartCount());
                }

                --this.removeGoldenDartTime;
                if (this.removeGoldenDartTime <= 0) {
                    this.setSyncedToClients(player, GOLDEN_DART_COUNT_SYNC_KEY, this.getGoldenDartCount() - 1);
                }
            }
            if (this.getPoisonDartCount() > 0) {
                if (this.removePoisonDartTime <= 0) {
                    this.removePoisonDartTime = 20 * (30 - this.getPoisonDartCount());
                }

                --this.removePoisonDartTime;
                if (this.removePoisonDartTime <= 0) {
                    this.setSyncedToClients(player, POISON_DART_COUNT_SYNC_KEY, this.getPoisonDartCount() - 1);
                }
            }
            if (this.getEnchantedDartCount() > 0) {
                if (this.removeEnchantedDartTime <= 0) {
                    this.removeEnchantedDartTime = 20 * (30 - this.getEnchantedDartCount());
                }

                --this.removeEnchantedDartTime;
                if (this.removeEnchantedDartTime <= 0) {
                    this.setSyncedToClients(player, ENCHANTED_DART_COUNT_SYNC_KEY, this.getEnchantedDartCount() - 1);
                }
            }
        }
    }

    private void removeRemedyDuration(Player player) {
        if (this.remedyStartDuration > 0) {
            if (!player.hasEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(AetherEffects.REMEDY))) {
                this.remedyStartDuration = 0;
            }
        }
    }

    /**
     * Handles the rotation for the Valkyrie Armor wings layer renderer at {@link com.aetherteam.aether.client.renderer.player.layer.PlayerWingsLayer}.
     */
    private void handleWingRotation(Player player) {
        if (player.level().isClientSide()) {
            this.wingRotationO = this.getWingRotation();
            if (EquipmentUtil.hasFullValkyrieSet(player)) {
                this.wingRotation = player.tickCount;
            } else {
                this.wingRotation = 0;
            }
        }
    }

    /**
     * Used for healing the player with a Vampire Blade. This method exists to get around a bug with heart rendering.
     */
    private void handleVampireHealing(Player player) {
        if (!player.level().isClientSide() && this.performVampireHealing()) {
            player.heal(1.0F);
            this.setVampireHealing(false);
        }
    }

    /**
     * Checks whether the player attachment should stop tracking a mounted Aerbunny.
     */
    private void checkToRemoveAerbunny(Player player) {
        if (this.getMountedAerbunny() != null && (!this.getMountedAerbunny().isAlive() || !player.isAlive())) {
            this.setMountedAerbunny(null);
        }
    }

    /**
     * Removes an Aerbunny from the world and stores it to NBT for the player attachment. This is used when a player logs out with an Aerbunny.
     */
    public void removeAerbunny() {
        if (this.getMountedAerbunny() != null) {
            Aerbunny aerbunny = this.getMountedAerbunny();
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, aerbunny.registryAccess());
            aerbunny.saveAsPassenger(output);
            CompoundTag nbt = output.buildResult();
            this.setMountedAerbunnyTag(Optional.of(nbt));
            aerbunny.stopRiding();
            aerbunny.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        }
    }

    /**
     * Remounts an Aerbunny to the player if there exists stored NBT when joining the world.
     */
    public void remountAerbunny(Player player) {
            if (this.getMountedAerbunnyTag().isPresent()) {
                if (!player.level().isClientSide()) {
                    Aerbunny aerbunny = new Aerbunny(AetherEntityTypes.AERBUNNY, player.level());
                    aerbunny.load(TagValueInput.create(ProblemReporter.DISCARDING, player.registryAccess(), this.getMountedAerbunnyTag().get()));
                    player.level().addFreshEntity(aerbunny);
                    aerbunny.startRiding(player);
                    this.setMountedAerbunny(aerbunny);
                if (player instanceof ServerPlayer serverPlayer) {
                    AetherPacketSender.sendToPlayer(serverPlayer, new RemountAerbunnyPacket(player.getId(), aerbunny.getId()));
                }
            }
            this.setMountedAerbunnyTag(Optional.empty());
        }
    }

    /**
     * Checks whether the player attachment should stop tracking Cloud Minions.
     */
    private void checkToRemoveCloudMinions() {
        this.getCloudMinions().removeIf(cloudMinion -> !cloudMinion.isAlive());
    }

    /**
     * Used when player attachment data is copied. This restores any extra health the players had from Life Shards before the copy occurred.
     */
    private void handleSavedHealth(Player player) {
        if (this.getSavedHealth() > 0.0F) {
            AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
            if (health != null && health.hasModifier(this.getLifeShardHealthAttributeModifier().id())) {
                player.setHealth(Math.min(this.getSavedHealth(), player.getMaxHealth()));
                this.setSavedHealth(0.0F);
            }
        }
    }

    /**
     * Sets up the attribute modifier for extra Life Shard hearts.
     */
    private void handleLifeShardModifier(Player player) {
        if (!player.level().isClientSide()) {
            AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
            AttributeModifier lifeShardHealth = this.getLifeShardHealthAttributeModifier();
            if (health != null) {
                if (health.hasModifier(lifeShardHealth.id())) {
                    health.removeModifier(lifeShardHealth.id());
                }
                health.addTransientModifier(lifeShardHealth);
            }
        }
    }

    /**
     * Sets the player's extra saved health on logout.
     */
    private void handleLogoutSavedHealth(Player player) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        if (health != null && health.hasModifier(this.getLifeShardHealthAttributeModifier().id())) {
            this.setSavedHealth(player.getHealth());
        }
    }

    /**
     * Gives the player an Aether Portal Frame item.
     */
    public void givePortalItem(Player player) {
        if (this.canGetPortal()) {
            player.addItem(new ItemStack(AetherItems.AETHER_PORTAL_FRAME));
            this.setCanGetPortal(false);
        }
    }

    public void setCanGetPortal(boolean canGetPortal) {
        this.canGetPortal = canGetPortal;
    }

    /**
     * @return Whether the player can get the Aether Portal Frame item, as a {@link Boolean}.
     */
    public boolean canGetPortal() {
        return this.canGetPortal;
    }

    public void setHitting(boolean isHitting) {
        this.isHitting = isHitting;
    }

    /**
     * @return Whether the player is hitting, as a {@link Boolean}.
     */
    public boolean isHitting() {
        return this.isHitting;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    /**
     * @return Whether the player is moving, as a {@link Boolean}.
     */
    public boolean isMoving() {
        return this.isMoving;
    }

    public void setJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    /**
     * @return Whether the player is jumping, as a {@link Boolean}.
     */
    public boolean isJumping() {
        return this.isJumping;
    }

    public void setGravititeJumpActive(boolean isGravititeJumpActive) {
        this.isGravititeJumpActive = isGravititeJumpActive;
    }

    /**
     * @return Whether the gravitite jump ability is active, as a {@link Boolean}.
     */
    public boolean isGravititeJumpActive() {
        return this.isGravititeJumpActive;
    }

    public void setSeenSunSpiritDialogue(boolean seenDialogue) {
        this.seenSunSpiritDialogue = seenDialogue;
    }

    /**
     * @return Whether the player has already seen the Sun Spirit's dialogue, as a {@link Boolean}.
     */
    public boolean hasSeenSunSpiritDialogue() {
        return this.seenSunSpiritDialogue;
    }

    public void setGoldenDartCount(int count) {
        this.goldenDartCount = count;
    }

    /**
     * @return An {@link Integer} for how many Golden Darts are stuck in the player.
     */
    public int getGoldenDartCount() {
        return this.goldenDartCount;
    }

    public void setPoisonDartCount(int count) {
        this.poisonDartCount = count;
    }

    /**
     * @return An {@link Integer} for how many Poison Darts are stuck in the player.
     */
    public int getPoisonDartCount() {
        return this.poisonDartCount;
    }

    public void setEnchantedDartCount(int count) {
        this.enchantedDartCount = count;
    }

    /**
     * @return An {@link Integer} for how many Enchanted Darts are stuck in the player.
     */
    public int getEnchantedDartCount() {
        return this.enchantedDartCount;
    }

    public void setRemedyStartDuration(int duration) {
        this.remedyStartDuration = duration;
    }

    /**
     * @return An {@link Integer} for the original time duration of the remedy effect.
     */
    public int getRemedyStartDuration() {
        return this.remedyStartDuration;
    }

    public void setVampireHealing(boolean performVampireHealing) {
        this.performVampireHealing = performVampireHealing;
    }

    /**
     * @return Whether to heal from attacking with the Vampire Blade, as a {@link Boolean}.
     */
    public boolean performVampireHealing() {
        return this.performVampireHealing;
    }

    public void setMountedAerbunny(@Nullable Aerbunny mountedAerbunny) {
        this.mountedAerbunny = mountedAerbunny;
    }

    /**
     * @return The {@link Aerbunny} currently mounted to the player
     */
    @Nullable
    public Aerbunny getMountedAerbunny() {
        return this.mountedAerbunny;
    }

    public void setMountedAerbunnyTag(Optional<CompoundTag> mountedAerbunnyTag) {
        this.mountedAerbunnyTag = mountedAerbunnyTag;
    }

    /**
     * @return The {@link CompoundTag} data for the Aerbunny currently mounted to the player.
     */
    public Optional<CompoundTag> getMountedAerbunnyTag() {
        return this.mountedAerbunnyTag;
    }

    public void setLastRiddenMoa(@Nullable UUID lastRiddenMoa) {
        this.lastRiddenMoa = Optional.ofNullable(lastRiddenMoa);
    }

    /**
     * @return The {@link UUID} for the last ridden Moa.
     */
    @Nullable
    public UUID getLastRiddenMoa() {
        return this.lastRiddenMoa.orElse(null);
    }

    public void setCloudMinions(Player player, CloudMinion cloudMinionRight, CloudMinion cloudMinionLeft) {
        this.sendCloudMinionPacket(player, cloudMinionRight, cloudMinionLeft);
        this.cloudMinions.add(0, cloudMinionRight);
        this.cloudMinions.add(1, cloudMinionLeft);
    }

    /**
     * @return The currently summoned {@link CloudMinion}s.
     */
    public List<CloudMinion> getCloudMinions() {
        return this.cloudMinions;
    }

    /**
     * @return An {@link Integer} for the last wing rotation value.
     */
    public int getWingRotationO() {
        return this.wingRotationO;
    }


    /**
     * @return An {@link Integer} for the wing rotation value.
     */
    public int getWingRotation() {
        return this.wingRotation;
    }

    /**
     * @return An {@link Integer} for the maximum flight duration.
     */
    public int getFlightTimerMax() {
        return FLIGHT_TIMER_MAX;
    }

    /**
     * @return A {@link Float} for the maximum flight speed modifier.
     */
    public float getFlightModifierMax() {
        return FLIGHT_MODIFIER_MAX;
    }

    public void setFlightTimer(int timer) {
        this.flightTimer = timer;
    }

    /**
     * @return The {@link Integer} timer for how long the player has to fly.
     */
    public int getFlightTimer() {
        return this.flightTimer;
    }

    public void setFlightModifier(float modifier) {
        this.flightModifier = modifier;
    }

    /**
     * @return The {@link Float} modifier for the player's flight speed.
     */
    public float getFlightModifier() {
        return this.flightModifier;
    }

    public void setSavedHealth(float health) {
        this.savedHealth = health;
    }

    /**
     * @return The {@link Float} for player health stored between player attachment copying.
     */
    public float getSavedHealth() {
        return this.savedHealth;
    }

    public void setNeptuneSubmergeLength(double length) {
        this.neptuneSubmergeLength = length;
    }

    /**
     * @return A {@link Double} for how long the player has been submerged in water while wearing Neptune Armor.
     */
    public double getNeptuneSubmergeLength() {
        return this.neptuneSubmergeLength;
    }

    public void setPhoenixSubmergeLength(double length) {
        this.phoenixSubmergeLength = length;
    }

    /**
     * @return A {@link Double} for how long the player has been submerged in lava while wearing Phoenix Armor.
     */
    public double getPhoenixSubmergeLength() {
        return this.phoenixSubmergeLength;
    }

    /**
     * @return An {@link Integer} for how long Phoenix Armor takes to convert into Obsidian Armor.
     */
    public int getObsidianConversionTimerMax() {
        return OBSIDIAN_TIMER_MAX;
    }

    public void setObsidianConversionTime(int time) {
        this.obsidianConversionTime = time;
    }

    /**
     * @return An {@link Integer} timer for how long until Phoenix Armor converts into Obsidian Armor.
     */
    public int getObsidianConversionTime() {
        return this.obsidianConversionTime;
    }

    public void setLifeShardCount(int amount) {
        this.lifeShards = amount;
    }

    /**
     * @return An {@link Integer} for how many Life Shards the player has used.
     */
    public int getLifeShardCount() {
        return this.lifeShards;
    }

    /**
     * @return An {@link Integer} for the maximum amount of Life Shards the player can use, from {@link AetherConfig.Server#maximum_life_shards}.
     */
    public int getLifeShardLimit() {
        return AetherConfig.SERVER.maximum_life_shards.get();
    }

    /**
     * @return The Life Shard health {@link AttributeModifier}.
     */
    public AttributeModifier getLifeShardHealthAttributeModifier() {
        return new AttributeModifier(LIFE_SHARD_HEALTH_ID, this.getLifeShardCount() * 2.0F, AttributeModifier.Operation.ADD_VALUE);
    }

    /**
     * Tracks the player's summoned Cloud Minions on the client.
     *
     * @param cloudMinionRight The right {@link CloudMinion}.
     * @param cloudMinionLeft  The left {@link CloudMinion}.
     */
    private void sendCloudMinionPacket(Player player, CloudMinion cloudMinionRight, CloudMinion cloudMinionLeft) {
        if (player instanceof ServerPlayer serverPlayer && !player.level().isClientSide()) {
            AetherPacketSender.sendToPlayer(serverPlayer, new CloudMinionPacket(player.getId(), cloudMinionRight.getId(), cloudMinionLeft.getId()));
        }
    }

    @Override
    public SyncPacket<?> getSyncPacket(int entityID, String key, ValueType valueType, Object value) {
        return new AetherPlayerSyncPacket(entityID, key, valueType, value);
    }
}
