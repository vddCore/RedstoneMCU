package eu.vddcore.mods.redstonemcu.gui.widget;

import eu.vddcore.mods.redstonemcu.gui.widget.editor.*;
import eu.vddcore.mods.redstonemcu.gui.widget.editor.commands.EditionCommands;
import eu.vddcore.mods.redstonemcu.gui.widget.editor.commands.NavigationCommands;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.Scissors;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class WCodeEditor extends WPanel {
    private final CodeBuffer buffer;
    private final WNonFocusableScrollBar verticalScrollBar;
    private final TextRenderer textRenderer;

    private int caretTicker = 0;
    private boolean drawCaret = false;
    private int currentBorderColor;

    public final EditorColors colors;
    public final EditorOptions options;

    public WCodeEditor() {
        super();

        colors = new EditorColors();
        options = new EditorOptions();
        buffer = new CodeBuffer(this);

        textRenderer = MinecraftClient.getInstance().textRenderer;

        NavigationCommands.registerAll();
        EditionCommands.registerAll();

        currentBorderColor = colors.borderNormal;

        verticalScrollBar = new WNonFocusableScrollBar(Axis.VERTICAL);
        verticalScrollBar.setValue(0);

        children.add(verticalScrollBar);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    public String getText() {
        return buffer.getText();
    }

    public int getMaxDisplayableLines() {
        int heightPerLine = textRenderer.fontHeight + options.lineSpacing;
        int usableHeight = getHeight() - options.verticalMargin * 2 - options.borderThickness * 2;

        return usableHeight / heightPerLine;
    }

    public void scrollTo(int value) {
        verticalScrollBar.setValue(value);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        buffer.setWindowTop(verticalScrollBar.getValue());

        int caretX = textRenderer.getWidth(buffer.getCurrentLine().textUntilCaret());
        int caretY = (buffer.getCurrentLineIndex() - buffer.getWindowTop()) * (textRenderer.fontHeight + options.lineSpacing);

        ScreenDrawing.coloredRect(x, y, getWidth(), getHeight(), currentBorderColor);
        ScreenDrawing.coloredRect(
            x + options.borderThickness,
            y + options.borderThickness,
            getWidth() - options.borderThickness * 2 - verticalScrollBar.getWidth(),
            getHeight() - options.borderThickness * 2,
            colors.background
        );

        Scissors.push(
            x + options.borderThickness + options.horizontalMargin - 1,
            y + options.borderThickness + options.verticalMargin,
            getWidth() - options.borderThickness * 2 - options.horizontalMargin * 2,
            getHeight() - options.borderThickness * 2 - options.verticalMargin * 2
        );

        if (options.highlightCurrentLine) {
            ScreenDrawing.coloredRect(
                x + options.horizontalMargin + options.borderThickness,
                y + caretY + options.verticalMargin + options.borderThickness - 1,
                getWidth() - (options.horizontalMargin + options.borderThickness) * 2 - verticalScrollBar.getWidth(),
                textRenderer.fontHeight + 1,
                colors.lineHighlighter
            );
        }

        int maxDisplayable = getMaxDisplayableLines();
        for (int i = 0; i < maxDisplayable; i++) {
            CodeBufferLine line;
            try {
                line = buffer.getLine(i + buffer.getWindowTop());
            } catch (Exception e) {
                break;
            }

            ScreenDrawing.drawString(
                matrices,
                line.getText(),
                x + options.horizontalMargin + options.borderThickness + 1,
                y + (i * (textRenderer.fontHeight + options.lineSpacing)) + options.verticalMargin + options.borderThickness + 1,
                colors.foreground
            );
        }

        if (drawCaret && isFocused()) {
            ScreenDrawing.coloredRect(
                x + caretX + options.horizontalMargin + options.borderThickness,
                y + caretY + options.verticalMargin + options.borderThickness - 1,
                options.caretThickness,
                textRenderer.fontHeight + 1,
                colors.caret
            );
        }
        Scissors.pop();

        super.paint(matrices, x, y, mouseX, mouseY);
    }

    @Override
    public void tick() {
        super.tick();

        if (caretTicker > options.caretBlinkingFrequency) {
            caretTicker = 0;
            drawCaret = !drawCaret;
        } else {
            caretTicker++;
        }

        verticalScrollBar.setWindow(
            textRenderer.fontHeight +
                options.verticalMargin +
                options.borderThickness +
                options.lineSpacing + 2
        );
    }

    @Override
    public void validate(GuiDescription host) {
        super.validate(host);

        verticalScrollBar.setSize(18, getHeight());
        verticalScrollBar.setLocation(getWidth() - verticalScrollBar.getWidth(), 0);
        verticalScrollBar.setMaxValue(buffer.getLineCount());
    }

    @Override
    public void onClick(int x, int y, int button) {
        super.onClick(x, y, button);

        int clickableArea = options.borderThickness + options.verticalMargin;
        if (y > clickableArea && y < getHeight() - clickableArea * 2) {
            int index = y / textRenderer.fontHeight - 1;

            if (index < 0)
                index = 0;

            if (index < buffer.getLineCount()) {
                buffer.setCurrentLineIndex(buffer.getWindowTop() + index);
            }
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            requestFocus();
        }
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        super.onKeyPressed(ch, key, modifiers);

        caretTicker = 0;
        drawCaret = true;

        KeyCommand cmd = KeyCommandRegistry.get(modifiers, ch);

        if (cmd != null)
            cmd.execute(this, buffer);

        verticalScrollBar.setMaxValue(buffer.getLineCount());
        buffer.updateViewport();
    }

    @Override
    public WWidget cycleFocus(boolean lookForwards) {
        handleTab(!lookForwards);
        return this;
    }

    private void handleTab(boolean shiftPressed) {
        int modifier = 0;

        if (shiftPressed)
            modifier = GLFW.GLFW_MOD_SHIFT;

        KeyCommand cmd = KeyCommandRegistry.get(modifier, GLFW.GLFW_KEY_TAB);
        if (cmd != null) cmd.execute(this, buffer);
    }

    @Override
    public void onCharTyped(char ch) {
        handlePrintable(ch);
    }

    @Override
    public void onFocusGained() {
        super.onFocusGained();
        currentBorderColor = colors.borderFocused;
    }

    @Override
    public void onFocusLost() {
        super.onFocusLost();
        currentBorderColor = colors.borderNormal;
    }

    @Override
    public void onMouseScroll(int x, int y, double amount) {
        super.onMouseScroll(x, y, amount);

        if (amount > 0) {
            scrollTo(verticalScrollBar.getValue() - 1);
        } else if (amount < 0) {
            scrollTo(verticalScrollBar.getValue() + 1);
        }
    }

    private void handlePrintable(char c) {
        CodeBufferLine currentLine = buffer.getCurrentLine();
        currentLine.insertAtCaret(c);
    }
}
