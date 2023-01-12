package eu.vddcore.mods.redstonemcu.registry;

import eu.vddcore.mods.redstonemcu.Identifiers;
import eu.vddcore.mods.redstonemcu.block.BlockMcu;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {
    public static final BlockMcu BLOCK_MCU;

    public static void registerAll() {
        Registry.register(Registry.BLOCK, Identifiers.MCU_STANDARD_BLOCK_ID, BLOCK_MCU);
    }

    static {
        BLOCK_MCU = new BlockMcu(FabricBlockSettings.of(Material.METAL).hardness(5.0f));
    }
}
