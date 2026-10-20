package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelShellbackSaddle;
import dev.obscuria.aquamirae.common.entity.Shellback;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.cache.model.GeoBone;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.PerBoneRender;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ShellbackSaddleLayer<T extends Shellback & GeoAnimatable, R extends GeoRenderState> extends GeoRenderLayer<T, Void, R> {

    private static final DataTicket<Integer> SADDLE_COUNT =
            DataTicket.create("aquamirae_shellback_saddle_count", new TypeToken<>() {});

    public static final Identifier TEXTURE = Aquamirae.id("textures/entity/shellback_saddle.png");

    protected final String boneName;
    protected final int requiredAmount;
    protected final ModelShellbackSaddle model;

    public ShellbackSaddleLayer(
            EntityRendererProvider.Context context,
            GeoRenderer<T, Void, R> entityRendererIn,
            boolean mainSaddle
    ) {
        super(entityRendererIn);
        this.boneName = mainSaddle ? "main_saddle" : "extra_saddle";
        this.requiredAmount = mainSaddle ? 1 : 2;
        this.model = new ModelShellbackSaddle(context.bakeLayer(AquamiraeModelLayers.SHELLBACK_SADDLE));
    }

    @Override
    public void addRenderData(T animatable, @Nullable Void relatedObject, R renderState, float partialTick) {
        renderState.addGeckolibData(SADDLE_COUNT, animatable.getSaddleCount());
    }

    @Override
    public void addPerBoneRender(RenderPassInfo<R> renderPassInfo, BiConsumer<GeoBone, PerBoneRender<R>> consumer) {
        if (!renderPassInfo.willRender()) return;
        if (renderPassInfo.renderState().getOrDefaultGeckolibData(SADDLE_COUNT, 0) < this.requiredAmount) return;

        renderPassInfo.model().getBone(this.boneName)
                .ifPresent(bone -> consumer.accept(bone, this::submitForBone));
    }

    private void submitForBone(RenderPassInfo<R> renderPassInfo, GeoBone bone, SubmitNodeCollector renderTasks) {
        final PoseStack poseStack = renderPassInfo.poseStack();

        poseStack.pushPose();
        poseStack.scale(-0.25f, -0.25f, 0.25f);
        renderTasks.submitModel(this.model, null, poseStack,
                this.model.renderType(TEXTURE),
                renderPassInfo.packedLight(), renderPassInfo.packedOverlay(), -1, null, -1, null);
        poseStack.popPose();
    }
}
