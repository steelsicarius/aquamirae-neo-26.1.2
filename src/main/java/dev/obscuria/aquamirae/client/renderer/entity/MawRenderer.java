package dev.obscuria.aquamirae.client.renderer.entity;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelMaw;
import dev.obscuria.aquamirae.client.renderer.entity.layer.MawItemLayer;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.client.renderer.entity.state.MawRenderState;
import dev.obscuria.aquamirae.common.entity.Maw;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.item.ItemDisplayContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class MawRenderer<T extends Maw> extends MobRenderer<T, MawRenderState, ModelMaw> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/maw.png");
    public static final Identifier OVERLAY_TEXTURE = Aquamirae.id("textures/entity/maw_overlay.png");

    public MawRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMaw(context.bakeLayer(AquamiraeModelLayers.MAW)), 0.9f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
        this.addLayer(new MawItemLayer(this));
    }

    @Override
    public MawRenderState createRenderState() {
        return new MawRenderState();
    }

    @Override
    public void extractRenderState(T entity, MawRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.attackState = entity.attackState;
        state.deathState = entity.deathState;
        ItemStack mouthItem = entity.getMouthItem();
        if (mouthItem.isEmpty()) {
            state.mouthItem.clear();
        } else {
            this.itemModelResolver.updateForLiving(state.mouthItem, mouthItem, ItemDisplayContext.FIXED, entity);
        }
    }

    @Override
    public Identifier getTextureLocation(MawRenderState state) {
        return TEXTURE;
    }
}
