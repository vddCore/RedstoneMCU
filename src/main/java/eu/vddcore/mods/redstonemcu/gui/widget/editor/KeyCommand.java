package eu.vddcore.mods.redstonemcu.gui.widget.editor;

import eu.vddcore.mods.redstonemcu.gui.widget.WCodeEditor;
import org.lwjgl.glfw.GLFW;

public class KeyCommand {
    public interface Handler {
        void handle(WCodeEditor editor, CodeBuffer buffer);
    }

    private boolean ctrl;
    private boolean alt;
    private boolean shift;
    private int key;

    private final Handler handler;

    public KeyCommand(Handler handler) {
        this.handler = handler;
    }

    public void setModifiers(boolean ctrl, boolean alt, boolean shift) {
        this.ctrl = ctrl;
        this.alt = alt;
        this.shift = shift;
    }

    public void execute(WCodeEditor editor, CodeBuffer buffer) {
        if (handler != null)
            handler.handle(editor, buffer);
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public int getModifiers() {
        int modifiers = 0;

        if (ctrl) modifiers |= GLFW.GLFW_MOD_CONTROL;
        if (alt) modifiers |= GLFW.GLFW_MOD_ALT;
        if (shift) modifiers |= GLFW.GLFW_MOD_SHIFT;

        return modifiers;
    }
}
