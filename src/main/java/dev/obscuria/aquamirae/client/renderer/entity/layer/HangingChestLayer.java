package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelHangingChest;
import dev.obscuria.aquamirae.common.entity.Shellback;
import dev.obscuria.aquamirae.common.entity.ShellbackChestType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
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
import java.util.function.Predicate;

public class HangingChestLayer<T extends Entity & GeoAnimatable, R extends GeoRenderState> extends GeoRenderLayer<T, Void, R> {

    private static final DataTicket<Boolean> VISIBLE =
            DataTicket.create("aquamirae_hanging_chest_visible", new TypeToken<>() {});

    private final String boneName;
    private final boolean mirrored;
    private final Predicate<T> visibilityPredicate;
    private final ModelHangingChest model;

    public static HangingChestLayer<Shellback, LivingEntityRenderState> shellbackChest(
            EntityRendererProvider.Context context,
            GeoRenderer<Shellback, Void, LivingEntityRenderState> entityRendererIn, int index
    ) {
        var chestType = ShellbackChestType.values()[index];
        return new HangingChestLayer<>(
                context, entityRendererIn,
                chestType.name, chestType.mirrored,
                entity -> entity.hasChest(index));
    }

    public HangingChestLayer(
            EntityRendererProvider.Context context,
            GeoRenderer<T, Void, R> entityRendererIn,
            String boneName, boolean mirrored,
            Predicate<T> visibilityPredicate
    ) {
        super(entityRendererIn);
        this.boneName = boneName;
        this.mirrored = mirrored;
        this.visibilityPredicate = visibilityPredicate;
        this.model = new ModelHangingChest(context.bakeLayer(AquamiraeModelLayers.HANGING_CHEST));
    }

    @Override
    public void addRenderData(T animatable, @Nullable Void relatedObject, R renderState, float partialTick) {
        renderState.addGeckolibData(VISIBLE, this.visibilityPredicate.test(animatable));
    }

    @Override
    public void addPerBoneRender(RenderPassInfo<R> renderPassInfo, BiConsumer<GeoBone, PerBoneRender<R>> consumer) {
        if (!renderPassInfo.willRender()) return;
        if (!renderPassInfo.renderState().getOrDefaultGeckolibData(VISIBLE, false)) return;

        renderPassInfo.model().getBone(this.boneName)
                .ifPresent(bone -> consumer.accept(bone, this::submitForBone));
    }

    private void submitForBone(RenderPassInfo<R> renderPassInfo, GeoBone bone, SubmitNodeCollector renderTasks) {
        final PoseStack poseStack = renderPassInfo.poseStack();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(this.mirrored ? -90 : 90));
        poseStack.scale(-0.25f, -0.25f, 0.25f);
        renderTasks.submitModel(this.model, null, poseStack,
                this.model.renderType(getTexture()),
                renderPassInfo.packedLight(), renderPassInfo.packedOverlay(), -1, null, null);
        poseStack.popPose();
    }

    protected Identifier getTexture() {
        return Identifier.withDefaultNamespace("textures/entity/horse/donkey.png");
    }
}
