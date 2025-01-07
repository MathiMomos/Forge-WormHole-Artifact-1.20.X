package net.mathimomos.wormhole_artifact.server.message;

import net.mathimomos.wormhole_artifact.server.item.custom.WormholeArtifactItem;
import net.mathimomos.wormhole_artifact.server.item.custom.WormholeRemoteItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TeleportToTargetMessage {
    private final String pTargetName;

    public TeleportToTargetMessage(String pTargetName) {
        this.pTargetName = pTargetName;
    }

    public static void write (TeleportToTargetMessage pMessage, FriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(pMessage.pTargetName);
    }

    public static TeleportToTargetMessage read (FriendlyByteBuf pBuffer) {
        return new TeleportToTargetMessage(pBuffer.readUtf());
    }

    public static void handle(TeleportToTargetMessage pMessage, Supplier<NetworkEvent.Context> pContextSupplier) {
        NetworkEvent.Context pContext = pContextSupplier.get();
        pContext.enqueueWork(() -> {
            Player pPlayer = pContext.getSender();
            if (pPlayer instanceof ServerPlayer pServerPlayer) {
                ServerPlayer pTargetPlayer = pServerPlayer.getServer().getPlayerList().getPlayers().stream()
                        .filter(player -> player.getDisplayName().getString().equals(pMessage.pTargetName))
                        .findFirst()
                        .orElse(null);

                if (pTargetPlayer != null) {
                    if (pServerPlayer.getMainHandItem().getItem() instanceof WormholeArtifactItem) {
                        ((WormholeArtifactItem) pServerPlayer.getMainHandItem().getItem())
                                .teleportToTarget(pServerPlayer, pTargetPlayer, pServerPlayer.getMainHandItem(), pServerPlayer.level());
                    } else if (pServerPlayer.getMainHandItem().getItem() instanceof WormholeRemoteItem) {
                        ((WormholeRemoteItem) pServerPlayer.getMainHandItem().getItem())
                                .teleportToTarget(pServerPlayer, pTargetPlayer, pServerPlayer.getMainHandItem(), pServerPlayer.level());
                    }
                } else {
                    pServerPlayer.displayClientMessage(Component.translatable("text.wormhole_artifact.player_not_found")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                }
            }
        });

        pContext.setPacketHandled(true);
    }



}
