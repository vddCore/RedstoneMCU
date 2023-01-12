package eu.vddcore.mods.redstonemcu;

import eu.vddcore.mods.redstonemcu.blocks.BlockEntityMcu;
import eu.vddcore.mods.redstonemcu.blocks.BlockMcu;
import eu.vddcore.mods.redstonemcu.items.ItemMcuDebugger;
import eu.vddcore.mods.redstonemcu.items.ItemMcuOverclocked;
import eu.vddcore.mods.redstonemcu.items.ItemMcuStandard;
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
    public static ItemMcuOverclocked ITEM_MCU_OVERCLOCKED = new ItemMcuOverclocked(BLOCK_MCU, new Item.Settings().group(ItemGroup.REDSTONE));
    public static ItemMcuDebugger ITEM_MCU_DEBUGGER = new ItemMcuDebugger(new Item.Settings().group(ItemGroup.MISC));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "mcu_standard"), ITEM_MCU_STANDARD);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "mcu_overclocked"), ITEM_MCU_OVERCLOCKED);
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
