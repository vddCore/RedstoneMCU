package eu.vddcore.mods.redstonemcu.gui;

import eu.vddcore.mods.redstonemcu.Identifiers;
import eu.vddcore.mods.redstonemcu.gui.widget.WCodeEditor;
import eu.vddcore.mods.redstonemcu.registry.ScreenTypeRegistry;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

public class McuIdeController extends SyncedGuiDescription {
    private WLabel northPortStatusLabel;
    private WLabel eastPortStatusLabel;
    private WLabel southPortStatusLabel;
    private WLabel westPortStatusLabel;

    private final BlockPos blockPos;

    public McuIdeController(int syncId, PlayerInventory inv, ScreenHandlerContext context) {
        super(
            ScreenTypeRegistry.IDE_SCREEN_HANDLER_TYPE,
            syncId,
            inv,
            getBlockInventory(context, 1),
            getBlockPropertyDelegate(context)
        );

        this.blockPos = BlockPos.ORIGIN;
        createVisualTree();
    }

    public McuIdeController(int syncId, PlayerInventory inv, ScreenHandlerContext context, PacketByteBuf buf) {
        super(
            ScreenTypeRegistry.IDE_SCREEN_HANDLER_TYPE,
            syncId,
            inv,
            getBlockInventory(context, 1),
            getBlockPropertyDelegate(context)
        );

        this.blockPos = buf.readBlockPos();
        createVisualTree();

        if (inv.player.getEntityWorld().isClient()) {
            PacketByteBuf buf2 = new PacketByteBuf(Unpooled.buffer());
            buf2.writeBlockPos(blockPos);

            ClientSidePacketRegistry.INSTANCE.sendToServer(Identifiers.CSP_MCU_IDE_OPENED, buf2);
        }
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
        if (player.getEntityWorld().isClient()) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBlockPos(blockPos);
            ClientSidePacketRegistry.INSTANCE.sendToServer(Identifiers.CSP_MCU_IDE_CLOSED, buf);
        }

        super.close(player);
    }

    private void createVisualTree() {
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

        WCodeEditor codeEditor = new WCodeEditor();
        WLabel codeEditorTitle = new WLabel("ROM Chip Assembly");

        root.add(codeEditorTitle, 6, 1);
        root.add(codeEditor, 6, 2, 12, 8);
        root.validate(this);
    }
}
