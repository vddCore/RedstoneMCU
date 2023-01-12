package eu.vddcore.mods.redstonemcu.block;

import eu.vddcore.mods.redstonemcu.entity.BlockEntityMcu;
import eu.vddcore.mods.redstonemcu.hardware.PortMode;
import eu.vddcore.mods.redstonemcu.hardware.RedstonePort;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class BlockMcu extends Block implements BlockEntityProvider {

    public BlockMcu(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntityMcu mcuEntity = (BlockEntityMcu) world.getBlockEntity(pos);

        if (mcuEntity == null)
            return ActionResult.FAIL;

        if (!world.isClient() && player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            if (mcuEntity.getCurrentlyInteractingPlayer() == null) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(new LiteralText("This MCU is currently being used by someone else."), true);
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BlockEntityMcu();
    }

    @Override
    public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getWeakRedstonePower(state, world, pos, direction);
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        BlockEntity entity = world.getBlockEntity(pos);

        if (!(entity instanceof BlockEntityMcu))
            return 0;

        BlockEntityMcu mcuEntity = (BlockEntityMcu) entity;
        RedstonePort p = mcuEntity.getRedstonePort(direction.getOpposite());

        if (p == null)
            return 0;

        if (p.getMode().equals(PortMode.OUTPUT)) {
            return p.getRedstonePowerLevel();
        }

        return 0;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.30, 1);
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return BlockSoundGroup.METAL;
    }
}
