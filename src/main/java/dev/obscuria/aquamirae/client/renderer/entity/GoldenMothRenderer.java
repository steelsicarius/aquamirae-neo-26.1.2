package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.client.model.ModelGoldenMoth;
import dev.obscuria.aquamirae.common.entity.GoldenMoth;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.layer.builtin.AutoGlowingGeoLayer;

public final class GoldenMothRenderer extends GeoEntityRenderer<GoldenMoth, LivingEntityRenderState> {

    public GoldenMothRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelGoldenMoth());
        this.withRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.shadowRadius = 0.2f;
    }
}
