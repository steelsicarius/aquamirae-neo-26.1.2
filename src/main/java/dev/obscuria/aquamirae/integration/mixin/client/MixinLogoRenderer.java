package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.obscuria.aquamirae.integration.hook.client.HookLogoRenderer;
import dev.obscuria.aquamirae.config.early.EarlyConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LogoRenderer.class)
public abstract class MixinLogoRenderer {

    /**
     * MC 26.1.2: LogoRenderer.renderLogo was replaced by extractRenderState,
     * which blits the logo (first blit) and the edition tag (second). We wrap
     * the logo blit (ordinal 0) to draw the Aquamirae logo instead.
     */
    @WrapOperation(
            method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIIII)V",
                    ordinal = 0)
    )
    private void extractRenderState$modifyLogoBlit(
            GuiGraphicsExtractor instance, com.mojang.blaze3d.pipeline.RenderPipeline pipeline, Identifier texture,
            int x, int y, float uOffset, float vOffset, int width,
            int height, int textureWidth, int textureHeight, int color,
            Operation<Void> original
    ) {
        if (EarlyConfig.THEMED_MAIN_MENU.get()) {
            HookLogoRenderer.renderLogo(instance, x, y, uOffset, vOffset);
        } else {
            original.call(instance, pipeline, texture, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight, color);
        }
    }
}
