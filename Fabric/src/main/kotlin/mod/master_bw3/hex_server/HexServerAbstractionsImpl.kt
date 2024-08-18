@file:JvmName("HexServerAbstractionsImpl")

package mod.master_bw3.hex_server.fabric

import mod.master_bw3.hex_server.registry.HexDebugRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: HexDebugRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
