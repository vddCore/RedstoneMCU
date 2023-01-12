package eu.vddcore.mods.redstonemcu.registry;

import eu.vddcore.mods.redstonemcu.Identifiers;
import eu.vddcore.mods.redstonemcu.gui.McuIdeController;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ScreenTypeRegistry {
    public static ScreenHandlerType<McuIdeController> IDE_SCREEN_HANDLER_TYPE;

    public static void registerAll() {
        IDE_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(
            Identifiers.MCU_STANDARD_BLOCK_ID,
            (syncId, inv, buf) -> new McuIdeController(syncId, inv, ScreenHandlerContext.EMPTY, buf)
        );
    }
}
