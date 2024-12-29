package net.mathimomos.wormhole_artifact.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WormholeArtifactItem extends Item {

    private static final int COOLDOWN_TIME_TICKS = 100;

    public WormholeArtifactItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
        }

        return InteractionResultHolder.sidedSuccess(pStack, pLevel.isClientSide());
    }

    public void teleportToTarget(Player pPlayer, ServerPlayer pTargetPlayer, ItemStack pStack, Level pLevel) {

        if (pTargetPlayer != null && pTargetPlayer.isAlive()) {


            pPlayer.teleportTo(pTargetPlayer.getX(), pTargetPlayer.getY(), pTargetPlayer.getZ());


            pStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pPlayer.getUsedItemHand()));

            pPlayer.getCooldowns().addCooldown(this, COOLDOWN_TIME_TICKS);


            String playerName = pPlayer.getDisplayName().getString();
            String targetName = pTargetPlayer.getDisplayName().getString();

            Component message = Component.translatable("text.wormhole_artifact.teleported", playerName, targetName)
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW));

            pLevel.players().forEach(player -> player.sendSystemMessage(message));
        }
    }
}
