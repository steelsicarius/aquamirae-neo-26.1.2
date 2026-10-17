package dev.obscuria.aquamirae.integration.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.obscuria.aquamirae.integration.hook.client.HookLogoRenderer;
import dev.obscuria.aquamirae.config.early.EarlyConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LogoRenderer.class)
public abstract class MixinLogoRenderer {

    @WrapOperation(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/Identifier;IIFFIIII)V", ordinal = 1))
    private void renderLogo$modifyBlit(
            GuiGraphics instance, Identifier texture,
            int x, int y, float uOffset, float vOffset, int width,
            int height, int textureWidth, int textureHeight,
            Operation<Void> original
    ) {
        if (EarlyConfig.THEMED_MAIN_MENU.get()) HookLogoRenderer.renderLogo(instance, x, y, uOffset, vOffset);
        else original.call(instance, texture, x, y, uOffset, vOffset, width, height, textureWidth, textureHeight);
    }
}