package eu.vddcore.mods.redstonemcu.gui.widget.editor;

import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

public class SyntaxHighlighter {
    private final static ArrayList<String> keywords = new ArrayList<>();

    static {
        keywords.add("mov");
        keywords.add("jmp");
        keywords.add("cmp");
        keywords.add("je");
        keywords.add("jne");
        keywords.add("push");
        keywords.add("pop");
    }

    public static ArrayList<SyntaxSegment> highlightLine(CodeBufferLine line) {
        ArrayList<SyntaxSegment> segments = new ArrayList<>();
        String text = line.getText();

        if (text.length() == 0)
            return segments;

        StringBuilder token = new StringBuilder();

        int i = 0;

        char ch = text.charAt(i);
        while (Character.isWhitespace(i)) {
            token.append(ch);
            i++;
        }

        while (i < text.length()) {
            ch = text.charAt(i);
            token.append(ch);

            if (Character.isWhitespace(ch)) {
                segments.add(buildSyntaxSegment(token.toString()));
                token = new StringBuilder();
            }

            i++;
        }

        if (token.length() > 0)
            segments.add(buildSyntaxSegment(token.toString()));

        return segments;
    }

    private static SyntaxSegment buildSyntaxSegment(String text) {
        int color = EditorColors.TEXT_FOREGROUND;

        if (keywords.contains(text.trim().toLowerCase())) {
            color = EditorColors.KEYWORD_FOREGROUND;
        } else if (text.trim().endsWith(":")) {
            color = EditorColors.LABEL_FOREGROUND;
        } else if (text.trim().startsWith("0x")) {
            try {
                Integer.parseInt(text.trim().substring(2), 16);
                color = EditorColors.NUMERAL_FOREGROUND;
            } catch (Exception ignored) {
            }
        }
        else {
            try {
                Integer.parseInt(text.trim());
                color = EditorColors.NUMERAL_FOREGROUND;
            } catch (Exception ignored) {
            }
        }

        return new SyntaxSegment(
            MinecraftClient.getInstance().textRenderer.getWidth(text),
            color,
            text
        );
    }
}
