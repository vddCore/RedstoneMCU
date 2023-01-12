package eu.vddcore.mods.redstonemcu.gui.widget;

import io.github.cottonmc.cotton.gui.client.Scissors;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class WCodeEditor extends WWidget {
    private final int CARET_BLINK_THRESHOLD = 10;
    private final int CARET_THICKNESS = 1;

    // --- these will eventually go as a settable things
    private final int EDITOR_BACKGROUND = 0xFF1E1E1E;
    private final int EDITOR_FOREGROUND = 0xFFCCCCCC;
    private final int EDITOR_CARET_COLOR = 0xFFCCCCCC;
    private final int EDITOR_BORDER_FOCUS_COLOR = 0xFF007ACC;
    private final int EDITOR_BORDER_NORMAL_COLOR = 0xFFFFFFFF;
    private final int EDITOR_LINE_HIGHLIGHT_COLOR = 0x11FFFFFF;

    private final int TAB_SIZE = 2;
    private final int BORDER_THICKNESS = 1;
    private final int LINE_SPACING = 1;
    private final int MARGIN_X = 4;
    private final int MARGIN_Y = 4;
    private final boolean AUTOINDENT = true;
    private final boolean HIGHLIGHT_CURRENT_LINE = true;
    // ---

    private final CodeBuffer buffer;

    private int caretTicker = 0;
    private boolean drawCaret = false;
    private int currentBorderColor = EDITOR_BORDER_NORMAL_COLOR;

    public WCodeEditor() {
        buffer = new CodeBuffer();
    }

    @Override
    public WWidget cycleFocus(boolean lookForwards) {
        handleTab(!lookForwards);
        return this;
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

    @Override
    @Environment(EnvType.CLIENT)
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int lineCount = buffer.getLineCount();

        ScreenDrawing.coloredRect(x, y, getWidth(), getHeight(), currentBorderColor);
        ScreenDrawing.coloredRect(
            x + BORDER_THICKNESS,
            y + BORDER_THICKNESS,
            getWidth() - BORDER_THICKNESS * 2,
            getHeight() - BORDER_THICKNESS * 2,
            EDITOR_BACKGROUND
        );

        Scissors.push(
            x + BORDER_THICKNESS + MARGIN_X - 1,
            y + BORDER_THICKNESS + MARGIN_Y,
            getWidth() - BORDER_THICKNESS * 2 - MARGIN_X * 2,
            getHeight() - BORDER_THICKNESS * 2 - MARGIN_Y * 2
        );

        if (HIGHLIGHT_CURRENT_LINE) {
            ScreenDrawing.coloredRect(
                x + MARGIN_X + BORDER_THICKNESS,
                (y + MARGIN_Y + BORDER_THICKNESS + LINE_SPACING * buffer.getCurrentLineIndex() + buffer.getCurrentLineIndex() * textRenderer.fontHeight) - 1,
                getWidth() - (MARGIN_X + BORDER_THICKNESS) * 2,
                textRenderer.fontHeight + 1,
                EDITOR_LINE_HIGHLIGHT_COLOR
            );
        }

        for (int i = 0; i < lineCount; i++) {
            CodeBufferLine line = buffer.getLine(i);
            ScreenDrawing.drawString(
                matrices,
                line.getText(),
                x + MARGIN_X + BORDER_THICKNESS + 1,
                y + (i * (textRenderer.fontHeight + LINE_SPACING)) + MARGIN_Y + BORDER_THICKNESS + 1,
                EDITOR_FOREGROUND
            );
        }

        if (drawCaret && isFocused()) {
            int caretX = textRenderer.getWidth(buffer.getCurrentLine().textUntilCaret());
            int caretY = buffer.getCurrentLineIndex() * (textRenderer.fontHeight + LINE_SPACING);

            ScreenDrawing.coloredRect(
                x + caretX + MARGIN_X + BORDER_THICKNESS,
                y + caretY + MARGIN_Y + BORDER_THICKNESS - 1,
                CARET_THICKNESS,
                textRenderer.fontHeight + 1,
                EDITOR_CARET_COLOR
            );
        }
        Scissors.pop();
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
            case GLFW.GLFW_KEY_DELETE:
                handleDelete();
                break;
            case GLFW.GLFW_KEY_HOME:
                buffer.getCurrentLine().goToStart();
                break;
            case GLFW.GLFW_KEY_END:
                buffer.getCurrentLine().goToEnd();
                break;
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                handleEnter();
                break;
            case GLFW.GLFW_KEY_TAB:
                // tab doesn't work at all.
                break;
        }
    }

    @Override
    public void onCharTyped(char ch) {
        handlePrintable(ch);
    }

    @Override
    public void onFocusGained() {
        super.onFocusGained();
        currentBorderColor = EDITOR_BORDER_FOCUS_COLOR;
    }

    @Override
    public void onFocusLost() {
        super.onFocusLost();
        currentBorderColor = EDITOR_BORDER_NORMAL_COLOR;
    }

    private void handleCursorLeft() {
        CodeBufferLine currentLine = buffer.getCurrentLine();
        if (currentLine.isCaretAtStart()) {
            if (buffer.isAtFirstLine()) return;
            currentLine = buffer.goToPreviousLine();
            currentLine.goToEnd();
        } else {
            currentLine.moveCaretLeft();
        }
    }

    private void handleCursorRight() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (currentLine.isCaretAtEnd()) {
            if (buffer.isAtLastLine()) return;
            currentLine = buffer.goToNextLine();
            currentLine.goToStart();
        } else {
            currentLine.moveCaretRight();
        }
    }

    private void handleCursorUp() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (buffer.isAtFirstLine()) {
            currentLine.goToStart();
        } else {
            int caretPosition = currentLine.getCaretPosition();
            boolean wasAtEnd = currentLine.isCaretAtEnd();

            currentLine = buffer.goToPreviousLine();

            if (!wasAtEnd) {
                if (caretPosition >= currentLine.getText().length()) {
                    currentLine.goToEnd();
                } else {
                    currentLine.setCaretPosition(caretPosition);
                }
            } else if (currentLine.getText().length() >= caretPosition) {
                currentLine.setCaretPosition(caretPosition);
            }
        }
    }

    private void handleCursorDown() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (buffer.isAtLastLine()) {
            currentLine.goToEnd();
        } else {
            int caretPosition = currentLine.getCaretPosition();
            boolean wasAtEnd = currentLine.isCaretAtEnd();

            currentLine = buffer.goToNextLine();

            if (!wasAtEnd) {
                if (caretPosition >= currentLine.getText().length()) {
                    currentLine.goToEnd();
                } else {
                    currentLine.setCaretPosition(caretPosition);
                }
            } else if (currentLine.getText().length() >= caretPosition) {
                currentLine.setCaretPosition(caretPosition);
            } else {
                currentLine.goToEnd();
            }
        }
    }

    private void handleBackspace() {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (currentLine.isCaretAtStart()) {
            if (buffer.isAtFirstLine()) return;

            String text = currentLine.getText();
            buffer.removeLine();
            currentLine = buffer.getCurrentLine();
            currentLine.goToEnd();
            currentLine.appendText(text);
        } else if (currentLine.isCaretAtEnd()) {
            currentLine.removeFromEnd();
            currentLine.moveCaretLeft();
        } else {
            currentLine.removeAtCaret(true);
        }
    }

    private void handleDelete() {
        CodeBufferLine line = buffer.getCurrentLine();

        if (line.isCaretAtEnd()) {
            if (buffer.isAtLastLine()) return;

            CodeBufferLine nextLine = buffer.goToNextLine();
            line.appendText(nextLine.getText());
            buffer.removeLine();
        } else {
            line.removeAtCaret(false);
        }
    }

    private void handleEnter() {
        CodeBufferLine currentLine = buffer.getCurrentLine();
        int indentCount = currentLine.getIndentCount();

        if (currentLine.isCaretAtEnd()) {
            buffer.insertNewLineAfterCurrent(true);

            if (AUTOINDENT) {
                buffer.getCurrentLine().indentAtCaret(indentCount);
            }
        } else if (currentLine.isCaretAtStart()) {
            buffer.insertNewLineBeforeCurrent(true);
        } else {
            String text = currentLine.textFromCaretOnwards();
            currentLine.setText(currentLine.textUntilCaret());

            buffer.insertNewLineAfterCurrent(true);
            currentLine = buffer.getCurrentLine();
            currentLine.appendText(text);
        }
    }

    private void handleTab(boolean shiftPressed) {
        if (!shiftPressed) {
            buffer.getCurrentLine().indentAtCaret(TAB_SIZE);
        } else {
            buffer.getCurrentLine().unindent(TAB_SIZE);
        }
    }

    private void handlePrintable(char c) {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        currentLine.insertAtCaret(c);
    }
}
