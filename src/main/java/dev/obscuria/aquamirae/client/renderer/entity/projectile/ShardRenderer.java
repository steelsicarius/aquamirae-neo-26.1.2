package dev.obscuria.aquamirae.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.client.model.ModelShard;
import dev.obscuria.aquamirae.client.renderer.entity.state.ShardRenderState;
import dev.obscuria.aquamirae.common.entity.projectile.AbstractShard;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;

public abstract class ShardRenderer<T extends AbstractShard, S extends ShardRenderState> extends EntityRenderer<T, S> {

    private final ModelShard model;

    public ShardRenderer(EntityRendererProvider.Context context, ModelShard model) {
        super(context);
        this.model = model;
    }

    @Override
    public abstract S createRenderState();

    @Override
    public void extractRenderState(T shard, S state, float delta) {
        super.extractRenderState(shard, state, delta);
        state.shardScale = shard.getShardScale();
        state.localXRot = Mth.lerp(delta, shard.localXRotO, shard.localXRot);
        state.localYRot = Mth.lerp(delta, shard.localYRotO, shard.localYRot);
    }

    @Override
    public void submit(S state, PoseStack pose, SubmitNodeCollector source, CameraRenderState cameraState) {
        super.submit(state, pose, source, cameraState);
        pose.pushPose();
        pose.scale(-1, -1, 1);
        pose.scale(state.shardScale, state.shardScale, state.shardScale);
        pose.translate(0, -1.5, 0);
        this.model.setupAnim(state);
        source.submitModel(this.model, state, pose,
                RenderTypes.eyes(getTextureLocation(state)),
                state.lightCoords, OverlayTexture.NO_OVERLAY, CommonColors.WHITE, null, null);
        pose.popPose();
    }

    public abstract Identifier getTextureLocation(S state);
}
