package net.mathimomos.wormhole_artifact.config;

import net.minecraftforge.common.ForgeConfigSpec;


public class ModConfigs {
    public final ForgeConfigSpec.IntValue WORMHOLE_ARTIFACT_MAX_DURABILITY;
    public final ForgeConfigSpec.IntValue WORMHOLE_REMOTE_MAX_DURABILITY;
    public final ForgeConfigSpec.IntValue WORMHOLE_ARTIFACT_COOLDOWN;
    public final ForgeConfigSpec.IntValue WORMHOLE_REMOTE_COOLDOWN;
    public final ForgeConfigSpec.DoubleValue ENDER_NACRE_DROP_PROBABILITY;

    public ModConfigs(final ForgeConfigSpec.Builder builder) {
        builder.push("wormhole-artifact-config");
        WORMHOLE_ARTIFACT_MAX_DURABILITY = builder
                .comment("Max durability for the wormhole artifact")
                .defineInRange("wormhole_artifact_max_durability", 16, 1, 9999);

        WORMHOLE_REMOTE_MAX_DURABILITY = builder
                .comment("Max durability for the wormhole artifact")
                .defineInRange("wormhole_remote_max_durability", 64, 1, 9999);

        WORMHOLE_ARTIFACT_COOLDOWN = builder
                .comment("Time in seconds for the wormhole artifact cooldown")
                .defineInRange("wormhole_artifact_cooldown", 5, 0, Integer.MAX_VALUE);

        WORMHOLE_REMOTE_COOLDOWN = builder
                .comment("Time in seconds for the wormhole remote cooldown")
                .defineInRange("wormhole_remote_cooldown", 2, 0, Integer.MAX_VALUE);

        ENDER_NACRE_DROP_PROBABILITY = builder
                .comment("Probability from 0 to 1 (1 for 100%) from Ender Pearl to drop an Ender Nacre when teleported (0 for deactivate)")
                .defineInRange("ender_nacre_drop_probability", 0.25, 0, 1);
        builder.pop();
    }
}
