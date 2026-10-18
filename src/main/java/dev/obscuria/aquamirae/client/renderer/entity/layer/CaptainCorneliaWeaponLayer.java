package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.client.model.ModelCaptainCornelia;
import dev.obscuria.aquamirae.client.renderer.entity.state.CaptainCorneliaRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;

public class CaptainCorneliaWeaponLayer extends RenderLayer<CaptainCorneliaRenderState, ModelCaptainCornelia> {

    public CaptainCorneliaWeaponLayer(RenderLayerParent<CaptainCorneliaRenderState, ModelCaptainCornelia> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack pose, SubmitNodeCollector source, int packedLight, CaptainCorneliaRenderState state, float f1, float f2) {
        if (!state.mainHandItem.isEmpty()) {
            pose.pushPose();
            this.getParentModel().translateToHand(HumanoidArm.RIGHT, pose);
            pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.translate(0.0D, 0.1D, 0.0D);
            state.mainHandItem.submit(pose, source, packedLight, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
        }

        if (!state.offHandItem.isEmpty()) {
            pose.pushPose();
            this.getParentModel().translateToHand(HumanoidArm.LEFT, pose);
            pose.mulPose(Axis.XP.rotationDegrees(45.0F));
            pose.translate(0.0D, -0.15D, -0.65D);
            state.offHandItem.submit(pose, source, packedLight, OverlayTexture.NO_OVERLAY, 0);
            pose.popPose();
        }
    }
}
