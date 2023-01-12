package eu.vddcore.mods.redstonemcu;

import eu.vddcore.mods.redstonemcu.registry.BlockRegistry;
import eu.vddcore.mods.redstonemcu.registry.EntityRegistry;
import eu.vddcore.mods.redstonemcu.registry.ItemRegistry;
import eu.vddcore.mods.redstonemcu.registry.ScreenTypeRegistry;
import net.fabricmc.api.ModInitializer;

public class RedstoneMcu implements ModInitializer {
    public static final String MOD_ID = "redstonemcu";

    @Override
    public void onInitialize() {
        BlockRegistry.registerAll();
        ItemRegistry.registerAll();
        EntityRegistry.registerAll();
        ScreenTypeRegistry.registerAll();
    }
}
