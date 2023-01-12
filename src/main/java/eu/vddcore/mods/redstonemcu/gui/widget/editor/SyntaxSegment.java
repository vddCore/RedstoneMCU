package eu.vddcore.mods.redstonemcu.gui.widget.editor;

public class SyntaxSegment {
    public final int pixelWidth;
    public final int foregroundColor;
    public final String text;

    public SyntaxSegment(int pixelWidth, int foregroundColor, String text) {
        this.pixelWidth = pixelWidth;
        this.foregroundColor = foregroundColor;
        this.text = text;
    }
}
