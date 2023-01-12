package eu.vddcore.mods.redstonemcu.items;

import net.minecraft.block.Block;

public class ItemMcuStandard extends ItemMcu {

    public ItemMcuStandard(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public String getTranslationKey() {
        return "item.redstonemcu.mcu_standard";
    }
}
