package eu.vddcore.mods.redstonemcu.gui.widget.editor;

import java.util.ArrayList;
import java.util.Optional;

public class KeyCommandRegistry {
    private static final ArrayList<KeyCommand> commands = new ArrayList<>();

    public static void bind(boolean ctrl, boolean alt, boolean shift, int key, KeyCommand command) {
        command.setKey(key);
        command.setModifiers(ctrl, alt, shift);

        commands.add(command);
    }

    public static KeyCommand get(int modifiers, int key) {
        Optional<KeyCommand> requestedCommand = commands.stream().filter(
            x -> x.getModifiers() == modifiers && x.getKey() == key
        ).findFirst();

        return requestedCommand.orElse(null);
    }
}
