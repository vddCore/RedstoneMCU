package eu.vddcore.mods.redstonemcu.gui.widget;

import io.github.cottonmc.cotton.gui.widget.WScrollBar;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

public class WNonFocusableScrollBar extends WScrollBar {
    public WNonFocusableScrollBar(Axis axis) {
        super(axis);
    }

    @Override
    public boolean canFocus() {
        return false;
    }
}
