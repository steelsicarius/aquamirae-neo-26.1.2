package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelEel;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.client.renderer.entity.state.EelRenderState;
import dev.obscuria.aquamirae.common.entity.Eel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class EelRenderer<T extends Eel> extends MobRenderer<T, EelRenderState, ModelEel> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/eel.png");
    public static final Identifier OVERLAY_TEXTURE = Aquamirae.id("textures/entity/eel_overlay.png");

    public EelRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelEel(context.bakeLayer(AquamiraeModelLayers.EEL)), 0f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public EelRenderState createRenderState() {
        return new EelRenderState();
    }

    @Override
    public void extractRenderState(T entity, EelRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.specialIdleState = entity.specialIdleState;
        state.attackState = entity.attackState;
        state.roarState = entity.roarState;
        state.digState = entity.digState;
        state.deathState = entity.deathState;
    }

    @Override
    public Identifier getTextureLocation(EelRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(EelRenderState state, PoseStack poseStack) {
        poseStack.scale(1.8f, 1.8f, 1.8f);
        poseStack.translate(0F, -0.7F, 0F);
    }
}
