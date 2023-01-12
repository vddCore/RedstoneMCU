package eu.vddcore.mods.redstonemcu.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class BlockMcu extends Block implements BlockEntityProvider, Tickable {

    public BlockMcu(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new BlockEntityMcu();
    }

    @Override
    public void tick() {

    }
}
