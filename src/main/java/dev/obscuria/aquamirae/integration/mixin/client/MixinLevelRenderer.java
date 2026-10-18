package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.client.event.ShipbreakerClientState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.DeltaTracker;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {

    @Shadow @Nullable private ClientLevel level;

    /**
     * MC 26.1.2: renderLevel was rebuilt around the extract/render phase model.
     * The buffer source is still handed out mid-method right before entity
     * submission, which is the closest equivalent of the old injection point.
     */
    @Inject(require = 0,
            method = "renderLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderBuffers;bufferSource()Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;"))
    private void renderLevel$onBufferSource(
            GraphicsResourceAllocator resourceAllocator, DeltaTracker deltaTracker, boolean renderOutline,
            CameraRenderState cameraState, org.joml.Matrix4fc modelViewMatrix,
            com.mojang.blaze3d.buffers.GpuBufferSlice terrainFog, org.joml.Vector4f fogColor,
            boolean shouldRenderSky, net.minecraft.client.renderer.chunk.ChunkSectionsToRender chunkSectionsToRender,
            CallbackInfo ci,
            @Local PoseStack poseStack,
            @Local Frustum frustum
    ) {
        if (level == null) return;
        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (camera == null) return;
        ShipbreakerClientState.render(
                poseStack, level, frustum, camera,
                Minecraft.getInstance().renderBuffers().bufferSource(),
                AquamiraeClient.partialTicks());
    }
}
