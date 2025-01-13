package net.mathimomos.wormhole_artifact.server.message;

import net.mathimomos.wormhole_artifact.client.screen.WormholeArtifactScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenWormholeArtifactScreenMessage {
    public static void write(OpenWormholeArtifactScreenMessage message, FriendlyByteBuf buffer) {
    }

    public static OpenWormholeArtifactScreenMessage read(FriendlyByteBuf buffer) {
        return new OpenWormholeArtifactScreenMessage();
    }


    public static void handle(OpenWormholeArtifactScreenMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(message));
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(OpenWormholeArtifactScreenMessage pMessage) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof WormholeArtifactScreen)) {
            minecraft.setScreen(new WormholeArtifactScreen());
        }
    }
}
