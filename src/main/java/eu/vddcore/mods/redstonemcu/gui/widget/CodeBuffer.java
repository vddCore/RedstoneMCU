package eu.vddcore.mods.redstonemcu.gui.widget;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CodeBuffer {

    private final WCodeEditor editor;
    private final ArrayList<CodeBufferLine> lines;

    private int currentLineIndex;
    private int currentWindowTop;

    public CodeBuffer(WCodeEditor editor) {
        this.editor = editor;

        lines = new ArrayList<>();
        lines.add(new CodeBufferLine());

        currentLineIndex = 0;
    }

    public CodeBufferLine getLine(int i) {
        return lines.get(i);
    }

    public int getLineCount() {
        return lines.size();
    }

    public void moveWindowDown() {
        if (currentWindowTop >= lines.size()) return;
        currentWindowTop++;
    }

    public void moveWindowUp() {
        if (currentWindowTop <= 0) return;
        currentWindowTop--;
    }

    public void setWindowTop(int top) {
        if (top >= lines.size()) return;
        this.currentWindowTop = top;
    }

    public int getWindowTop() {
        return currentWindowTop;
    }

    public boolean isAtLastLine() {
        return currentLineIndex >= lines.size() - 1;
    }

    public boolean isAtFirstLine() {
        return currentLineIndex <= 0;
    }

    public CodeBufferLine goToNextLine() {
        if (currentLineIndex + 1 < lines.size())
            currentLineIndex++;

        return getCurrentLine();
    }

    public CodeBufferLine goToPreviousLine() {
        if (currentLineIndex - 1 >= 0)
            currentLineIndex--;

        return getCurrentLine();
    }

    public void updateViewport() {
        while (currentLineIndex - currentWindowTop >= editor.getMaxDisplayableLines()) {
            moveWindowDown();
            editor.forceMoveScrollBar(currentWindowTop);
        }

        while (currentLineIndex - currentWindowTop < 0) {
            moveWindowUp();
            editor.forceMoveScrollBar(currentWindowTop);
        }
    }

    public void insertNewLineAfterCurrent(boolean goToNext) {
        lines.add(currentLineIndex + 1, new CodeBufferLine());
        if (goToNext) currentLineIndex++;
    }

    public void insertNewLineBeforeCurrent(boolean goToNext) {
        lines.add(currentLineIndex, new CodeBufferLine());
        if (goToNext) currentLineIndex++;
    }

    public void removeLine() {
        lines.remove(currentLineIndex);
        goToPreviousLine();
    }

    public CodeBufferLine getCurrentLine() {
        return lines.get(currentLineIndex);
    }

    public int getCurrentLineIndex() {
        return currentLineIndex;
    }

    public String getText() {
        return lines.stream().map(CodeBufferLine::getText).collect(Collectors.joining("\n"));
    }
}
