package net.mathimomos.wormhole_artifact.server.message;

import net.mathimomos.wormhole_artifact.client.screen.WormholeArtifactScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PlayerListResponseMessage {
    private final List<String> playerNames;

    public PlayerListResponseMessage(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public static void write(PlayerListResponseMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.playerNames.size());
        for (String playerName : message.playerNames) {
            buffer.writeUtf(playerName);
        }
    }

    public static PlayerListResponseMessage read(FriendlyByteBuf buffer) {
        int size = buffer.readInt();
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            playerNames.add(buffer.readUtf());
        }
        return new PlayerListResponseMessage(playerNames);
    }

    public static void handle(PlayerListResponseMessage pMessage, Supplier<NetworkEvent.Context> pContextSupplier) {
        NetworkEvent.Context pContext = pContextSupplier.get();
        pContext.enqueueWork(() -> {
            if (pContext.getDirection() == NetworkDirection.PLAY_TO_CLIENT){
                List<String> playerNames = pMessage.getPlayerNames();
                Minecraft.getInstance().setScreen(new WormholeArtifactScreen(playerNames));
            }
        });
        pContext.setPacketHandled(true);
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }
}