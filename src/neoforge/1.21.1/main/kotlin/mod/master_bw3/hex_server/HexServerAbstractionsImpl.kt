@file:JvmName("HexServerAbstractionsImpl")

package mod.master_bw3.hex_server

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.msrandom.multiplatform.annotations.Actual

actual fun sendPacketToServer(packet: CustomPacketPayload): Unit {
    ClientPlayNetworking.send(packet)
}

actual fun sendPacketToPlayer(
    target: ServerPlayer,
    packet: CustomPacketPayload
): Unit {
    ServerPlayNetworking.send(target, packet)
}
