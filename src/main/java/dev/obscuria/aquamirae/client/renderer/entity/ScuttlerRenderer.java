package dev.obscuria.aquamirae.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.model.ModelScuttler;
import dev.obscuria.aquamirae.client.renderer.entity.state.ScuttlerRenderState;
import dev.obscuria.aquamirae.common.entity.Scuttler;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import com.geckolib.cache.model.GeoBone;
import com.geckolib.loading.math.MathParser;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.PerBoneRender;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.BlockAndItemGeoLayer;

import java.util.List;
import java.util.function.BiConsumer;

public class ScuttlerRenderer extends GeoEntityRenderer<Scuttler, ScuttlerRenderState> {

    public ScuttlerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelScuttler());
        this.withRenderLayer(new StolenItemLayer(context, this));
        this.shadowRadius = 0.2F;
    }

    @Override
    public ScuttlerRenderState createRenderState(Scuttler animatable, @Nullable Void relatedObject) {
        return new ScuttlerRenderState();
    }

    @Override
    public void extractRenderState(Scuttler entity, ScuttlerRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.snowOffset = 0.125f * Mth.lerp(partialTick, entity.localInSnowO, entity.localInSnow);
        state.waterOffset = 0.33f * Mth.lerp(partialTick, entity.localInWaterO, entity.localInWater);
    }

    @Override
    public void addRenderData(Scuttler animatable, @Nullable Void relatedObject, ScuttlerRenderState renderState, float partialTick) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick);
        MathParser.setVariable(
                AquamiraeClient.MOLANG_SCUTTLER_WALK,
                state -> renderState.walkAnimationPos * 0.25);
    }

    @Override
    public void preRenderPass(RenderPassInfo<ScuttlerRenderState> renderPassInfo, SubmitNodeCollector renderTasks) {
        var state = renderPassInfo.renderState();
        renderPassInfo.poseStack().translate(0, state.snowOffset + state.waterOffset, 0);
    }

    @Override
    public Identifier getTextureLocation(ScuttlerRenderState state) {
        return Aquamirae.id("textures/entity/scuttler.png");
    }

    private static class StolenItemLayer extends BlockAndItemGeoLayer<Scuttler, Void, ScuttlerRenderState> {

        private static final String SEGMENT_2 = "segment_2";

        StolenItemLayer(EntityRendererProvider.Context context, GeoRenderer<Scuttler, Void, ScuttlerRenderState> renderer) {
            super(context, renderer);
        }

        @Override
        protected List<RenderData> getRelevantBones(Scuttler animatable, @Nullable Void relatedObject, ScuttlerRenderState renderState, float partialTick) {
            return renderState.getOrDefaultGeckolibData(CONTENTS, List.of());
        }

        @Override
        public void addRenderData(Scuttler animatable, @Nullable Void relatedObject, ScuttlerRenderState renderState, float partialTick) {
            ItemStack stack = animatable.getStolenItem();
            if (stack.isEmpty()) {
                renderState.addGeckolibData(CONTENTS, List.of());
                return;
            }
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForLiving(itemState, stack, ItemDisplayContext.FIXED, animatable);
            renderState.addGeckolibData(CONTENTS, List.of(RenderData.item(SEGMENT_2, ItemDisplayContext.FIXED, itemState)));
        }

        @Override
        public void addPerBoneRender(RenderPassInfo<ScuttlerRenderState> renderPassInfo, BiConsumer<GeoBone, PerBoneRender<ScuttlerRenderState>> consumer) {
            if (!renderPassInfo.willRender()) return;
            for (RenderData renderData : renderPassInfo.renderState().getOrDefaultGeckolibData(CONTENTS, List.of())) {
                renderData.modelState().left().ifPresent(stack -> {
                    if (stack.isEmpty()) return;
                    renderPassInfo.model().getBone(renderData.boneName())
                            .ifPresent(bone -> consumer.accept(bone, (pass, bone2, tasks) -> submitForBone(pass, tasks, stack)));
                });
            }
        }

        private void submitForBone(RenderPassInfo<ScuttlerRenderState> renderPassInfo, SubmitNodeCollector renderTasks, ItemStackRenderState stack) {
            var poseStack = renderPassInfo.poseStack();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.0F, 0.3F, 0.0F);
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
            stack.submit(poseStack, renderTasks, renderPassInfo.packedLight(), OverlayTexture.NO_OVERLAY, 0);
        }
    }
}
