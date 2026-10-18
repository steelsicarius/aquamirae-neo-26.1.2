package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.client.model.ModelMaw;
import dev.obscuria.aquamirae.client.renderer.entity.state.MawRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class MawItemLayer extends RenderLayer<MawRenderState, ModelMaw> {

    public MawItemLayer(RenderLayerParent<MawRenderState, ModelMaw> layer) {
        super(layer);
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector source, int packedLight, MawRenderState state, float f1, float f2) {
        if (state.mouthItem.isEmpty()) return;
        pose.pushPose();
        this.getParentModel().translateToMouth(pose);
        pose.mulPose(Axis.XP.rotationDegrees(100.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(0.0F));
        pose.translate(0.0D, -0.8D, 0.02D);
        pose.scale(0.7f, 0.7f, 0.7f);
        state.mouthItem.submit(pose, source, packedLight, OverlayTexture.NO_OVERLAY, 0);
        pose.popPose();
    }
}
