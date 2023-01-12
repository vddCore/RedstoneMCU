package eu.vddcore.mods.redstonemcu.gui;

import eu.vddcore.mods.redstonemcu.registry.ScreenTypeRegistry;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class McuIdeController extends SyncedGuiDescription {
    private final WLabel statusLabel;

    public McuIdeController(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(
            ScreenTypeRegistry.IDE_SCREEN_HANDLER_TYPE,
            syncId,
            playerInventory,
            getBlockInventory(context, 1),
            getBlockPropertyDelegate(context)
        );

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(172, 96);

        statusLabel = new WLabel("dicks");

        root.add(statusLabel, 0, 1);

        root.validate(this);
    }

    public void tick() {
//        BlockEntityMcu entity = (BlockEntityMcu) world.getBlockEntity(pos);
//
//        if (entity == null)
//            return;
//
//        RedstonePort pn = entity.getRedstonePort(Direction.NORTH);
//        statusLabel.setText(new LiteralText(pn.getDirection().toString() + " [" + pn.getMode().toString() + "]: " + pn.getRedstonePowerLevel()));
    }
}
