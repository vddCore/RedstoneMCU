package eu.vddcore.mods.redstonemcu.gui.widget.editor.commands;

import eu.vddcore.mods.redstonemcu.gui.widget.editor.CodeBufferLine;
import eu.vddcore.mods.redstonemcu.gui.widget.editor.KeyCommand;
import eu.vddcore.mods.redstonemcu.gui.widget.editor.KeyCommandRegistry;
import org.lwjgl.glfw.GLFW;

public class EditionCommands {
    public static void registerAll() {
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_BACKSPACE, backspace);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_DELETE, delete);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_ENTER, enter);
        KeyCommandRegistry.bind(false, false, false, GLFW.GLFW_KEY_TAB, indent);
        KeyCommandRegistry.bind(false, false, true, GLFW.GLFW_KEY_TAB, unindent);
    }

    private static final KeyCommand backspace = new KeyCommand((editor, buffer) -> {
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
    });

    private static final KeyCommand delete = new KeyCommand((editor, buffer) -> {
        CodeBufferLine line = buffer.getCurrentLine();

        if (line.isCaretAtEnd()) {
            if (buffer.isAtLastLine()) return;

            CodeBufferLine nextLine = buffer.goToNextLine();
            line.appendText(nextLine.getText());
            buffer.removeLine();
        } else {
            line.removeAtCaret(false);
        }
    });

    private static final KeyCommand enter = new KeyCommand((editor, buffer) -> {
        CodeBufferLine currentLine = buffer.getCurrentLine();
        int indentCount = currentLine.getIndentCount();

        if (currentLine.isCaretAtEnd()) {
            buffer.insertNewLineAfterCurrent(true);

            if (editor.options.autoIndent) {
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
    });

    private static final KeyCommand indent = new KeyCommand((editor, buffer) -> buffer.getCurrentLine().indentAtCaret(editor.options.tabSize));
    private static final KeyCommand unindent = new KeyCommand((editor, buffer) -> buffer.getCurrentLine().unindent(editor.options.tabSize));
}
