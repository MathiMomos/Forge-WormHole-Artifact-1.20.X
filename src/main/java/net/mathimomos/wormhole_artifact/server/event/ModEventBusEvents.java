package net.mathimomos.wormhole_artifact.server.event;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.client.particle.ModParticles;
import net.mathimomos.wormhole_artifact.client.particle.custom.TeleportParticles;
import net.mathimomos.wormhole_artifact.client.particle.custom.TeleportWaveParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WormholeArtifact.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.TELEPORT_WAVE_PARTICLES.get(), TeleportWaveParticles.Provider::new);
        event.registerSpriteSet(ModParticles.TELEPORT_PARTICLES.get(), TeleportParticles.Provider::new);
    }
}
