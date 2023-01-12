package eu.vddcore.mods.redstonemcu.registry;

import eu.vddcore.mods.redstonemcu.Identifiers;
import eu.vddcore.mods.redstonemcu.entity.BlockEntityMcu;
import eu.vddcore.mods.redstonemcu.gui.McuIdeScreen;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;

public class PacketRegistry {
    public static void registerServerToClient() {
        ClientSidePacketRegistry.INSTANCE.register(
            Identifiers.SCP_MCU_STATUS_DATA,
            (packetContext, packetByteBuf) -> {
                int textLength = packetByteBuf.readInt();
                String text = packetByteBuf.readString(textLength);

                packetContext.getTaskQueue().execute(() -> {
                    Screen screen = MinecraftClient.getInstance().currentScreen;

                    if (screen instanceof McuIdeScreen) {
                        McuIdeScreen mcuIdeScreen = (McuIdeScreen) screen;
                        mcuIdeScreen.getScreenHandler().updateMcuStatusText(text);
                    }
                });
            }
        );
    }

    public static void registerClientToServer() {
        ServerSidePacketRegistry.INSTANCE.register(
            Identifiers.CSP_MCU_IDE_OPENED,
            (packetContext, packetByteBuf) -> {
                BlockPos blockPos = packetByteBuf.readBlockPos();

                packetContext.getTaskQueue().execute(() -> {
                    BlockEntity be = packetContext.getPlayer().getEntityWorld().getBlockEntity(blockPos);

                    if (be instanceof BlockEntityMcu) {
                        BlockEntityMcu mcuEntity = (BlockEntityMcu) be;
                        mcuEntity.setCurrentlyInteractingPlayer(packetContext.getPlayer());
                    }
                });
            }
        );

        ServerSidePacketRegistry.INSTANCE.register(
            Identifiers.CSP_MCU_IDE_CLOSED,
            (packetContext, packetByteBuf) -> {
                BlockPos blockPos = packetByteBuf.readBlockPos();

                packetContext.getTaskQueue().execute(() -> {
                    BlockEntity be = packetContext.getPlayer().getEntityWorld().getBlockEntity(blockPos);

                    if (be instanceof BlockEntityMcu) {
                        BlockEntityMcu mcuEntity = (BlockEntityMcu) be;
                        mcuEntity.setCurrentlyInteractingPlayer(null);
                    }
                });
            }
        );
    }
}
