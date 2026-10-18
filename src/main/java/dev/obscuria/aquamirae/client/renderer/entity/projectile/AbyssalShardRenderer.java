package dev.obscuria.aquamirae.client.renderer.entity.projectile;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelShard;
import dev.obscuria.aquamirae.client.renderer.entity.state.ShardRenderState;
import dev.obscuria.aquamirae.common.entity.projectile.AbyssalShard;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class AbyssalShardRenderer<T extends AbyssalShard> extends ShardRenderer<T, ShardRenderState> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/projectile/abyssal_shard.png");

    public AbyssalShardRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelShard(context.bakeLayer(AquamiraeModelLayers.ABYSSAL_SHARD)));
    }

    @Override
    public ShardRenderState createRenderState() {
        return new ShardRenderState();
    }

    @Override
    public Identifier getTextureLocation(ShardRenderState state) {
        return TEXTURE;
    }
}
