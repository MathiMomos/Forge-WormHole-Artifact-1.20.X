package net.mathimomos.wormhole_artifact.client.sound;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WormholeArtifact.MOD_ID);

    public static final RegistryObject<SoundEvent> TELEPORT_SFX = registerSoundEvents("teleport_sfx");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WormholeArtifact.MOD_ID, name)));
    };

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
