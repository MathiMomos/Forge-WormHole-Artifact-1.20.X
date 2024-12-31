package net.mathimomos.wormhole_artifact.server.item.custom;

import net.mathimomos.wormhole_artifact.WormholeArtifact;
import net.mathimomos.wormhole_artifact.server.message.PlayerListResponseMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.List;

public class WormholeArtifactItem extends Item {
    private static final int COOLDOWN_TIME_TICKS = 100;

    public WormholeArtifactItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                List<String> players = Arrays.asList(pLevel.getServer().getPlayerList().getPlayerNamesArray());
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

            pLevel.addParticle(ParticleTypes.PORTAL, pPlayer.getX(), pPlayer.getY()+1.0, pPlayer.getZ(),
                    (Math.random() - 0.5) * 0.2, (Math.random() - 0.5) * 0.2, (Math.random() - 0.5) * 0.2);
        }
    }
}
