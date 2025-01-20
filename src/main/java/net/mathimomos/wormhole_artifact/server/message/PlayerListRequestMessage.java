package net.mathimomos.wormhole_artifact.server.message;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlayerListRequestMessage {
    public PlayerListRequestMessage() {
    }

    public static void write(PlayerListRequestMessage message, FriendlyByteBuf buffer) {
    }

    public static PlayerListRequestMessage read(FriendlyByteBuf buffer) {
        return new PlayerListRequestMessage();
    }

    public static void handle(PlayerListRequestMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Level pLevel = context.getSender().level();
            ServerPlayer serverPlayer = context.getSender();

            List<PlayerData> playerDataList = pLevel.getServer().getPlayerList().getPlayers().stream()
                    .filter(player -> hasWormholeArtifactsInInventory(player))
                    .filter(player -> !player.getName().getString().equals(serverPlayer.getName().getString()))
                    .filter(player -> player.isAlive())
                    .filter(player -> notMultidimension(serverPlayer) ? player.level() == serverPlayer.level() : true)
                    .map(player -> {
                        String name = player.getName().getString();
                        String dimension = player.level().dimension().location().toString();
                        int distance = (player.level() == serverPlayer.level()) ? (int) Math.sqrt(serverPlayer.distanceToSqr(player)) : -1;
                        return new PlayerData(name, dimension, distance);
                    })
                    .collect(Collectors.toList());

            WormholeArtifact.NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new PlayerListResponseMessage(playerDataList));
        });
        context.setPacketHandled(true);
    }

    private static boolean hasWormholeArtifactsInInventory(Player pPlayer) {
        for (ItemStack stack : pPlayer.getInventory().items) {
            if (stack.getItem() == ModItems.WORMHOLE_ARTIFACT.get() || stack.getItem() == ModItems.WORMHOLE_REMOTE.get()) {
                return true;
            }
        }
        return false;
    }

    private static boolean notMultidimension(Player pPlayer) {
        ItemStack stackMain = pPlayer.getMainHandItem();
        ItemStack stackOff = pPlayer.getOffhandItem();
        Item wormholeArtifact = ModItems.WORMHOLE_ARTIFACT.get();

        return stackMain.getItem() == wormholeArtifact || stackOff.getItem() == wormholeArtifact;
    }
}