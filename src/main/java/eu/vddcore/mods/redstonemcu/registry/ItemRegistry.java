package eu.vddcore.mods.redstonemcu.registry;

import eu.vddcore.mods.redstonemcu.Identifiers;
import eu.vddcore.mods.redstonemcu.items.ItemDebugger;
import eu.vddcore.mods.redstonemcu.items.ItemMcuStandard;
import eu.vddcore.mods.redstonemcu.items.ItemRomChip;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static final ItemMcuStandard ITEM_MCU_STANDARD;
    public static ItemRomChip ITEM_ROM_CHIP;
    public static ItemDebugger ITEM_MCU_DEBUGGER;

    public static void registerAll() {
        Registry.register(Registry.ITEM, Identifiers.MCU_STANDARD_ID, ITEM_MCU_STANDARD);
        Registry.register(Registry.ITEM, Identifiers.ROM_CHIP_ID, ITEM_ROM_CHIP);
        Registry.register(Registry.ITEM, Identifiers.MCU_DEBUGGER_ID, ITEM_MCU_DEBUGGER);
    }

    static {
        ITEM_MCU_STANDARD = new ItemMcuStandard(BlockRegistry.BLOCK_MCU, new Item.Settings().group(ItemGroup.REDSTONE));
        ITEM_ROM_CHIP = new ItemRomChip(new Item.Settings().group(ItemGroup.REDSTONE));
        ITEM_MCU_DEBUGGER = new ItemDebugger(new Item.Settings().group(ItemGroup.MISC));
    }
}
