package mod.master_bw3.hex_server.client.HexHttp

import at.petrak.hexcasting.api.casting.eval.ExecutionClientView
import mod.master_bw3.hex_server.network.HexServerNetworking
import mod.master_bw3.hex_server.network.MsgEvaluateHexC2S
import net.minecraft.nbt.NbtCompound
import java.util.*
import java.util.concurrent.*

class MCServerRequestHandler {
    private var pending: ConcurrentHashMap<UUID, HexFuture> = ConcurrentHashMap()

    fun evaluateHex(hex: NbtCompound): Future<ExecutionClientView> {
        val uuid = UUID.randomUUID()
        val future = HexFuture(uuid, this)
        HexServerNetworking.sendToServer(MsgEvaluateHexC2S(hex, uuid))
        pending.put(uuid, future)

        return future
    }

    fun setEvaluatedHexResult(id: UUID, result: ExecutionClientView) {
        val future: HexFuture? = pending.remove(id)

        future?.complete(result)
    }

    //not cursed at all
    private class HexFuture(private val id: UUID, private val requestHandler: MCServerRequestHandler) :
        CompletableFuture<ExecutionClientView>() {

        override fun get(timeout: Long, unit: TimeUnit): ExecutionClientView {
            return try {
                super.get(timeout, unit)
            } catch (e: TimeoutException) {
                requestHandler.pending.remove(id)
                throw e
            }
        }
    }
}