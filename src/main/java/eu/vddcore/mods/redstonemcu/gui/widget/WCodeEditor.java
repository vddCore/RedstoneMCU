package eu.vddcore.mods.redstonemcu.gui.widget;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class WCodeEditor extends WWidget {
    private final int CARET_BLINK_THRESHOLD = 10;
    private final int CARET_THICKNESS = 1;

    private final int EDITOR_BACKGROUND = 0xFF1E1E1E;
    private final int EDITOR_FOREGROUND = 0xFFAAAAAA;
    private final int EDITOR_CARET_COLOR = 0xFFBBBBBB;

    private final int LINE_SPACING = 1;
    private final int MARGIN = 2;

    private final CodeBuffer buffer;
    private int caretTicker = 0;
    private boolean drawCaret = false;

    public WCodeEditor() {
        buffer = new CodeBuffer();
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int lineCount = buffer.getLineCount();

        ScreenDrawing.coloredRect(x, y, getWidth(), getHeight(), EDITOR_BACKGROUND);

        for (int i = 0; i < lineCount; i++) {
            CodeBufferLine line = buffer.getLine(i);
            ScreenDrawing.drawString(matrices, line.text, x + MARGIN, y + (i * textRenderer.fontHeight) + LINE_SPACING, EDITOR_FOREGROUND);
        }

        if (drawCaret) {
            int caretX = textRenderer.getWidth(buffer.getCurrentLine().textUntilCaret());
            int caretY = buffer.getCurrentLineIndex() * textRenderer.fontHeight;

            ScreenDrawing.coloredRect(x + caretX + MARGIN, y + caretY + MARGIN - 2, CARET_THICKNESS, textRenderer.fontHeight, EDITOR_CARET_COLOR);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (caretTicker > CARET_BLINK_THRESHOLD) {
            caretTicker = 0;
            drawCaret = !drawCaret;
        } else {
            caretTicker++;
        }
    }

    @Override
    public void onClick(int x, int y, int button) {
        super.onClick(x, y, button);

        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            requestFocus();
        }
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        super.onKeyPressed(ch, key, modifiers);

        caretTicker = 0;
        drawCaret = true;

        switch (ch) {
            case GLFW.GLFW_KEY_LEFT:
                handleCursorLeft();
                break;
            case GLFW.GLFW_KEY_RIGHT:
                handleCursorRight();
                break;
            case GLFW.GLFW_KEY_DOWN:
                handleCursorDown();
                break;
            case GLFW.GLFW_KEY_UP:
                handleCursorUp();
                break;
            case GLFW.GLFW_KEY_BACKSPACE:
                handleBackspace();
                break;
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                handleEnter();
                break;
        }
    }

    @Override
    public void onCharTyped(char ch) {
        handlePrintable(ch);
    }

    private void handleCursorLeft() {
        CodeBufferLine currentLine = buffer.getCurrentLine();
        if (currentLine.isCaretAtStart()) {
            if (buffer.isAtFirstLine()) return;
            buffer.previousLine();
        } else {
            currentLine.moveCaretLeft();
        }
    }

    private void handleCursorRight() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (currentLine.isCaretAtEnd()) {
            if (buffer.isAtLastLine()) return;

            buffer.nextLine();
        } else {
            currentLine.moveCaretRight();
        }
    }

    private void handleCursorUp() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (buffer.isAtFirstLine()) {
            currentLine.goToStart();
        } else {
            buffer.previousLine();
        }
    }

    private void handleCursorDown() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (buffer.isAtLastLine()) {
            currentLine.goToEnd();
        } else {
            buffer.nextLine();
        }
    }

    private void handleBackspace() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (currentLine.isCaretAtStart()) {
            if (buffer.isAtFirstLine()) return;

            String text = currentLine.text;
            buffer.removeLine();
            currentLine = buffer.getCurrentLine();
            currentLine.goToEnd();
            currentLine.mergeWith(text);
        } else {
            currentLine.removeAtCaret();
        }
    }

    private void handleEnter() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (currentLine.isCaretAtStart()) {
            buffer.insertNewLineBeforeCurrent();
        } else if (currentLine.isCaretAtEnd()) {
            buffer.insertNewLineAfterCurrent();
        } else {
            String text = currentLine.textFromCaretOnwards();
            currentLine.setText(currentLine.textUntilCaret());

            buffer.insertNewLineAfterCurrent();
            currentLine = buffer.getCurrentLine();
            currentLine.mergeWith(text);
        }
    }

    private void handlePrintable(char c) {
        CodeBufferLine currentLine = buffer.getCurrentLine();
        currentLine.insertAtCaret(c);
    }
}
