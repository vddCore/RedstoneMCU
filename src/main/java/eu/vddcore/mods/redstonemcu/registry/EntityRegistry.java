package eu.vddcore.mods.redstonemcu.registry;

import eu.vddcore.mods.redstonemcu.Identifiers;
import eu.vddcore.mods.redstonemcu.entity.BlockEntityMcu;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class EntityRegistry {
    public static final BlockEntityType<BlockEntityMcu> BLOCK_MCU_ENTITY;

    public static void registerAll() {
        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            Identifiers.MCU_STANDARD_BLOCKENTITY_ID,
            BLOCK_MCU_ENTITY
        );
    }

    static {
        BLOCK_MCU_ENTITY = BlockEntityType.Builder.create(
            BlockEntityMcu::new,
            BlockRegistry.BLOCK_MCU
        ).build(null);
    }
}
