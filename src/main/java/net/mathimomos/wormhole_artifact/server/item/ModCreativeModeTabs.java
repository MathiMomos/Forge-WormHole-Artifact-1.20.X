package net.mathimomos.wormhole_artifact.server.item;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WormholeArtifact.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ERYTHRIUM_TAB =CREATIVE_MODE_TABS.register("erythrium_tab",
    () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WORMHOLE_ARTIFACT.get()))
            .title(Component.translatable("wormhole_artifact.wormhole_artifact_tab"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.WORMHOLE_ARTIFACT.get());
                pOutput.accept(ModItems.WORMHOLE_REMOTE.get());
            })
            .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
