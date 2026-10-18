package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelTorturedSoul;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.client.renderer.entity.state.TorturedSoulRenderState;
import dev.obscuria.aquamirae.common.entity.TorturedSoul;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class TorturedSoulRenderer<T extends TorturedSoul> extends MobRenderer<T, TorturedSoulRenderState, ModelTorturedSoul> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/tortured_soul.png");
    public static final Identifier OVERLAY_TEXTURE = Aquamirae.id("textures/entity/tortured_soul_overlay.png");

    public TorturedSoulRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelTorturedSoul(context.bakeLayer(AquamiraeModelLayers.TORTURED_SOUL)), 0.5f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public TorturedSoulRenderState createRenderState() {
        return new TorturedSoulRenderState();
    }

    @Override
    public void extractRenderState(T entity, TorturedSoulRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.attackState = entity.attackState;
    }

    @Override
    public Identifier getTextureLocation(TorturedSoulRenderState state) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(TorturedSoulRenderState state) {
        return true;
    }
}
