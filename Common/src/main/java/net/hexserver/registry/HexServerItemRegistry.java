package net.hexserver.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.hexserver.HexServer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import static net.hexserver.HexServer.id;

public class HexServerItemRegistry {
    // Register items through this
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(HexServer.MOD_ID, Registry.ITEM_KEY);

    public static void init() {
        ITEMS.register();
    }

    // A new creative tab. Notice how it is one of the few things that are not deferred
    public static final ItemGroup DUMMY_GROUP = CreativeTabRegistry.create(id("dummy_group"), () -> new ItemStack(HexServerItemRegistry.DUMMY_ITEM.get()));

    // During the loading phase, refrain from accessing suppliers' items (e.g. EXAMPLE_ITEM.get()), they will not be available
    public static final RegistrySupplier<Item> DUMMY_ITEM = ITEMS.register("dummy_item", () -> new Item(new Item.Settings().group(DUMMY_GROUP)));


}
