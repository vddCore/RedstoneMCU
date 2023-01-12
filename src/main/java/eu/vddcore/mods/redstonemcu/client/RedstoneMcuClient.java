package eu.vddcore.mods.redstonemcu.client;

import eu.vddcore.mods.redstonemcu.gui.McuIdeController;
import eu.vddcore.mods.redstonemcu.gui.McuIdeScreen;
import eu.vddcore.mods.redstonemcu.registry.ScreenTypeRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class RedstoneMcuClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.<McuIdeController, McuIdeScreen>register(
            ScreenTypeRegistry.IDE_SCREEN_HANDLER_TYPE,
            (gui, inventory, title) -> new McuIdeScreen(gui, inventory.player, title)
        );
    }
}
