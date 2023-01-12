package eu.vddcore.mods.redstonemcu;

import eu.vddcore.mods.redstonemcu.blockentity.BlockEntityMcu;
import eu.vddcore.mods.redstonemcu.block.BlockMcu;
import eu.vddcore.mods.redstonemcu.items.ItemDebugger;
import eu.vddcore.mods.redstonemcu.items.ItemMcuStandard;
import eu.vddcore.mods.redstonemcu.items.ItemRomChip;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RedstoneMcu implements ModInitializer {
    public static final String MOD_ID = "redstonemcu";

    public static BlockMcu BLOCK_MCU = new BlockMcu(FabricBlockSettings.of(Material.METAL).hardness(5.0f));
    public static BlockEntityType<BlockEntityMcu> BLOCK_MCU_ENTITY;

    public static ItemMcuStandard ITEM_MCU_STANDARD = new ItemMcuStandard(BLOCK_MCU, new Item.Settings().group(ItemGroup.REDSTONE));
    public static ItemRomChip ITEM_ROM_CHIP = new ItemRomChip(new Item.Settings().group(ItemGroup.REDSTONE));

    public static ItemDebugger ITEM_MCU_DEBUGGER = new ItemDebugger(new Item.Settings().group(ItemGroup.MISC));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "mcu_standard"), ITEM_MCU_STANDARD);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "rom_chip"), ITEM_ROM_CHIP);

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "mcu_debugger"), ITEM_MCU_DEBUGGER);

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "mcu_block_standard"), BLOCK_MCU);
        BLOCK_MCU_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            MOD_ID + ":mcu_block_entity",
            BlockEntityType.Builder.create(
                BlockEntityMcu::new,
                BLOCK_MCU
            ).build(null)
        );
    }
}
