package net.mathimomos.wormhole_artifact.server.item.custom;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.message.PlayerListResponseMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WormholeArtifactItem extends Item {
    private static final int COOLDOWN_TIME_TICKS = 100;

    public WormholeArtifactItem(Properties pProperties) {
        super(pProperties);
    }

    private boolean hasWormholeArtifactInInventory(Player pPlayer) {
        for (ItemStack stack : pPlayer.getInventory().items) {
            if (stack.getItem() == this) {
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                List<String> players = pLevel.getServer().getPlayerList().getPlayers().stream()
                        .filter(player -> hasWormholeArtifactInInventory(player))
                        .filter(player -> !player.getName().getString().equals(pPlayer.getName().getString()))
                        .filter(player -> player.isAlive())
                        .map(player -> player.getName().getString())
                        .collect(Collectors.toList());

                PlayerListResponseMessage message = new PlayerListResponseMessage(players);
                WormholeArtifact.NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
            }
        }

        return InteractionResultHolder.sidedSuccess(pStack, pLevel.isClientSide());
    }

    public void teleportToTarget(ServerPlayer pPlayer, ServerPlayer pTargetPlayer, ItemStack pStack, Level pLevel) {
        if (pTargetPlayer != null && pTargetPlayer.isAlive()) {
            pPlayer.teleportTo(pTargetPlayer.getX(), pTargetPlayer.getY(), pTargetPlayer.getZ());
            pStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
            pPlayer.getCooldowns().addCooldown(this, COOLDOWN_TIME_TICKS);

            String pPlayerName = pPlayer.getDisplayName().getString();
            String pTargetPlayerName = pTargetPlayer.getDisplayName().getString();
            pLevel.players().forEach(player -> player.sendSystemMessage(
                    Component.translatable("text.wormhole_artifact.teleported", pPlayerName, pTargetPlayerName)
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
            ));

            if (pLevel instanceof ServerLevel serverLevel) {
                double radius = 1.0;
                int particleCount = 30;
                double height = 2.0;
                double speed = 0.05;

                for (int i = 0; i < particleCount; i++) {
                    double angle = 2 * Math.PI * i / particleCount;
                    double x = pPlayer.getX() + radius * Math.cos(angle);
                    double z = pPlayer.getZ() + radius * Math.sin(angle);
                    double y = pPlayer.getY();

                    for (int j = 0; j < 10; j++) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL, x, y + j * height / 10, z, 1, 0, speed, 0, 3);
                    }
                }
                serverLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 2.0F, 1.0F);

            }
        }
    }
}
