package eu.vddcore.mods.redstonemcu.hardware;

import eu.vddcore.mods.redstonemcu.entity.BlockEntityMcu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;

public class RedstonePort {

    private final String NBT_MODE_KEY;
    private final String NBT_POWER_LEVEL_KEY;

    private final Direction direction;
    private final BlockEntityMcu owner;

    private PortMode mode;
    private int redstonePowerLevel;

    public RedstonePort(BlockEntityMcu owner, Direction direction) {
        this.owner = owner;
        this.direction = direction;

        String directionBase = direction.toString().toLowerCase();
        NBT_MODE_KEY = directionBase.concat("_mode");
        NBT_POWER_LEVEL_KEY = directionBase.concat("_power_level");

        setMode(PortMode.OUTPUT);
        setRedstonePowerLevel(0);
    }

    public void setRedstonePowerLevel(int level) {
        redstonePowerLevel = level;
        owner.onPortStateChanged();
    }

    public void setMode(PortMode portMode) {
        if (!portMode.equals(PortMode.DISABLED)) {
            setRedstonePowerLevel(0);
        }

        mode = portMode;
        owner.onPortStateChanged();
    }

    public Direction getDirection() {
        return direction;
    }

    public int getRedstonePowerLevel() {
        return redstonePowerLevel;
    }

    public PortMode getMode() {
        return mode;
    }

    public void writeNbt(CompoundTag tag) {
        tag.putInt(NBT_MODE_KEY, mode.ordinal());
        tag.putInt(NBT_POWER_LEVEL_KEY, redstonePowerLevel);
    }

    public void readNbt(CompoundTag tag) {
        setMode(PortMode.values()[tag.getInt(NBT_MODE_KEY)]);
        setRedstonePowerLevel(tag.getInt(NBT_POWER_LEVEL_KEY));
    }
}
