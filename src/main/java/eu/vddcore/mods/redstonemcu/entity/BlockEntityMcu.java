package eu.vddcore.mods.redstonemcu.entity;

import eu.vddcore.mods.redstonemcu.gui.McuIdeController;
import eu.vddcore.mods.redstonemcu.hardware.PortMode;
import eu.vddcore.mods.redstonemcu.hardware.RedstonePort;
import eu.vddcore.mods.redstonemcu.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockEntityMcu extends BlockEntity implements Tickable, NamedScreenHandlerFactory {

    private final List<RedstonePort> ports;
    public LiteralText statusText;

    public BlockEntityMcu() {
        super(EntityRegistry.BLOCK_MCU_ENTITY);

        ports = Stream.of(
            new RedstonePort(this, Direction.NORTH),
            new RedstonePort(this, Direction.WEST),
            new RedstonePort(this, Direction.SOUTH),
            new RedstonePort(this, Direction.EAST)
        ).collect(Collectors.toList());

        statusText = new LiteralText("status unavailable");
    }

    @Override
    public void tick() {
        World world = getWorld();

        if (world == null || world.isClient())
            return;

        BlockPos pos = getPos();

        StringBuilder text = new StringBuilder();
        for (RedstonePort p : ports) {
            if (p != null) {
                PortMode mode = p.getMode();

                if (mode.equals(PortMode.DISABLED)) {
                    // DISABLED retains its last power state
                    continue;
                }

                Direction portDirection = p.getDirection();

                if (mode.equals(PortMode.INPUT)) {
                    if (world.isEmittingRedstonePower(pos.offset(portDirection), portDirection)) {
                        int redstoneLevel = world.getEmittedRedstonePower(
                            pos.offset(p.getDirection()),
                            portDirection.getOpposite()
                        );

                        p.setRedstonePowerLevel(redstoneLevel);
                    } else {
                        p.setRedstonePowerLevel(0);
                    }
                }

                text.append(portDirection.toString())
                    .append(" [")
                    .append(mode.toString())
                    .append("]: ")
                    .append(p.getRedstonePowerLevel())
                    .append("\n");
            }
        }

        statusText = new LiteralText(text.toString());
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        for (RedstonePort p : ports)
            p.readNbt(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        for (RedstonePort p : ports)
            p.writeNbt(tag);

        return tag;
    }

    public void onPortStateChanged() {
        World world = getWorld();
        if (world == null) return;

        world.updateNeighbors(getPos(), world.getBlockState(getPos()).getBlock());
        markDirty();
    }

    public RedstonePort getRedstonePort(Direction direction) {
        for (RedstonePort p : ports) {
            if (p != null && p.getDirection().equals(direction))
                return p;
        }

        return null;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new McuIdeController(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
}
