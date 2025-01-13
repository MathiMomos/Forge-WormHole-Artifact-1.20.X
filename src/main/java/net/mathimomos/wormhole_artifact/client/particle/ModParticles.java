package net.mathimomos.wormhole_artifact.client.particle;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WormholeArtifact.MOD_ID);

    public static final RegistryObject<SimpleParticleType> TELEPORT_WAVE_PARTICLES =
            PARTICLE_TYPES.register("teleport_wave_particles", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TELEPORT_PARTICLES =
            PARTICLE_TYPES.register("teleport_particles", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
