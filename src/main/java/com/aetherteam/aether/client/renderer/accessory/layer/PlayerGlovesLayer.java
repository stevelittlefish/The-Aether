package com.aetherteam.aether.client.renderer.accessory.layer;

import com.aetherteam.aether.client.renderer.accessory.model.GlovesModel;
import com.aetherteam.aether.item.accessories.gloves.GlovesItem;
import com.aetherteam.aether.client.renderer.accessory.AccessoryRendering;
import com.aetherteam.aether.mixin.mixins.client.accessor.PlayerModelAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.palette.PalettedTextureManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class PlayerGlovesLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final GlovesModel glovesModel;
    private final GlovesModel glovesTrimModel;
    private final GlovesModel glovesSlimModel;
    private final GlovesModel glovesTrimSlimModel;

    public PlayerGlovesLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, GlovesModel glovesModel, GlovesModel glovesTrimModel, GlovesModel glovesSlimModel, GlovesModel glovesTrimSlimModel) {
        super(renderer);
        this.glovesModel = glovesModel;
        this.glovesTrimModel = glovesTrimModel;
        this.glovesSlimModel = glovesSlimModel;
        this.glovesTrimSlimModel = glovesTrimSlimModel;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, AvatarRenderState renderState, float netHeadYaw, float headPitch) {
        if (renderState.isInvisible || Minecraft.getInstance().level == null) {
            return;
        }

        if (!(Minecraft.getInstance().level.getEntity(renderState.id) instanceof LivingEntity livingEntity)) {
            return;
        }

        ItemStack stack = AccessoryRendering.getVisibleAccessory(livingEntity, GlovesItem.getStaticSlotType(), 0);
        if (!(stack.getItem() instanceof GlovesItem glovesItem)) {
            return;
        }

        boolean slim = ((PlayerModelAccessor) this.getParentModel()).aether$getSlim();
        GlovesModel model = slim ? this.glovesSlimModel : this.glovesModel;
        GlovesModel trimModel = slim ? this.glovesTrimSlimModel : this.glovesTrimModel;
        int overlay = LivingEntityRenderer.getOverlayCoords(renderState, 0.0F);
        int color = DyedItemColor.getOrDefault(stack, -1);

        this.getParentModel().copyTransforms(model);
        model.setupAnim(renderState);
        // Since 26.3 the enchantment glint is part of the armor render type rather than a pass of its own.
        RenderType glovesType = stack.hasFoil() ? RenderTypes.armorCutoutNoCullGlint(glovesItem.getGlovesTexture()) : RenderTypes.armorCutoutNoCull(glovesItem.getGlovesTexture());
        collector.order(0).submitModel(model, renderState, poseStack, glovesType, packedLight, overlay, color, null, -1);

        PalettedTextureManager.Handle trimSprite = AccessoryRendering.getHumanoidArmorTrimTexture(stack);
        if (trimSprite != null) {
            this.getParentModel().copyTransforms(trimModel);
            trimModel.setupAnim(renderState);
            collector.order(1).submitModel(trimModel, renderState, poseStack, AccessoryRendering.getArmorTrimRenderType(stack, trimSprite), packedLight, overlay, -1, trimSprite, -1);
        }
    }
}
