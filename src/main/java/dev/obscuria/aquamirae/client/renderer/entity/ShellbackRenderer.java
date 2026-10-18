package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.ModelShellback;
import dev.obscuria.aquamirae.client.renderer.entity.layer.HangingChestLayer;
import dev.obscuria.aquamirae.client.renderer.entity.layer.ShellbackSaddleLayer;
import dev.obscuria.aquamirae.common.entity.Shellback;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import com.geckolib.renderer.GeoEntityRenderer;

public class ShellbackRenderer extends GeoEntityRenderer<Shellback, LivingEntityRenderState> {

    public ShellbackRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelShellback());
        this.withRenderLayer(new ShellbackSaddleLayer<>(context, this, true));
        this.withRenderLayer(new ShellbackSaddleLayer<>(context, this, false));
        this.withRenderLayer(HangingChestLayer.shellbackChest(context, this, 0));
        this.withRenderLayer(HangingChestLayer.shellbackChest(context, this, 1));
        this.withRenderLayer(HangingChestLayer.shellbackChest(context, this, 2));
        this.withRenderLayer(HangingChestLayer.shellbackChest(context, this, 3));
        this.withScale(5f);
        this.shadowRadius = 1.5F;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return Aquamirae.id("textures/entity/shellback.png");
    }
}
