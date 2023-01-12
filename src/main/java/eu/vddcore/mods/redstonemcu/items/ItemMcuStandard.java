package eu.vddcore.mods.redstonemcu.items;

import eu.vddcore.mods.redstonemcu.RedstoneMcu;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemMcuStandard extends ItemMcu {

    public ItemMcuStandard(Block block, Settings settings) {
        super(block, settings);
    }
}
