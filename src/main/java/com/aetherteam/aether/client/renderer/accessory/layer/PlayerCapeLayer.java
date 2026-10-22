package com.aetherteam.aether.client.renderer.accessory.layer;

import com.aetherteam.aether.client.renderer.accessory.CapeTextures;
import com.aetherteam.aether.item.accessories.cape.CapeItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * Draws a player's Aether cape (worn, or smithed onto their chestplate) with vanilla's cape model, so it flaps as a
 * player cape does. It's our own layer, added through Fabric API, rather than swapping the skin's cape as the Aether
 * did. A cape the player owns may still be drawn under it: it sits a hair further out.
 */
public class PlayerCapeLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final HumanoidModel<AvatarRenderState> model;

    public PlayerCapeLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new PlayerCapeModel(modelSet.bakeLayer(ModelLayers.PLAYER_CAPE));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, AvatarRenderState state, float yRot, float xRot) {
        if (state.isInvisible) {
            return;
        }
        Identifier texture = CapeTextures.get(state.chestEquipment);
        if (texture == null) {
            return;
        }
        poseStack.pushPose();
        if (!(state.chestEquipment.getItem() instanceof CapeItem)) { // Over a chestplate, as vanilla moves a cape out over armour.
            poseStack.translate(0.0F, -0.053125F, 0.06875F);
        }
        poseStack.translate(0.0F, 0.0F, 0.003F);
        collector.submitModel(this.model, state, poseStack, RenderTypes.entitySolid(texture), light, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
