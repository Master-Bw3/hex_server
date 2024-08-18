package mod.master_bw3.hex_server.registry

import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import dev.architectury.platform.Platform
import mod.master_bw3.hex_server.HexServer
import net.fabricmc.api.EnvType
import net.minecraft.registry.Registry

typealias RegistrarEntry<T> = HexServerRegistrar<T>.Entry<out T>

// scuffed.

// use Any upper bound to forbid nullable types, because Registry.register needs that???
abstract class HexServerRegistrar<T>(
    val registryKey: RegistryKey<Registry<T>>,
    getRegistry: () -> Registry<T>,
) {
    /** Do not access until the mod has been initialized! */
    val registry by lazy(getRegistry)

    private var isInitialized = false

    private val mutableEntries = mutableSetOf<Entry<out T>>()
    val entries: Set<Entry<out T>> = mutableEntries

    open fun init(registerer: (Identifier, T) -> Unit) {
        if (isInitialized) throw IllegalStateException("$this has already been initialized!")
        isInitialized = true
        for (entry in entries) {
            registerer(entry.id, entry.value)
        }
        if (Platform.getEnv() == EnvType.CLIENT) {
            initClient()
        }
    }

    open fun initClient() {}

    fun <V : T> register(name: String, builder: () -> V) = register(HexServer.id(name), builder)

    fun <V : T> register(id: Identifier, builder: () -> V) = register(id, lazy {
        if (!isInitialized) throw IllegalStateException("$this has not been initialized!")
        builder()
    })

    fun <V : T> register(id: Identifier, lazyValue: Lazy<V>) = Entry(id, lazyValue).also {
        if (!mutableEntries.add(it)) {
            throw IllegalArgumentException("Duplicate id: $id")
        }
    }

    open inner class Entry<V : T>(
        val id: Identifier,
        private val lazyValue: Lazy<V>,
    ) {
        constructor(entry: Entry<V>) : this(entry.id, entry.lazyValue)

        val key: RegistryKey<T> = RegistryKey.of(registryKey, id)

        /** Do not access until the mod has been initialized! */
        val value by lazyValue

        override fun equals(other: Any?) = when (other) {
            is RegistrarEntry<*> -> key.registry == other.key.registry && id == other.id
            else -> false
        }

        override fun hashCode() = 31 * key.registry.hashCode() + id.hashCode()
    }
}