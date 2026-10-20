package dev.obscuria.aquamirae.integration.mixin.client;

import dev.obscuria.aquamirae.integration.hook.client.HookGameRenderer;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    // MC 26.1.2: bobView(CameraRenderState, PoseStack); the partial tick is on
    // the DeltaTracker now and the hook only applies the camera shake pose.
    @Inject(require = 0, method = "bobView", at = @At("HEAD"))
    private void bobView$onHead(CameraRenderState cameraState, PoseStack poseStack, CallbackInfo ci) {
        HookGameRenderer.bobView(poseStack);
    }

    /**
     * MC 26.1.2: the themed panorama CubeMap is created and registered with
     * vanilla's own (CubeMap allocates GPU buffers, so it can't live in a
     * mod-constructor static initializer).
     */
    @Inject(method = "registerPanoramaTextures", at = @At("TAIL"))
    private void registerPanoramaTextures$registerAquamirae(net.minecraft.client.renderer.texture.TextureManager textureManager, CallbackInfo ci) {
        AquamiraeClient.initPanorama(textureManager);
    }
}
