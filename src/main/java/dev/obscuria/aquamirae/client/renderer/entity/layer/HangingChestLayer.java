package dev.obscuria.aquamirae.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.obscuria.aquamirae.client.model.AquamiraeModelLayers;
import dev.obscuria.aquamirae.client.model.ModelHangingChest;
import dev.obscuria.aquamirae.common.entity.Shellback;
import dev.obscuria.aquamirae.common.entity.ShellbackChestType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import com.geckolib.cache.model.GeoBone;
import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.layer.GeoRenderLayer;
import com.geckolib.util.RenderUtil;

import java.util.function.Predicate;

public class HangingChestLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

    private final String boneName;
    private final boolean mirrored;
    private final Predicate<T> visibilityPredicate;
    private final ModelHangingChest model;

    public static <T extends Shellback> HangingChestLayer<T> shellbackChest(
            EntityRendererProvider.Context context,
            GeoRenderer<T> entityRendererIn, int index
    ) {
        var chestType = ShellbackChestType.values()[index];
        return new HangingChestLayer<>(
                context, entityRendererIn,
                chestType.name, chestType.mirrored,
                entity -> entity.hasChest(index));
    }

    public HangingChestLayer(
            EntityRendererProvider.Context context,
            GeoRenderer<T> entityRendererIn,
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
    public void renderForBone(
            PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType,
            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
            int packedLight, int packedOverlay
    ) {
        if (!bone.name().equals(boneName) || !visibilityPredicate.test(animatable)) return;

        poseStack.pushPose();
        RenderUtil.translateAndRotateMatrixForBone(poseStack, bone);
        poseStack.mulPose(Axis.YP.rotationDegrees(mirrored ? -90 : 90));
        poseStack.scale(-0.25f, -0.25f, 0.25f);
        this.model.renderToBuffer(
                poseStack, bufferSource.getBuffer(model.renderType(getTexture())),
                packedLight, packedOverlay, -1);
        poseStack.popPose();

        bufferSource.getBuffer(renderType);
    }

    protected Identifier getTexture() {
        return Identifier.withDefaultNamespace("textures/entity/horse/donkey.png");
    }
}
