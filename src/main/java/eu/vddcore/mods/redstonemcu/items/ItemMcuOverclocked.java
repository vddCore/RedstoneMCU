package eu.vddcore.mods.redstonemcu.items;

import net.minecraft.block.Block;

public class ItemMcuOverclocked extends ItemMcu {

    public ItemMcuOverclocked(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public String getTranslationKey() {
        return "item.redstonemcu.mcu_overclocked";
    }
}
