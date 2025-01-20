package net.mathimomos.wormhole_artifact;

import com.mojang.logging.LogUtils;
import net.mathimomos.wormhole_artifact.client.particle.ModParticles;
import net.mathimomos.wormhole_artifact.client.sound.ModSounds;
import net.mathimomos.wormhole_artifact.config.ModConfigs;
import net.mathimomos.wormhole_artifact.server.item.ModCreativeModeTabs;
import net.mathimomos.wormhole_artifact.server.item.ModItems;
import net.mathimomos.wormhole_artifact.server.message.OpenWormholeArtifactScreenMessage;
import net.mathimomos.wormhole_artifact.server.message.PlayerListRequestMessage;
import net.mathimomos.wormhole_artifact.server.message.PlayerListResponseMessage;
import net.mathimomos.wormhole_artifact.server.message.TeleportToTargetMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

import java.util.Optional;


@Mod(WormholeArtifact.MOD_ID)
public class WormholeArtifact {
    public static final String MOD_ID = "wormhole_artifact";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel NETWORK_WRAPPER = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public WormholeArtifact() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);

        ModParticles.register(modEventBus);

        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModConfigs.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        int packetsRegistered = 0;
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, TeleportToTargetMessage.class,
                TeleportToTargetMessage::write,
                TeleportToTargetMessage::read,
                TeleportToTargetMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK_WRAPPER.registerMessage(packetsRegistered++, PlayerListRequestMessage.class,
                PlayerListRequestMessage::write,
                PlayerListRequestMessage::read,
                PlayerListRequestMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        NETWORK_WRAPPER.registerMessage(packetsRegistered++, PlayerListResponseMessage.class,
                PlayerListResponseMessage::write,
                PlayerListResponseMessage::read,
                PlayerListResponseMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        NETWORK_WRAPPER.registerMessage(packetsRegistered++, OpenWormholeArtifactScreenMessage.class,
                OpenWormholeArtifactScreenMessage::write, OpenWormholeArtifactScreenMessage::read,
                OpenWormholeArtifactScreenMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
