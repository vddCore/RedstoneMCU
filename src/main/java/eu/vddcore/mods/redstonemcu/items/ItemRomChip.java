package eu.vddcore.mods.redstonemcu.items;

import eu.vddcore.mods.redstonemcu.entity.BlockEntityMcu;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class ItemRomChip extends ItemBase {
    public ItemRomChip(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlockServerOnly(ItemUsageContext context) {
        BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());

        if (entity instanceof BlockEntityMcu) {
            BlockEntityMcu mcu = (BlockEntityMcu)entity;
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }
}
