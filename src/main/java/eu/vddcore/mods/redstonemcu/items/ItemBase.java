package eu.vddcore.mods.redstonemcu.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemBase extends Item {
    public ItemBase(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlockServerOnly(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    public TypedActionResult<ItemStack> useServerOnly(World world, PlayerEntity user, Hand hand) {
        return new TypedActionResult<>(ActionResult.PASS, user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return super.useOnBlock(context);
        } else {
            return useOnBlockServerOnly(context);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            return super.use(world, user, hand);
        } else {
            return useServerOnly(world, user, hand);
        }
    }
}
