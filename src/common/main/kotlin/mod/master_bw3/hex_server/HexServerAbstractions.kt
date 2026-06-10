@file:JvmName("HexServerAbstractions")

package mod.master_bw3.hex_server

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

expect fun sendPacketToServer(packet: CustomPacketPayload): Unit

expect fun sendPacketToPlayer(
    target: ServerPlayer,
    packet: CustomPacketPayload
): Unit
