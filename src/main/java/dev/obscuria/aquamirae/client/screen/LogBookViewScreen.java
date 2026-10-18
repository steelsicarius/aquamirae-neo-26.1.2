package dev.obscuria.aquamirae.client.screen;

import dev.obscuria.aquamirae.common.component.LogBookContents;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class LogBookViewScreen extends Screen {

    private static final Identifier BOOK_LOCATION = Identifier.withDefaultNamespace("textures/gui/book.png");

    private static final int IMAGE_WIDTH = 192;
    private static final int IMAGE_HEIGHT = 192;
    private static final int TEXT_WIDTH = 114;
    private static final int TEXT_HEIGHT = 144;
    private static final int LINE_HEIGHT = 9;
    private static final int TEXT_MARGIN_X = 36;
    private static final int TEXT_MARGIN_Y = 16;
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_GAP = 18;
    private static final int MIN_THUMB_HEIGHT = 16;

    private final Component content;

    private List<FormattedCharSequence> lines = Collections.emptyList();

    private int leftPos;
    private int topPos;

    private double scrollAmount;
    private int maxScroll;
    private boolean scrollbarGrabbed;
    private double scrollbarDragOffset;

    public LogBookViewScreen(ItemStack stack) {
        this(
                stack.getOrDefault(AquamiraeDataComponents.LOGBOOK_CONTENTS, LogBookContents.EMPTY).topic(),
                stack.getOrDefault(AquamiraeDataComponents.LOGBOOK_CONTENTS, LogBookContents.EMPTY).content());
    }

    public LogBookViewScreen(Component title, Component content) {
        super(title);
        this.content = content;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - IMAGE_WIDTH) / 2;
        this.topPos = (this.height - IMAGE_HEIGHT) / 2;

        this.lines = this.font.split(this.content, TEXT_WIDTH);

        int contentHeight = this.lines.size() * LINE_HEIGHT;
        this.maxScroll = Math.max(0, contentHeight - TEXT_HEIGHT);
        this.scrollAmount = Mth.clamp(this.scrollAmount, 0, this.maxScroll);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_LOCATION, this.leftPos, this.topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
        this.extractPage(graphics, mouseX, mouseY);
        this.extractScrollbar(graphics);
    }

    private void extractPage(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;

        graphics.enableScissor(textX, textY, textX + TEXT_WIDTH, textY + TEXT_HEIGHT);

        // Text is drawn through the active text collector so hover/click styling keeps working
        this.visitPage(graphics.textRenderer());

        graphics.disableScissor();
    }

    private void visitPage(ActiveTextCollector collector) {
        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;

        for (int i = 0; i < this.lines.size(); i++) {
            int lineY = (int) (textY + i * LINE_HEIGHT - this.scrollAmount);
            collector.accept(TextAlignment.LEFT, textX, lineY, this.lines.get(i));
        }
    }

    private void extractScrollbar(GuiGraphicsExtractor graphics) {
        if (this.maxScroll <= 0) {
            return;
        }

        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackX = textX + TEXT_WIDTH + SCROLLBAR_GAP;

        graphics.fill(trackX, textY, trackX + SCROLLBAR_WIDTH, textY + TEXT_HEIGHT, 0x33000000);

        int thumbHeight = this.getThumbHeight();
        int thumbY = this.getThumbY(thumbHeight);

        graphics.fill(trackX, thumbY, trackX + SCROLLBAR_WIDTH, thumbY + thumbHeight, 0xFF8B8B8B);
        graphics.fill(trackX, thumbY, trackX + SCROLLBAR_WIDTH - 1, thumbY + thumbHeight - 1, 0xFFD0D0D0);
    }

    @Nullable
    private Style getStyleAt(double mouseX, double mouseY) {
        var finder = new ActiveTextCollector.ClickableStyleFinder(this.font, (int) mouseX, (int) mouseY);
        this.visitPage(finder);
        return finder.result();
    }

    protected boolean handleClickEvent(@Nullable ClickEvent event) {
        if (event == null) {
            return false;
        }
        defaultHandleGameClickEvent(event, this.minecraft, this);
        return true;
    }

    private boolean isOverScrollbar(double mouseX, double mouseY) {
        if (this.maxScroll <= 0) {
            return false;
        }
        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackX = textX + TEXT_WIDTH + SCROLLBAR_GAP;
        return mouseX >= trackX && mouseX < trackX + SCROLLBAR_WIDTH
                && mouseY >= textY && mouseY < textY + TEXT_HEIGHT;
    }

    private int getThumbHeight() {
        return Math.max(MIN_THUMB_HEIGHT, (TEXT_HEIGHT * TEXT_HEIGHT) / (TEXT_HEIGHT + this.maxScroll));
    }

    private int getThumbY(int thumbHeight) {
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackRange = TEXT_HEIGHT - thumbHeight;
        if (trackRange <= 0 || this.maxScroll <= 0) {
            return textY;
        }
        return textY + (int) (trackRange * (this.scrollAmount / this.maxScroll));
    }

    private void setScrollFromThumbY(double thumbY, int thumbHeight) {
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackRange = TEXT_HEIGHT - thumbHeight;
        if (trackRange <= 0) {
            this.scrollAmount = 0;
            return;
        }
        double relative = (thumbY - textY) / (double) trackRange;
        this.scrollAmount = Mth.clamp(relative * this.maxScroll, 0, this.maxScroll);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0 && this.isOverScrollbar(event.x(), event.y())) {
            this.scrollbarGrabbed = true;

            int thumbHeight = this.getThumbHeight();
            int thumbY = this.getThumbY(thumbHeight);

            if (event.y() >= thumbY && event.y() < thumbY + thumbHeight) {
                this.scrollbarDragOffset = event.y() - thumbY;
            } else {
                this.scrollbarDragOffset = thumbHeight / 2.0;
                this.setScrollFromThumbY(event.y() - this.scrollbarDragOffset, thumbHeight);
            }
            return true;
        }

        Style style = this.getStyleAt(event.x(), event.y());
        if (style != null && this.handleClickEvent(style.getClickEvent())) {
            return true;
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (this.scrollbarGrabbed) {
            int thumbHeight = this.getThumbHeight();
            this.setScrollFromThumbY(event.y() - this.scrollbarDragOffset, thumbHeight);
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0) {
            this.scrollbarGrabbed = false;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.maxScroll > 0) {
            this.scrollAmount = Mth.clamp(this.scrollAmount - scrollY * LINE_HEIGHT * 3, 0, this.maxScroll);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int keyCode = event.key();
        if (this.maxScroll > 0) {
            if (keyCode == GLFW.GLFW_KEY_UP) {
                this.scrollAmount = Mth.clamp(this.scrollAmount - LINE_HEIGHT, 0, this.maxScroll);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_DOWN) {
                this.scrollAmount = Mth.clamp(this.scrollAmount + LINE_HEIGHT, 0, this.maxScroll);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
                this.scrollAmount = Mth.clamp(this.scrollAmount - TEXT_HEIGHT, 0, this.maxScroll);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
                this.scrollAmount = Mth.clamp(this.scrollAmount + TEXT_HEIGHT, 0, this.maxScroll);
                return true;
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
