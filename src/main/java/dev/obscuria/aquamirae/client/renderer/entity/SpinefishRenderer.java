package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelSpinefish;
import dev.obscuria.aquamirae.common.entity.Spinefish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class SpinefishRenderer<T extends Spinefish> extends MobRenderer<T, ModelSpinefish<T>> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/spinefish.png");

    public SpinefishRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelSpinefish<>(context.bakeLayer(AquamiraeModelLayers.SPINEFISH)), 0.3f);
    }

    @Override
    public Identifier getTextureLocation(Spinefish entity) {
        return TEXTURE;
    }
}
