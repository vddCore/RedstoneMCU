package eu.vddcore.mods.redstonemcu;

import net.minecraft.util.Identifier;

import static eu.vddcore.mods.redstonemcu.RedstoneMcu.MOD_ID;

public class Identifiers {

    public static final Identifier MCU_STANDARD_ID = new Identifier(MOD_ID, "mcu_standard");
    public static final Identifier MCU_STANDARD_BLOCK_ID = new Identifier(MOD_ID, "mcu_block_standard");
    public static final Identifier MCU_STANDARD_BLOCKENTITY_ID = new Identifier(MOD_ID, "mcu_blockentity_standard");

    public static final Identifier ROM_CHIP_ID = new Identifier(MOD_ID, "rom_chip");
    public static final Identifier MCU_DEBUGGER_ID = new Identifier(MOD_ID, "mcu_debugger");

    // SCP: SERVER->CLIENT Packet
    public static final Identifier SCP_MCU_SOURCE_CODE = new Identifier(MOD_ID, "packet_mcu_source_code");
    public static final Identifier SCP_MCU_STATUS_DATA = new Identifier(MOD_ID, "packet_mcu_status_data");

    // CSP: CLIENT->SERVER Packet
    public static final Identifier CSP_MCU_IDE_OPENED = new Identifier(MOD_ID, "packet_mcu_ide_opened");
    public static final Identifier CSP_MCU_IDE_CLOSED = new Identifier(MOD_ID, "packet_mcu_ide_closed");
}
