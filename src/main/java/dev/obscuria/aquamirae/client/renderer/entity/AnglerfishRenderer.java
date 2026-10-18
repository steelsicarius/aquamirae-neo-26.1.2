package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelAnglerfish;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.client.renderer.entity.state.AnglerfishRenderState;
import dev.obscuria.aquamirae.common.entity.Anglerfish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class AnglerfishRenderer<T extends Anglerfish> extends MobRenderer<T, AnglerfishRenderState, ModelAnglerfish> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/anglerfish.png");
    public static final Identifier OVERLAY_TEXTURE = Aquamirae.id("textures/entity/anglerfish_overlay.png");

    public AnglerfishRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelAnglerfish(context.bakeLayer(AquamiraeModelLayers.ANGLERFISH)), 1.5f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public AnglerfishRenderState createRenderState() {
        return new AnglerfishRenderState();
    }

    @Override
    public void extractRenderState(T entity, AnglerfishRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.groundMod = Mth.lerp(partialTick, entity.clientGroundModO, entity.clientGroundMod);
        state.attackState = entity.attackState;
    }

    @Override
    public Vec3 getRenderOffset(AnglerfishRenderState state) {
        var offset = Mth.clampedMap(state.groundMod, 0f, 1f, -0.75f, 0f);
        return new Vec3(0.0, offset, 0.0);
    }

    @Override
    public Identifier getTextureLocation(AnglerfishRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(AnglerfishRenderState state, com.mojang.blaze3d.vertex.PoseStack poseStack) {
        poseStack.scale(1.5F, 1.5F, 1.5F);
    }
}
