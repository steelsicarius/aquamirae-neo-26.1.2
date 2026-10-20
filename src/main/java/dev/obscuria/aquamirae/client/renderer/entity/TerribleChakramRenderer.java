package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelTerribleChakram;
import dev.obscuria.aquamirae.client.renderer.entity.state.ChakramRenderState;
import dev.obscuria.aquamirae.common.entity.projectile.TerribleChakram;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class TerribleChakramRenderer<T extends TerribleChakram> extends EntityRenderer<T, ChakramRenderState> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/terrible_chakram.png");

    private final ModelTerribleChakram model;

    public TerribleChakramRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelTerribleChakram(context.bakeLayer(AquamiraeModelLayers.TERRIBLE_CHAKRAM));
    }

    @Override
    public ChakramRenderState createRenderState() {
        return new ChakramRenderState();
    }

    @Override
    public void extractRenderState(T entity, ChakramRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        state.pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
    }

    @Override
    public void submit(ChakramRenderState state, PoseStack pose, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
        super.submit(state, pose, renderTasks, cameraState);
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(state.yaw + 180.0F));
        pose.mulPose(Axis.XP.rotationDegrees(state.pitch));
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);
        this.model.setupAnim(state);
        renderTasks.submitModel(this.model, state, pose, this.model.renderType(TEXTURE),
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, -1, null);
        pose.popPose();
    }
}
