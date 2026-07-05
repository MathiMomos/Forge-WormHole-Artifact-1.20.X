package net.mathimomos.wormhole_artifact.server.message;

import net.mathimomos.wormhole_artifact.server.item.custom.WormholeArtifactItem;
import net.mathimomos.wormhole_artifact.server.item.custom.WormholeRemoteItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
            ServerPlayer pServerPlayer = pContext.getSender();

            ServerPlayer pTargetPlayer = pServerPlayer.getServer().getPlayerList().getPlayers().stream()
                    .filter(player -> player.getName().getString().equals(pMessage.pTargetName))
                    .findFirst()
                    .orElse(null);

            if (pTargetPlayer == null) {
                pServerPlayer.displayClientMessage(Component.translatable("text.wormhole_artifact.player_not_found")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                return;
            }

            if (getWormholeItemInHands(pServerPlayer).getItem() instanceof WormholeArtifactItem artifact) {
                artifact.teleportToTarget(pServerPlayer, pTargetPlayer, getWormholeItemInHands(pServerPlayer), pServerPlayer.level());
            } else if (getWormholeItemInHands(pServerPlayer).getItem() instanceof WormholeRemoteItem remote) {
                remote.teleportToTarget(pServerPlayer, pTargetPlayer, getWormholeItemInHands(pServerPlayer), pServerPlayer.level());
            }
        });

        pContext.setPacketHandled(true);
    }

    private static ItemStack getWormholeItemInHands(ServerPlayer player) {
        ItemStack main = player.getMainHandItem();
        if (main.getItem() instanceof WormholeArtifactItem || main.getItem() instanceof WormholeRemoteItem) {
            return main;
        }
        ItemStack off = player.getOffhandItem();
        if (off.getItem() instanceof WormholeArtifactItem || off.getItem() instanceof WormholeRemoteItem) {
            return off;
        }
        return ItemStack.EMPTY;
    }



}
