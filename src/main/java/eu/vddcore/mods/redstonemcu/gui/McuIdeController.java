package eu.vddcore.mods.redstonemcu.gui;

import eu.vddcore.mods.redstonemcu.Identifiers;
import eu.vddcore.mods.redstonemcu.registry.ScreenTypeRegistry;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

public class McuIdeController extends SyncedGuiDescription {
    private final BlockPos blockPos;

    private final WLabel northPortStatusLabel;
    private final WLabel eastPortStatusLabel;
    private final WLabel southPortStatusLabel;
    private final WLabel westPortStatusLabel;

    public McuIdeController(int syncId, PlayerInventory playerInventory, BlockPos blockPos, ScreenHandlerContext context) {
        super(
            ScreenTypeRegistry.IDE_SCREEN_HANDLER_TYPE,
            syncId,
            playerInventory,
            getBlockInventory(context, 1),
            getBlockPropertyDelegate(context)
        );

        this.blockPos = blockPos;

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(172, 96);

        northPortStatusLabel = new WLabel("NORTH_PORT: UNKNOWN");
        westPortStatusLabel = new WLabel("WEST_PORT: UNKNOWN");
        southPortStatusLabel = new WLabel("SOUTH_PORT: UNKNOWN");
        eastPortStatusLabel = new WLabel("EAST_PORT: UNKNOWN");

        root.add(northPortStatusLabel, 0, 1);
        root.add(westPortStatusLabel, 0, 2);
        root.add(southPortStatusLabel, 0, 3);
        root.add(eastPortStatusLabel, 0, 4);

        root.validate(this);
    }

    public void updateMcuStatusText(String text) {
        String[] segments = text.split("\n");

        northPortStatusLabel.setText(new LiteralText(segments[0]));
        westPortStatusLabel.setText(new LiteralText(segments[1]));
        southPortStatusLabel.setText(new LiteralText(segments[2]));
        eastPortStatusLabel.setText(new LiteralText(segments[3]));
    }

    @Override
    public void close(PlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(blockPos);
        ClientSidePacketRegistry.INSTANCE.sendToServer(Identifiers.CSP_MCU_IDE_CLOSED, buf);

        super.close(player);
    }
}
