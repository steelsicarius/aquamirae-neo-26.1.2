package dev.obscuria.aquamirae.client.screen.tooltip;

import dev.obscuria.aquamirae.common.component.Keepsake;
import dev.obscuria.fragmentum.v2.api.common.Color;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public record ClientKeepsakeTooltip(Component component) implements ClientTooltipComponent {

    private static final long START_TIME = System.currentTimeMillis();
    private static final String LANG_KEY = "tooltip.aquamirae.keepsake";
    private static final Color BASE_COLOR = Color.parse(16733525);

    public static ClientKeepsakeTooltip create(Keepsake keepsake) {
        return new ClientKeepsakeTooltip(Component.translatable(LANG_KEY, keepsake.owner()));
    }

    @Override
    public int getHeight(Font font) {
        return 12;
    }

    @Override
    public int getWidth(Font font) {
        return font.width(component);
    }

    @Override
    public void extractImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
        var timer = (System.currentTimeMillis() - START_TIME) / 500.0f;
        // TODO(port): the old Fragmentum HSV#shiftHue took a hue fraction; v2 works in degrees.
        var color = BASE_COLOR.shiftHue(54f * Mth.cos(timer)).argb();
        graphics.text(font, component, x, y, color);
    }
}
