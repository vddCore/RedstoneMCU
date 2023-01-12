package eu.vddcore.mods.redstonemcu.block;

import eu.vddcore.mods.redstonemcu.blockentity.BlockEntityMcu;
import eu.vddcore.mods.redstonemcu.hardware.PortMode;
import eu.vddcore.mods.redstonemcu.hardware.RedstonePort;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class BlockMcu extends Block implements BlockEntityProvider {

    public static final BooleanProperty EMITTING_NORTH = BooleanProperty.of("emitting_north");
    public static final BooleanProperty EMITTING_EAST = BooleanProperty.of("emitting_east");
    public static final BooleanProperty EMITTING_SOUTH = BooleanProperty.of("emitting_south");
    public static final BooleanProperty EMITTING_WEST = BooleanProperty.of("emitting_west");

    public BlockMcu(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(EMITTING_NORTH)
            .add(EMITTING_EAST)
            .add(EMITTING_SOUTH)
            .add(EMITTING_WEST);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new BlockEntityMcu();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean emitsRedstonePower(BlockState state) {
        return state.get(EMITTING_NORTH)
            || state.get(EMITTING_EAST)
            || state.get(EMITTING_SOUTH)
            || state.get(EMITTING_WEST);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getWeakRedstonePower(state, world, pos, direction);
    }

    @Override
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.30, 1);
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return BlockSoundGroup.METAL;
    }
}
