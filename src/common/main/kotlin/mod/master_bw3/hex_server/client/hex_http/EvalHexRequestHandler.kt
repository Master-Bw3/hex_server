package mod.master_bw3.hex_server.client.hex_http

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import at.petrak.hexcasting.api.casting.iota.Iota
import mod.master_bw3.hex_server.network.MsgEvaluateHexC2S
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future
import mod.master_bw3.hex_server.sendPacketToServer


typealias HexFuture = CompletableFuture<ExecutionClientView>

class EvalHexRequestHandler {
    private var pending: ConcurrentHashMap<UUID, HexFuture> = ConcurrentHashMap()

    fun evaluateHex(hex: List<Iota>): Future<ExecutionClientView> {
        val uuid = UUID.randomUUID()
        val future = HexFuture()
        sendPacketToServer(MsgEvaluateHexC2S(hex, uuid))
        pending[uuid] = future

        return future
    }

    fun setEvaluatedHexResult(id: UUID, result: ExecutionClientView) {
        val future: HexFuture? = pending.remove(id)

        future?.complete(result)
    }
}