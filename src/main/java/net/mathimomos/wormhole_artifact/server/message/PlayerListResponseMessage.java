package net.mathimomos.wormhole_artifact.server.message;

import net.mathimomos.wormhole_artifact.client.screen.WormholeArtifactScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PlayerListResponseMessage {
    private final List<PlayerData> playerData;

    public PlayerListResponseMessage(List<PlayerData> playerData) {
        this.playerData = playerData;
    }

    public static void write(PlayerListResponseMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.playerData.size());
        for (PlayerData data : message.playerData) {
            buffer.writeUtf(data.getPlayerName());
            buffer.writeUtf(data.getPlayerDimension());
            buffer.writeInt(data.getPlayerDistance());
        }
    }

    public static PlayerListResponseMessage read(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        List<PlayerData> playerData = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String name = buffer.readUtf();
            String dimension = buffer.readUtf();
            int distance = buffer.readInt();
            playerData.add(new PlayerData(name, dimension, distance));
        }
        return new PlayerListResponseMessage(playerData);
    }

    public static void handle(PlayerListResponseMessage pMessage, Supplier<NetworkEvent.Context> pContextSupplier) {
        NetworkEvent.Context pContext = pContextSupplier.get();
        pContext.enqueueWork(() -> {
            if (pContext.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                // Thx for all Blackbox.ai I was already crying 😭
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(pMessage));
            }
        });
        pContext.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(PlayerListResponseMessage pMessage) {
        List<PlayerData> playerData = pMessage.getPlayerData();
        Minecraft.getInstance().setScreen(new WormholeArtifactScreen(playerData));
    }

    public List<PlayerData> getPlayerData() {
        return playerData;
    }
}