package eu.vddcore.mods.redstonemcu.gui;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class McuIdeScreen extends CottonInventoryScreen<McuIdeController> {

    private final McuIdeController controller;

    public McuIdeScreen(McuIdeController controller, PlayerEntity player, Text title) {
        super(controller, player, title);
        this.controller = controller;
    }
}
