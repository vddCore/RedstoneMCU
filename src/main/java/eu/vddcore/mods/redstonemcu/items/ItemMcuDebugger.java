package eu.vddcore.mods.redstonemcu.items;

import eu.vddcore.mods.redstonemcu.blockentity.BlockEntityMcu;
import eu.vddcore.mods.redstonemcu.debug.DebuggerOperationMode;
import eu.vddcore.mods.redstonemcu.hardware.PortMode;
import eu.vddcore.mods.redstonemcu.hardware.RedstonePort;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemMcuDebugger extends Item {

    private DebuggerOperationMode operationMode = DebuggerOperationMode.DISPLAY_INTERNAL_STATE;

    public ItemMcuDebugger(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        if (context.getPlayer().isSneaking()) {
            return ActionResult.PASS;
        } else {
            BlockEntity entity = context.getWorld().getBlockEntity(context.getBlockPos());

            if (entity instanceof BlockEntityMcu) {
                BlockEntityMcu mcuEntity = (BlockEntityMcu) entity;
                if (operationMode.equals(DebuggerOperationMode.SET_PORT_MODE)) {
                    RedstonePort port = mcuEntity.getRedstonePort(context.getSide());

                    if (port != null) {
                        port.setMode(PortMode.values()[(port.getMode().ordinal() + 1) % PortMode.values().length]);
                        context.getPlayer().sendMessage(new LiteralText("set " + context.getSide().toString().toUpperCase() + " to " + port.getMode().toString()), false);
                    }

                    return ActionResult.SUCCESS;
                } else if (operationMode.equals(DebuggerOperationMode.DISPLAY_INTERNAL_STATE)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("north redstone power: ")
                        .append(mcuEntity.getRedstonePort(Direction.NORTH).getRedstonePowerLevel())
                        .append("\n");

                    sb.append("west redstone power: ")
                        .append(mcuEntity.getRedstonePort(Direction.WEST)
                            .getRedstonePowerLevel()).append("\n");

                    sb.append("south redstone power: ")
                        .append(mcuEntity.getRedstonePort(Direction.SOUTH).getRedstonePowerLevel())
                        .append("\n");

                    sb.append("east redstone power: ")
                        .append(mcuEntity.getRedstonePort(Direction.EAST).getRedstonePowerLevel())
                        .append("\n--------\n");

                    context.getPlayer().sendMessage(new LiteralText(sb.toString()), false);

                    return ActionResult.SUCCESS;
                } else if (operationMode.equals(DebuggerOperationMode.ROTATE_REDSTONE_LEVEL)) {
                    RedstonePort port = mcuEntity.getRedstonePort(context.getSide());
                    port.setRedstonePowerLevel((port.getRedstonePowerLevel() + 1) % 16);

                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient() || !user.isSneaking())
            return new TypedActionResult<>(ActionResult.PASS, user.getStackInHand(hand));

        operationMode = DebuggerOperationMode.values()[(operationMode.ordinal() + 1) % DebuggerOperationMode.values().length];
        user.sendMessage(new LiteralText("set operation mode to " + operationMode.toString()), true);

        return new TypedActionResult<>(ActionResult.PASS, user.getStackInHand(hand));
    }
}
