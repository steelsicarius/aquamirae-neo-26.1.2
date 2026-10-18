package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.config.early.EarlyConfig;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.CubeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * MC 26.1.2: the menu panorama moved from Screen/TitleScreen fields into
 * GuiRenderer, which renders a single fixed CubeMap field. Swapping the field
 * value while the panorama is being rendered themes every screen that shows
 * the rotating panorama background (this replaces the old MixinScreen and
 * MixinTitleScreen panorama hooks).
 */
@Mixin(GuiRenderer.class)
public abstract class MixinGuiRenderer {

    @ModifyExpressionValue(require = 0,
            method = "render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/render/GuiRenderer;cubeMap:Lnet/minecraft/client/renderer/CubeMap;"))
    private CubeMap aquamirae$modifyPanorama(CubeMap original) {
        return EarlyConfig.THEMED_MAIN_MENU.get()
                ? AquamiraeClient.PANORAMA
                : original;
    }
}
