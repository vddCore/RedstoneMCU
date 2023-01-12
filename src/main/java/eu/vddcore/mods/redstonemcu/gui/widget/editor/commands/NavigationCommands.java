package eu.vddcore.mods.redstonemcu.gui.widget.editor.commands;

import eu.vddcore.mods.redstonemcu.gui.widget.editor.CodeBufferLine;
import eu.vddcore.mods.redstonemcu.gui.widget.editor.KeyCommand;
import eu.vddcore.mods.redstonemcu.gui.widget.editor.KeyCommandRegistry;
import org.lwjgl.glfw.GLFW;

public class NavigationCommands {

    public static void registerAll() {
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_LEFT, moveCursorLeft);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_RIGHT, moveCursorRight);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_UP, moveCursorUp);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_DOWN, moveCursorDown);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_HOME, goToLineStart);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_END, goToLineEnd);
        KeyCommandRegistry.bind(true, false, false, GLFW.GLFW_KEY_HOME, goToDocumentStart);
        KeyCommandRegistry.bind(true, false, false, GLFW.GLFW_KEY_END, goToDocumentEnd);
    }

    private static final KeyCommand moveCursorLeft = new KeyCommand((editor, buffer) -> {
        CodeBufferLine currentLine = buffer.getCurrentLine();
        if (currentLine.isCaretAtStart()) {
            if (buffer.isAtFirstLine()) return;
            currentLine = buffer.goToPreviousLine();
            currentLine.goToEnd();
        } else {
            currentLine.moveCaretLeft();
        }
    });

    public static final KeyCommand moveCursorRight = new KeyCommand((editor, buffer) -> {
        CodeBufferLine currentLine = buffer.getCurrentLine();

        if (currentLine.isCaretAtEnd()) {
            if (buffer.isAtLastLine()) return;
            currentLine = buffer.goToNextLine();
            currentLine.goToStart();
        } else {
            currentLine.moveCaretRight();
        }
    });

    public static KeyCommand moveCursorUp = new KeyCommand((editor, buffer) -> {
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
    });

    public static KeyCommand moveCursorDown = new KeyCommand((editor, buffer) -> {
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
    });

    public static KeyCommand goToLineStart = new KeyCommand((editor, buffer) -> {
        buffer.getCurrentLine().goToStart();
    });

    public static KeyCommand goToLineEnd = new KeyCommand((editor, buffer) -> {
       buffer.getCurrentLine().goToEnd();
    });

    public static KeyCommand goToDocumentStart = new KeyCommand((editor, buffer) -> {
        buffer.setCurrentLineIndex(0);
    });

    public static KeyCommand goToDocumentEnd = new KeyCommand((editor, buffer) -> {
        buffer.setCurrentLineIndex(buffer.getLineCount() - 1);
    });
}
