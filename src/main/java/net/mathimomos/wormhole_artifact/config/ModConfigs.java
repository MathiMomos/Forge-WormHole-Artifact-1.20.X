package net.mathimomos.wormhole_artifact.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ModConfigs {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.IntValue WORMHOLE_ARTIFACT_COOLDOWN;
    public static final ForgeConfigSpec.IntValue WORMHOLE_REMOTE_COOLDOWN;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Wormhole Artifact Config");
        WORMHOLE_ARTIFACT_COOLDOWN = builder
                .comment("Cooldown time in ticks for the Wormhole Artifact")
                .defineInRange("WormholeArtifactCooldown", 200, 0, Integer.MAX_VALUE);

        WORMHOLE_REMOTE_COOLDOWN = builder
                .comment("Cooldown time in ticks for the Wormhole Remote")
                .defineInRange("WormholeRemoteCooldown", 100, 0, Integer.MAX_VALUE);

        builder.pop();

        COMMON_CONFIG = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }
}
