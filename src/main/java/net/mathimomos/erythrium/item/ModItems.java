package net.mathimomos.erythrium.item;

import net.mathimomos.erythrium.Erythrium;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Erythrium.MOD_ID);

    public static final RegistryObject<Item> ERYTHRIUM = ITEMS.register("erythrium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ROUGH_ERYTHRIUM = ITEMS.register("rough_erythrium",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
