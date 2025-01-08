package net.mathimomos.wormhole_artifact.server.item;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.item.custom.WormholeArtifactItem;
import net.mathimomos.wormhole_artifact.server.item.custom.WormholeRemoteItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WormholeArtifact.MOD_ID);

    public static final RegistryObject<Item> WORMHOLE_ARTIFACT = ITEMS.register("wormhole_artifact",
            () -> new WormholeArtifactItem(new Item.Properties().durability(16)));

    public static final RegistryObject<Item> WORMHOLE_REMOTE = ITEMS.register("wormhole_remote",
            () -> new WormholeRemoteItem(new Item.Properties().durability(64)));

    public static final RegistryObject<Item> ENDER_NACRE = ITEMS.register("ender_nacre",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
