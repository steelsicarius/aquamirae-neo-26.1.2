package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelMazeMother;
import dev.obscuria.aquamirae.client.renderer.entity.layer.OverlayLayer;
import dev.obscuria.aquamirae.client.renderer.entity.state.MazeMotherRenderState;
import dev.obscuria.aquamirae.common.entity.MazeMother;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class MazeMotherRenderer<T extends MazeMother> extends MobRenderer<T, MazeMotherRenderState, ModelMazeMother> {

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/maze_mother.png");
    public static final Identifier OVERLAY_TEXTURE = Aquamirae.id("textures/entity/maze_mother_overlay.png");

    public MazeMotherRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMazeMother(context.bakeLayer(AquamiraeModelLayers.MAZE_MOTHER)), 1f);
        this.addLayer(new OverlayLayer<>(this, OVERLAY_TEXTURE));
    }

    @Override
    public MazeMotherRenderState createRenderState() {
        return new MazeMotherRenderState();
    }

    @Override
    public void extractRenderState(T entity, MazeMotherRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.verticalMotion = entity.getVerticalMotion(partialTick);
    }

    @Override
    public Identifier getTextureLocation(MazeMotherRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(MazeMotherRenderState state, PoseStack poseStack) {
        poseStack.scale(5f, 5f, 5f);
        poseStack.translate(0F, -1F, 0F);
    }
}
