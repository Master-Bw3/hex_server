package mod.master_bw3.hex_server.client.HexHttp

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import mod.master_bw3.hex_server.network.HexServerNetworking
import mod.master_bw3.hex_server.network.MsgEvaluateHexC2S
import net.minecraft.nbt.NbtCompound
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Future

typealias HexFuture = CompletableFuture<ExecutionClientView>

class MCServerRequestHandler {
    private var pending: ConcurrentHashMap<UUID, HexFuture> = ConcurrentHashMap()

    fun evaluateHex(hex: NbtCompound): Future<ExecutionClientView> {
        val future = HexFuture()
        val uuid = UUID.randomUUID()
        HexServerNetworking.sendToServer(MsgEvaluateHexC2S(hex, uuid))
        pending.put(uuid, future)

        return future
    }

    fun setEvaluatedHexResult(id: UUID, result: ExecutionClientView) {
        val future: HexFuture? = pending.remove(id)

        future?.complete(result)
    }
}