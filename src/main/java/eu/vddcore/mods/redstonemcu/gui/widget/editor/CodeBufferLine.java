package eu.vddcore.mods.redstonemcu.gui.widget.editor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

public class CodeBufferLine {
    private final CodeBuffer buffer;

    private String text = "";
    private int caretPosition = 0;

    public CodeBufferLine(CodeBuffer buffer) {
        this.buffer = buffer;
    }

    public void insertAt(int i, char c) {
        String pre = text.substring(0, i);
        String post = text.substring(i);

        setText(pre + c + post);
    }

    public void removeAt(int i) {
        if (text.length() == 0 || i > text.length()) return;

        String pre = text.substring(0, i);
        String post = text.substring(i + 1);

        setText(pre + post);
    }

    public void appendText(String text) {
        this.text += text;
    }

    @Environment(EnvType.CLIENT)
    public int getTextPixelWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public int getCaretPosition() {
        return this.caretPosition;
    }

    public void setCaretPosition(int caretPosition) {
        this.caretPosition = caretPosition;
    }

    public char getCharAtCaret() {
        int position = caretPosition - 1;

        if (position < 0)
            position = 0;

        return text.charAt(position);
    }

    public char getCharBehindCaret() {
        int position = caretPosition - 2;

        if (position < 0)
            position = 0;

        return text.charAt(position);
    }

    public char getCharBeforeCaret() {
        int position = caretPosition + 1;

        if (position >= text.length())
            position = text.length() - 1;

        return text.charAt(position);
    }

    public String textFromCaretOnwards() {
        return text.substring(caretPosition);
    }

    public String textUntilCaret() {
        return text.substring(0, caretPosition);
    }

    public void removeAtCaret(boolean moveCaretPosition) {
        if (moveCaretPosition) --caretPosition;
        removeAt(caretPosition);
    }

    public void removeFromEnd() {
        text = text.substring(0, text.length() - 1);
    }

    public void insertAtCaret(char c) {
        insertAt(caretPosition++, c);
    }

    public void moveCaretLeft() {
        caretPosition--;
    }

    public void moveCaretRight() {
        caretPosition++;
    }

    public boolean isCaretAtEnd() {
        return caretPosition >= text.length();
    }

    public boolean isCaretAtStart() {
        return caretPosition <= 0;
    }

    public void goToEnd() {
        caretPosition = text.length();
    }

    public void goToStart() {
        caretPosition = 0;
    }

    public void indentAtCaret(int tabSize) {
        for (int i = 0; i < tabSize; i++) {
            insertAtCaret(' ');
        }
    }

    public void indent(int tabSize) {
        for (int i = 0; i < tabSize; i++) {
            insertAt(0, ' ');
        }
    }

    public void unindent(int tabSize) {
        for (int i = 0; i < tabSize && text.length() > 0; i++) {
            if (text.charAt(0) == ' ') {
                text = text.substring(1);
                moveCaretLeft();
            } else break;
        }
    }

    public int getIndentCount() {
        int indentCount = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ')
                indentCount++;
            else break;
        }

        return indentCount;
    }
}
