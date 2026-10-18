package dev.obscuria.aquamirae.integration.hook.client;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class HookLogoRenderer {

    private static final Identifier LOGO = Aquamirae.id("textures/gui/title/logo.png");

    public static void renderLogo(GuiGraphicsExtractor graphics, int x, int y, float uOffset, float vOffset) {
        // MC 26.1.2: GuiGraphics was replaced by GuiGraphicsExtractor and blit
        // requires the render pipeline up front.
        graphics.blit(RenderPipelines.GUI_TEXTURED, LOGO, x + 10, y, uOffset, vOffset, 108, 24, 108, 24);
    }
}
