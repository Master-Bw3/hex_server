package mod.master_bw3.hex_server.client.HexHttp

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import mod.master_bw3.hex_server.network.HexServerNetworking
import mod.master_bw3.hex_server.network.MsgEvaluateHexC2S
import net.minecraft.nbt.NbtCompound
import java.util.*
import java.util.concurrent.*

typealias HexFuture = CompletableFuture<ExecutionClientView>

class EvalHexRequestHandler {
    private var pending: ConcurrentHashMap<UUID, HexFuture> = ConcurrentHashMap()

    fun evaluateHex(hex: NbtCompound): Future<ExecutionClientView> {
        val uuid = UUID.randomUUID()
        val future = HexFuture()
        HexServerNetworking.sendToServer(MsgEvaluateHexC2S(hex, uuid))
        pending[uuid] = future

        return future
    }

    fun setEvaluatedHexResult(id: UUID, result: ExecutionClientView) {
        val future: HexFuture? = pending.remove(id)

        future?.complete(result)
    }
}