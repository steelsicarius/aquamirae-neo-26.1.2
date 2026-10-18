package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelMazeRose;
import dev.obscuria.aquamirae.client.renderer.entity.state.MazeRoseRenderState;
import dev.obscuria.aquamirae.common.entity.projectile.MazeRose;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class MazeRoseRenderer<T extends MazeRose> extends EntityRenderer<T, MazeRoseRenderState> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/maze_rose.png");
    public static final Identifier OVERLAY_TEXTURE = Aquamirae.id("textures/entity/maze_rose_overlay.png");

    private final ModelMazeRose model;

    public MazeRoseRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ModelMazeRose(context.bakeLayer(AquamiraeModelLayers.MAZE_ROSE));
    }

    @Override
    public MazeRoseRenderState createRenderState() {
        return new MazeRoseRenderState();
    }

    @Override
    public void extractRenderState(T entity, MazeRoseRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        state.pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
    }

    @Override
    public void submit(MazeRoseRenderState state, PoseStack pose, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
        super.submit(state, pose, renderTasks, cameraState);
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(state.yaw + 180.0F));
        pose.mulPose(Axis.XP.rotationDegrees(state.pitch));
        pose.scale(-1.0F, -1.0F, 1.0F);
        pose.translate(0.0F, -1.501F, 0.0F);
        this.model.setupAnim(state);
        renderTasks.submitModel(this.model, state, pose, this.model.renderType(TEXTURE),
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, null);
        renderTasks.submitModel(this.model, state, pose, RenderTypes.eyes(OVERLAY_TEXTURE),
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, null);
        pose.popPose();
    }
}
