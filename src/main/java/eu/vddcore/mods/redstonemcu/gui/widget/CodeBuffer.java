package eu.vddcore.mods.redstonemcu.gui.widget;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CodeBuffer {
    private ArrayList<CodeBufferLine> lines;
    private int currentLineIndex;

    public CodeBuffer() {
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

    public boolean isAtLastLine() {
        return currentLineIndex >= lines.size();
    }

    public boolean isAtFirstLine() {
        return currentLineIndex <= 0;
    }

    public void nextLine() {
        if (currentLineIndex + 1 < lines.size())
            currentLineIndex++;
    }

    public void previousLine() {
        if (currentLineIndex - 1 >= 0)
            currentLineIndex--;
    }

    public void insertNewLineBeforeCurrent() {
        lines.add(currentLineIndex, new CodeBufferLine());
    }

    public void insertNewLineAfterCurrent() {
        lines.add(currentLineIndex + 1, new CodeBufferLine());
        nextLine();
    }

    public void removeLine() {
        lines.remove(currentLineIndex);
        previousLine();
    }

    public CodeBufferLine getCurrentLine() {
        return lines.get(currentLineIndex);
    }

    public int getCurrentLineIndex() {
        return currentLineIndex;
    }
}
