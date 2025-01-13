package net.mathimomos.wormhole_artifact.server.message;

import net.mathimomos.wormhole_artifact.client.screen.WormholeArtifactScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
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

    public static void handle(PlayerListResponseMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen instanceof WormholeArtifactScreen screen) {
                screen.updatePlayerData(message.getPlayerData());
            }
        });
        context.setPacketHandled(true);
    }

    public List<PlayerData> getPlayerData() {
        return playerData;
    }
}