package com.aetherteam.aether.client.renderer.entity.layers;

import com.aetherteam.aether.api.registers.MoaType;
import com.aetherteam.aether.client.renderer.entity.model.MoaModel;
import com.aetherteam.aether.client.renderer.entity.state.MoaRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;

public class MoaSaddleLayer extends RenderLayer<MoaRenderState, MoaModel> {
    private static final Identifier DEFAULT_LOCATION = Identifier.withDefaultNamespace("textures/entity/mobs/moa/black_moa_saddle.png");
    private final MoaModel saddle;

    public MoaSaddleLayer(RenderLayerParent<MoaRenderState, MoaModel> entityRenderer, MoaModel saddleModel) {
        super(entityRenderer);
        this.saddle = saddleModel;
    }

    /**
     * Renders a saddle layer on a Moa, with the {@link MoaType}'s saddle texture.
     *
     * @param poseStack       The rendering {@link PoseStack}.
     * @param collector       The rendering {@link SubmitNodeCollector}.
     * @param packedLight     The {@link Integer} for the packed lighting for rendering.
     * @param renderState     The {@link MoaRenderState} for the entity.
     * @param netHeadYaw      The {@link Float} for the head yaw rotation.
     * @param headPitch       The {@link Float} for the head pitch rotation.
     */
    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, MoaRenderState renderState, float netHeadYaw, float headPitch) {
        if (renderState.saddle && !renderState.isInvisible) {
            Identifier texture = renderState.type != null ? renderState.saddleLocation : DEFAULT_LOCATION;
            this.saddle.setupAnim(renderState);
            renderColoredCutoutModel(this.saddle, texture, poseStack, collector, packedLight, renderState, -1, 0);
        }
    }
}
