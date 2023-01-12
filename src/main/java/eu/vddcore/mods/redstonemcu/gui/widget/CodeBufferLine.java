package eu.vddcore.mods.redstonemcu.gui.widget;

public class CodeBufferLine {
    public String text = "";
    public int caretPosition = 0;

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

    public void setText(String text) {
        this.text = text;
    }

    public String textFromCaretOnwards() {
        return text.substring(caretPosition);
    }

    public String textUntilCaret() {
        return text.substring(0, caretPosition);
    }

    public void removeAtCaret() {
        removeAt(caretPosition--);
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

    public void mergeWith(String text) {
        this.text += text;
    }
}
