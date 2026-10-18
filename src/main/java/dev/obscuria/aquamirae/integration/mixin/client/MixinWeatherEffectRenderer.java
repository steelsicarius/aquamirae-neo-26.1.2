package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.obscuria.aquamirae.client.BlizzardContext;
import dev.obscuria.aquamirae.config.ClientConfig;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC 26.1.2: LevelRenderer.renderSnowAndRain was replaced by the instanced
 * WeatherEffectRenderer (extract + render phases). The blizzard hooks moved
 * here: wind updates on extract, rain texture scroll speed scales with
 * BlizzardContext.speedFactor, and rain streak quads are slanted by the wind
 * (top vertices of each quad are offset, matching the old per-vertex logic).
 */
@Mixin(WeatherEffectRenderer.class)
public abstract class MixinWeatherEffectRenderer {

    @Unique
    private int aquamirae$vertexIndex = 0;

    @Inject(require = 0,
            method = "extractRenderState(Lnet/minecraft/world/level/Level;IFLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/level/WeatherRenderState;)V",
            at = @At("HEAD"))
    private void extractRenderState$onHead(
            Level level, int ticks, float partialTicks, Vec3 cameraPos,
            WeatherRenderState renderState, CallbackInfo ci
    ) {
        aquamirae$vertexIndex = 0;
        if (!ClientConfig.BLIZZARD_CUSTOM_PARTICLES.get()) return;
        float roughDelta = Math.max(partialTicks / 20f, 0.001f);
        BlizzardContext.update(cameraPos.x, cameraPos.y, cameraPos.z, roughDelta);
    }

    @ModifyExpressionValue(require = 0, method = "createRainColumnInstance",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"))
    private float createRainColumnInstance$modifySpeed(float speed) {
        if (!ClientConfig.BLIZZARD_CUSTOM_PARTICLES.get()) return speed;
        if (!BlizzardContext.active) return speed;
        return speed * BlizzardContext.speedFactor;
    }

    @Redirect(require = 0, method = "renderInstances",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;addVertex(FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer renderInstances$modifyVertex(
            VertexConsumer instance, float x, float y, float z
    ) {
        if (ClientConfig.BLIZZARD_CUSTOM_PARTICLES.get() && BlizzardContext.active) {
            int idx = aquamirae$vertexIndex % 4;
            if (idx == 0 || idx == 1) {
                x += BlizzardContext.windX;
                z += BlizzardContext.windZ;
            }
        }
        aquamirae$vertexIndex++;
        return instance.addVertex(x, y, z);
    }
}
